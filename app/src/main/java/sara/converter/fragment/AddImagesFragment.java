package sara.converter.fragment;

import static sara.converter.util.Constants.ADD_IMAGES;
import static sara.converter.util.Constants.BUNDLE_DATA;
import static sara.converter.util.Constants.REQUEST_CODE_FOR_WRITE_PERMISSION;
import static sara.converter.util.Constants.WRITE_PERMISSIONS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.dd.morphingbutton.MorphingButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sara.converter.R;
import sara.converter.adapter.MergeFilesAdapter;
import sara.converter.interfaces.BottomSheetPopulate;
import sara.converter.interfaces.OnBackPressedInterface;
import sara.converter.util.BottomSheetCallback;
import sara.converter.util.BottomSheetUtils;
import sara.converter.util.CommonCodeUtils;
import sara.converter.util.DialogUtils;
import sara.converter.util.FileUriUtils;
import sara.converter.util.FileUtils;
import sara.converter.util.ImageUtils;
import sara.converter.util.MorphButtonUtility;
import sara.converter.util.PDFUtils;
import sara.converter.util.PermissionsUtils;
import sara.converter.util.StringUtils;

public class AddImagesFragment extends Fragment implements BottomSheetPopulate,
        MergeFilesAdapter.OnClickListener, OnBackPressedInterface {

    private static final int INTENT_REQUEST_PICK_FILE_CODE = 10;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final ArrayList<String> mImagesUri = new ArrayList<>();
    @BindView((R.id.lottie_progress))
    LottieAnimationView mLottieProgress;
    @BindView((R.id.selectFile))
    MorphingButton selectFileButton;
    @BindView((R.id.pdfCreate))
    MorphingButton mCreatePdf;
    @BindView((R.id.addImages))
    MorphingButton addImages;
    @BindView((R.id.bottom_sheet))
    LinearLayout layoutBottomSheet;
    @BindView((R.id.upArrow))
    ImageView mUpArrow;
    @BindView((R.id.downArrow))
    ImageView mDownArrow;
    @BindView((R.id.layout))
    RelativeLayout mLayout;
    @BindView((R.id.recyclerViewFiles))
    RecyclerView mRecyclerViewFiles;
    @BindView((R.id.tvNoOfImages))
    TextView mNoOfImages;
    private Activity mActivity;
    private String mPath;
    private MorphButtonUtility mMorphButtonUtility;
    private FileUtils mFileUtils;
    private BottomSheetUtils mBottomSheetUtils;
    private PDFUtils mPDFUtils;
    private String mOperation;
    private BottomSheetBehavior<? extends View> mSheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_images, container, false);
        ButterKnife.bind(this, rootView);
        mSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        mSheetBehavior.setBottomSheetCallback(new BottomSheetCallback(mUpArrow, isAdded()));
        mOperation = requireArguments().getString(BUNDLE_DATA);
        mLottieProgress.setVisibility(View.VISIBLE);
        mBottomSheetUtils.populateBottomSheetWithPDFs(this);

        resetValues();
        return rootView;
    }

    @OnClick((R.id.viewFiles))
    void onViewFilesClick() {
        mBottomSheetUtils.showHideSheet(mSheetBehavior);
    }

    /**
     * Displays file chooser intent
     */
    @OnClick((R.id.selectFile))
    public void showFileChooser() {
        startActivityForResult(mFileUtils.getFileChooser(),
                INTENT_REQUEST_PICK_FILE_CODE);
    }

    /**
     * Called after Matisse Activity is called
     *
     * @param requestCode REQUEST Code for opening Matisse Activity
     * @param resultCode  result code of the process
     * @param data        Data of the image selected
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        switch (requestCode) {
            case INTENT_REQUEST_GET_IMAGES -> {
                mImagesUri.clear();
                mImagesUri.addAll(Matisse.obtainPathResult(data));
                if (mImagesUri.size() > 0) {
                    mNoOfImages.setText(String.format(mActivity.getResources()
                            .getString(R.string.images_selected), mImagesUri.size()));
                    mNoOfImages.setVisibility(View.VISIBLE);
                    StringUtils.getInstance().showSnackbar(mActivity, R.string.snackbar_images_added);
                    mCreatePdf.setEnabled(true);
                } else {
                    mNoOfImages.setVisibility(View.GONE);
                }
                mMorphButtonUtility.morphToSquare(mCreatePdf, mMorphButtonUtility.integer());
            }
            case INTENT_REQUEST_PICK_FILE_CODE ->
                    setTextAndActivateButtons(FileUriUtils.getInstance().getFilePath(data.getData()));
        }
    }

    private void getRuntimePermissions() {
        PermissionsUtils.getInstance().requestRuntimePermissions(this,
                WRITE_PERMISSIONS,
                REQUEST_CODE_FOR_WRITE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsUtils.getInstance().handleRequestPermissionsResult(mActivity, grantResults,
                requestCode, REQUEST_CODE_FOR_WRITE_PERMISSION, this::selectImages);
    }

    @OnClick((R.id.pdfCreate))
    public void parse() {
        StringUtils.getInstance().hideKeyboard(mActivity);
        if (mOperation.equals(ADD_IMAGES))
            getFileName();
    }

    private void getFileName() {
        MaterialDialog.Builder builder = DialogUtils.getInstance().createCustomDialog(mActivity,
                R.string.creating_pdf, R.string.enter_file_name);
        builder.input(getString(R.string.example), null, (dialog, input) -> {
            if (StringUtils.getInstance().isEmpty(input)) {
                StringUtils.getInstance().showSnackbar(mActivity, R.string.snackbar_name_not_blank);
            } else {
                final String filename = input.toString();
                FileUtils utils = new FileUtils(mActivity);
                if (!utils.isFileExist(filename + getString(R.string.pdf_ext))) {
                    this.addImagesToPdf(filename);
                } else {
                    MaterialDialog.Builder builder2 = DialogUtils.getInstance().createOverwriteDialog(mActivity);
                    builder2.onPositive((dialog2, which) ->
                            this.addImagesToPdf(filename)).onNegative((dialog1, which) -> getFileName()).show();
                }
            }
        }).show();
    }

    /**
     * Adds images to existing PDF
     *
     * @param output - path of output PDF
     */
    private void addImagesToPdf(String output) {
        int index = mPath.lastIndexOf("/");
        String outputPath = mPath.replace(mPath.substring(index + 1),
                output + mActivity.getString(R.string.pdf_ext));

        if (mImagesUri.size() > 0) {
            MaterialDialog progressDialog = DialogUtils.getInstance().createAnimationDialog(mActivity);
            progressDialog.show();
            mPDFUtils.addImagesToPdf(mPath, outputPath, mImagesUri);
            mMorphButtonUtility.morphToSuccess(mCreatePdf);
            resetValues();
            progressDialog.dismiss();
        } else {
            StringUtils.getInstance().showSnackbar(mActivity, R.string.no_images_selected);
        }
    }

    private void resetValues() {
        mPath = null;
        mImagesUri.clear();
        mMorphButtonUtility.initializeButton(selectFileButton, mCreatePdf);
        mMorphButtonUtility.initializeButton(selectFileButton, addImages);
        mNoOfImages.setVisibility(View.GONE);
    }

    /**
     * Adding Images to PDF
     */
    @OnClick((R.id.addImages))
    void startAddingImages() {
        PermissionsUtils.getInstance().checkStoragePermissionAndProceed(getContext(), this::selectImages);
    }

    /**
     * Opens Matisse activity to select Images
     */
    private void selectImages() {
        ImageUtils.selectImages(this, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mMorphButtonUtility = new MorphButtonUtility(mActivity);
        mFileUtils = new FileUtils(mActivity);
        mPDFUtils = new PDFUtils(mActivity);
        mBottomSheetUtils = new BottomSheetUtils(mActivity);
    }

    @Override
    public void onItemClick(String path) {
        mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setTextAndActivateButtons(path);
    }

    private void setTextAndActivateButtons(String path) {
        mPath = path;
        mMorphButtonUtility.setTextAndActivateButtons(path,
                selectFileButton, addImages);

    }

    @Override
    public void onPopulate(ArrayList<String> paths) {
        CommonCodeUtils.getInstance().populateUtil(mActivity, paths,
                this, mLayout, mLottieProgress, mRecyclerViewFiles);
    }

    @Override
    public void closeBottomSheet() {
        CommonCodeUtils.getInstance().closeBottomSheetUtil(mSheetBehavior);
    }

    @Override
    public boolean checkSheetBehaviour() {
        return CommonCodeUtils.getInstance().checkSheetBehaviourUtil(mSheetBehavior);
    }
}

package sara.converter.fragment;

import static sara.converter.util.Constants.ADD_IMAGES;
import static sara.converter.util.Constants.ADD_IMAGES_KEY;
import static sara.converter.util.Constants.ADD_PASSWORD_KEY;
import static sara.converter.util.Constants.ADD_PWD;
import static sara.converter.util.Constants.ADD_TEXT_KEY;
import static sara.converter.util.Constants.ADD_WATERMARK;
import static sara.converter.util.Constants.ADD_WATERMARK_KEY;
import static sara.converter.util.Constants.BUNDLE_DATA;
import static sara.converter.util.Constants.COMPRESS_PDF;
import static sara.converter.util.Constants.COMPRESS_PDF_KEY;
import static sara.converter.util.Constants.EXCEL_TO_PDF_KEY;
import static sara.converter.util.Constants.EXTRACT_IMAGES;
import static sara.converter.util.Constants.EXTRACT_IMAGES_KEY;
import static sara.converter.util.Constants.EXTRACT_TEXT_KEY;
import static sara.converter.util.Constants.HISTORY_KEY;
import static sara.converter.util.Constants.IMAGE_TO_PDF_KEY;
import static sara.converter.util.Constants.INVERT_PDF_KEY;
import static sara.converter.util.Constants.MERGE_PDF_KEY;
import static sara.converter.util.Constants.PDF_TO_IMAGES;
import static sara.converter.util.Constants.PDF_TO_IMAGES_KEY;
import static sara.converter.util.Constants.QR_BARCODE_KEY;
import static sara.converter.util.Constants.REMOVE_DUPLICATE_PAGES_KEY;
import static sara.converter.util.Constants.REMOVE_PAGES;
import static sara.converter.util.Constants.REMOVE_PAGES_KEY;
import static sara.converter.util.Constants.REMOVE_PASSWORD_KEY;
import static sara.converter.util.Constants.REMOVE_PWd;
import static sara.converter.util.Constants.REORDER_PAGES;
import static sara.converter.util.Constants.REORDER_PAGES_KEY;
import static sara.converter.util.Constants.ROTATE_PAGES;
import static sara.converter.util.Constants.ROTATE_PAGES_KEY;
import static sara.converter.util.Constants.SPLIT_PDF_KEY;
import static sara.converter.util.Constants.TEXT_TO_PDF_KEY;
import static sara.converter.util.Constants.VIEW_FILES_KEY;
import static sara.converter.util.Constants.ZIP_TO_PDF_KEY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sara.converter.R;
import sara.converter.activity.FavouritesActivity;
import sara.converter.activity.MainActivity;
import sara.converter.customviews.MyCardView;
import sara.converter.fragment.texttopdf.TextToPdfFragment;
import sara.converter.model.HomePageItem;
import sara.converter.util.CommonCodeUtils;

public class FavouritesFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        View.OnClickListener {
    @BindView((R.id.images_to_pdf_fav))
    MyCardView pref_img_to_pdf;
    @BindView((R.id.text_to_pdf_fav))
    MyCardView pref_text_to_pdf;
    @BindView((R.id.qr_barcode_to_pdf_fav))
    MyCardView pref_qr_barcode;
    @BindView((R.id.view_files_fav))
    MyCardView pref_view_files;
    @BindView((R.id.view_history_fav))
    MyCardView pref_history;
    @BindView((R.id.add_password_fav))
    MyCardView pref_add_password;
    @BindView((R.id.remove_password_fav))
    MyCardView pref_rem_pass;
    @BindView((R.id.rotate_pages_fav))
    MyCardView pref_rot_pages;
    @BindView((R.id.add_watermark_fav))
    MyCardView pref_add_watermark;
    @BindView((R.id.add_images_fav))
    MyCardView pref_add_images;
    @BindView((R.id.merge_pdf_fav))
    MyCardView pref_merge_pdf;
    @BindView((R.id.split_pdf_fav))
    MyCardView pref_split_pdf;
    @BindView((R.id.invert_pdf_fav))
    MyCardView pref_invert_pdf;
    @BindView((R.id.compress_pdf_fav))
    MyCardView pref_compress;
    @BindView((R.id.remove_duplicates_pages_pdf_fav))
    MyCardView pref_rem_dup_pages;
    @BindView((R.id.remove_pages_fav))
    MyCardView pref_remove_pages;
    @BindView((R.id.rearrange_pages_fav))
    MyCardView pref_reorder_pages;
    @BindView((R.id.extract_images_fav))
    MyCardView pref_extract_img;
    @BindView((R.id.pdf_to_images_fav))
    MyCardView pref_pdf_to_img;
    @BindView((R.id.extract_text_fav))
    MyCardView pref_extract_txt;
    @BindView((R.id.excel_to_pdf_fav))
    MyCardView pref_excel_to_pdf;
    @BindView((R.id.add_text_fav))
    MyCardView pref_add_text;
    @BindView((R.id.favourites))
    LottieAnimationView favouritesAnimation;
    @BindView((R.id.favourites_text))
    TextView favouritesText;
    @BindView((R.id.zip_to_pdf_fav))
    MyCardView pref_zip_to_pdf;
    private SharedPreferences mSharedpreferences;
    private boolean mDoesFavouritesExist;
    private Activity mActivity;
    private Map<Integer, HomePageItem> mFragmentPositionMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourites_fragment, container, false);
        ButterKnife.bind(this, rootView);

        mSharedpreferences = PreferenceManager
                .getDefaultSharedPreferences(mActivity);
        mSharedpreferences.registerOnSharedPreferenceChangeListener(this);

        initializeValues();

        setHasOptionsMenu(true);
        return rootView;
    }

    /**
     * Initializes listeners & default values
     */
    private void initializeValues() {

        mDoesFavouritesExist = false;
        checkFavourites(mSharedpreferences);
        mFragmentPositionMap = CommonCodeUtils.getInstance().fillNavigationItemsMap(false);

        pref_img_to_pdf.setOnClickListener(this);
        pref_text_to_pdf.setOnClickListener(this);
        pref_qr_barcode.setOnClickListener(this);
        pref_view_files.setOnClickListener(this);
        pref_history.setOnClickListener(this);
        pref_extract_txt.setOnClickListener(this);
        pref_add_text.setOnClickListener(this);
        pref_split_pdf.setOnClickListener(this);
        pref_merge_pdf.setOnClickListener(this);
        pref_compress.setOnClickListener(this);
        pref_remove_pages.setOnClickListener(this);
        pref_reorder_pages.setOnClickListener(this);
        pref_extract_img.setOnClickListener(this);
        pref_pdf_to_img.setOnClickListener(this);
        pref_add_password.setOnClickListener(this);
        pref_rem_pass.setOnClickListener(this);
        pref_rot_pages.setOnClickListener(this);
        pref_add_watermark.setOnClickListener(this);
        pref_add_images.setOnClickListener(this);
        pref_rem_dup_pages.setOnClickListener(this);
        pref_invert_pdf.setOnClickListener(this);
        pref_excel_to_pdf.setOnClickListener(this);
        pref_zip_to_pdf.setOnClickListener(this);

    }

    @OnClick((R.id.fav_add_fab))
    public void onAddFavouriteButtonClicked() {
        startActivity(new Intent(this.getContext(), FavouritesActivity.class));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.menu_favourites_item).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    /**
     * This method checks for the favourites from preferences list
     * and passes them to another method for dealing with the required view.
     *
     * @param sharedPreferences - preferences instance
     */
    private void checkFavourites(SharedPreferences sharedPreferences) {

        // assigned due to onSharedPreferenceChanged
        mSharedpreferences = sharedPreferences;
        viewVisibility(pref_img_to_pdf, IMAGE_TO_PDF_KEY);
        viewVisibility(pref_text_to_pdf, TEXT_TO_PDF_KEY);
        viewVisibility(pref_qr_barcode, QR_BARCODE_KEY);
        viewVisibility(pref_view_files, VIEW_FILES_KEY);
        viewVisibility(pref_history, HISTORY_KEY);
        viewVisibility(pref_add_text, ADD_TEXT_KEY);
        viewVisibility(pref_add_password, ADD_PASSWORD_KEY);
        viewVisibility(pref_rem_pass, REMOVE_PASSWORD_KEY);
        viewVisibility(pref_rot_pages, ROTATE_PAGES_KEY);
        viewVisibility(pref_add_watermark, ADD_WATERMARK_KEY);
        viewVisibility(pref_add_images, ADD_IMAGES_KEY);
        viewVisibility(pref_merge_pdf, MERGE_PDF_KEY);
        viewVisibility(pref_split_pdf, SPLIT_PDF_KEY);
        viewVisibility(pref_invert_pdf, INVERT_PDF_KEY);
        viewVisibility(pref_compress, COMPRESS_PDF_KEY);
        viewVisibility(pref_rem_dup_pages, REMOVE_DUPLICATE_PAGES_KEY);
        viewVisibility(pref_remove_pages, REMOVE_PAGES_KEY);
        viewVisibility(pref_reorder_pages, REORDER_PAGES_KEY);
        viewVisibility(pref_extract_txt, EXTRACT_TEXT_KEY);
        viewVisibility(pref_extract_img, EXTRACT_IMAGES_KEY);
        viewVisibility(pref_pdf_to_img, PDF_TO_IMAGES_KEY);
        viewVisibility(pref_excel_to_pdf, EXCEL_TO_PDF_KEY);
        viewVisibility(pref_zip_to_pdf, ZIP_TO_PDF_KEY);

        // if there are no favourites then show favourites animation and text
        if (!mDoesFavouritesExist) {
            favouritesAnimation.setVisibility(View.VISIBLE);
            favouritesText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * This method toggles the visibility of the passed view.
     *
     * @param view - the view, whose visibility is to be modified
     * @param id   - get the preference value using id
     */
    private void viewVisibility(View view, String id) {
        if (mSharedpreferences.getBoolean(id, false)) {
            view.setVisibility(View.VISIBLE);
            // if any favourites exists set mDoesFavouritesExist to true
            mDoesFavouritesExist = true;
            // & disable favourites animation and text
            favouritesAnimation.setVisibility(View.GONE);
            favouritesText.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        checkFavourites(sharedPreferences);
    }

    @Override
    public void onClick(@NonNull View v) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        setTitleFragment(mFragmentPositionMap.get(v.getId()).getTitleString());

        switch (v.getId()) {
            case (R.id.images_to_pdf_fav) -> fragment = new ImageToPdfFragment();
            case (R.id.qr_barcode_to_pdf_fav) -> fragment = new QrBarcodeScanFragment();
            case (R.id.text_to_pdf_fav) -> fragment = new TextToPdfFragment();
            case (R.id.view_files_fav) -> fragment = new ViewFilesFragment();
            case (R.id.view_history_fav) -> fragment = new HistoryFragment();
            case (R.id.add_text_fav) -> fragment = new AddTextFragment();
            case (R.id.merge_pdf_fav) -> fragment = new MergeFilesFragment();
            case (R.id.split_pdf_fav) -> fragment = new SplitFilesFragment();
            case (R.id.compress_pdf_fav) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, COMPRESS_PDF);
                fragment.setArguments(bundle);
            }
            case (R.id.extract_images_fav) -> {
                fragment = new PdfToImageFragment();
                bundle.putString(BUNDLE_DATA, EXTRACT_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.pdf_to_images_fav) -> {
                fragment = new PdfToImageFragment();
                bundle.putString(BUNDLE_DATA, PDF_TO_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.remove_pages_fav) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.rearrange_pages_fav) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REORDER_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.add_password_fav) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_PWD);
                fragment.setArguments(bundle);
            }
            case (R.id.remove_password_fav) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PWd);
                fragment.setArguments(bundle);
            }
            case (R.id.rotate_pages_fav) -> {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ROTATE_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.add_watermark_fav) -> {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ADD_WATERMARK);
                fragment.setArguments(bundle);
            }
            case (R.id.add_images_fav) -> {
                fragment = new AddImagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.remove_duplicates_pages_pdf_fav) ->
                    fragment = new RemoveDuplicatePagesFragment();
            case (R.id.invert_pdf_fav) -> fragment = new InvertPdfFragment();
            case (R.id.extract_text_fav) -> fragment = new ExtractTextFragment();
            case (R.id.excel_to_pdf_fav) -> fragment = new ExceltoPdfFragment();
            case (R.id.zip_to_pdf_fav) -> fragment = new ZipToPdfFragment();
        }
        try {
            if (fragment != null && fragmentManager != null) {
                ((MainActivity) mActivity).setNavigationViewSelection(mFragmentPositionMap.get(
                        v.getId()).getNavigationItemId());
                fragmentManager.beginTransaction()
                        .replace(R.id.content, fragment)
                        .addToBackStack(getString(R.string.favourites))
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sets the title on action bar
     *
     * @param title - the string id to be set
     */
    private void setTitleFragment(int title) {
        if (title != 0)
            mActivity.setTitle(title);
    }
}

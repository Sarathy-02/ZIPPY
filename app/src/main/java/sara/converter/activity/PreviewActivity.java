package sara.converter.activity;

import static sara.converter.util.Constants.PREVIEW_IMAGES;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eftimoff.viewpagertransformers.DepthPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sara.converter.R;
import sara.converter.adapter.PreviewAdapter;
import sara.converter.adapter.PreviewImageOptionsAdapter;
import sara.converter.model.PreviewImageOptionItem;
import sara.converter.util.Constants;
import sara.converter.util.ImageSortUtils;
import sara.converter.util.ThemeUtils;

public class PreviewActivity extends AppCompatActivity implements PreviewImageOptionsAdapter.OnItemClickListener {

    private static final int INTENT_REQUEST_REARRANGE_IMAGE = 1;
    @BindView((R.id.recyclerView))
    RecyclerView mRecyclerView;
    private ArrayList<String> mImagesArrayList;
    private PreviewAdapter mPreviewAdapter;
    private ViewPager mViewPager;

    @NonNull
    public static Intent getStartIntent(Context context, ArrayList<String> uris) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(PREVIEW_IMAGES, uris);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.getInstance().setThemeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);
        // Extract mImagesArrayList uri array from the intent
        Intent intent = getIntent();
        mImagesArrayList = intent.getStringArrayListExtra(PREVIEW_IMAGES);

        mViewPager = findViewById(R.id.viewpager);
        mPreviewAdapter = new PreviewAdapter(this, mImagesArrayList);
        mViewPager.setAdapter(mPreviewAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        showOptions();
    }

    /**
     * Shows preview options at the bottom of activity
     */
    private void showOptions() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        PreviewImageOptionsAdapter adapter = new PreviewImageOptionsAdapter(this, getOptions(),
                getApplicationContext());
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Returns a list of options for preview activity
     *
     * @return - list
     */
    @NonNull
    private ArrayList<PreviewImageOptionItem> getOptions() {
        ArrayList<PreviewImageOptionItem> mOptions = new ArrayList<>();
        mOptions.add(new PreviewImageOptionItem(R.drawable.ic_rearrange, getString(R.string.rearrange_text)));
        mOptions.add(new PreviewImageOptionItem(R.drawable.ic_sort, getString(R.string.sort)));
        return mOptions;
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0 -> startActivityForResult(RearrangeImages.getStartIntent(this, mImagesArrayList),
                    INTENT_REQUEST_REARRANGE_IMAGE);
            case 1 -> sortImages();
        }
    }

    /**
     * Shows a dialog to sort images
     */
    private void sortImages() {
        new MaterialDialog.Builder(this)
                .title(R.string.sort_by_title)
                .items(R.array.sort_options_images)
                .itemsCallback((dialog, itemView, position, text) -> {
                    ImageSortUtils.getInstance().performSortOperation(position, mImagesArrayList);
                    mPreviewAdapter.setData(new ArrayList<>(mImagesArrayList));
                    mViewPager.setAdapter(mPreviewAdapter);
                })
                .negativeText(R.string.cancel)
                .show();
    }

    /**
     * Sends the resultant uri back to calling activity
     */
    private void passUris() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra(Constants.RESULT, mImagesArrayList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == INTENT_REQUEST_REARRANGE_IMAGE) {
            try {
                mImagesArrayList = data.getStringArrayListExtra(Constants.RESULT);
                mPreviewAdapter.setData(mImagesArrayList);
                mViewPager.setAdapter(mPreviewAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        passUris();
    }
}
package swati4star.createpdf.providers.fragmentmanagement;

import static swati4star.createpdf.util.Constants.ACTION_MERGE_PDF;
import static swati4star.createpdf.util.Constants.ACTION_SELECT_IMAGES;
import static swati4star.createpdf.util.Constants.ACTION_TEXT_TO_PDF;
import static swati4star.createpdf.util.Constants.ACTION_VIEW_FILES;
import static swati4star.createpdf.util.Constants.ADD_IMAGES;
import static swati4star.createpdf.util.Constants.ADD_PWD;
import static swati4star.createpdf.util.Constants.ADD_WATERMARK;
import static swati4star.createpdf.util.Constants.BUNDLE_DATA;
import static swati4star.createpdf.util.Constants.COMPRESS_PDF;
import static swati4star.createpdf.util.Constants.EXTRACT_IMAGES;
import static swati4star.createpdf.util.Constants.OPEN_SELECT_IMAGES;
import static swati4star.createpdf.util.Constants.PDF_TO_IMAGES;
import static swati4star.createpdf.util.Constants.REMOVE_PAGES;
import static swati4star.createpdf.util.Constants.REMOVE_PWd;
import static swati4star.createpdf.util.Constants.REORDER_PAGES;
import static swati4star.createpdf.util.Constants.ROTATE_PAGES;
import static swati4star.createpdf.util.Constants.SHOW_WELCOME_ACT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import swati4star.createpdf.R;
import swati4star.createpdf.activity.WelcomeActivity;
import swati4star.createpdf.fragment.AboutUsFragment;
import swati4star.createpdf.fragment.AddImagesFragment;
import swati4star.createpdf.fragment.AddTextFragment;
import swati4star.createpdf.fragment.ExceltoPdfFragment;
import swati4star.createpdf.fragment.ExtractTextFragment;
import swati4star.createpdf.fragment.FAQFragment;
import swati4star.createpdf.fragment.FavouritesFragment;
import swati4star.createpdf.fragment.HistoryFragment;
import swati4star.createpdf.fragment.HomeFragment;
import swati4star.createpdf.fragment.ImageToPdfFragment;
import swati4star.createpdf.fragment.InvertPdfFragment;
import swati4star.createpdf.fragment.MergeFilesFragment;
import swati4star.createpdf.fragment.PdfToImageFragment;
import swati4star.createpdf.fragment.QrBarcodeScanFragment;
import swati4star.createpdf.fragment.RemoveDuplicatePagesFragment;
import swati4star.createpdf.fragment.RemovePagesFragment;
import swati4star.createpdf.fragment.SettingsFragment;
import swati4star.createpdf.fragment.SplitFilesFragment;
import swati4star.createpdf.fragment.ViewFilesFragment;
import swati4star.createpdf.fragment.ZipToPdfFragment;
import swati4star.createpdf.fragment.texttopdf.TextToPdfFragment;
import swati4star.createpdf.util.FeedbackUtils;
import swati4star.createpdf.util.FragmentUtils;

/**
 * This is a fragment service that manages the fragments
 * mainly for the MainActivity.
 */
public class FragmentManagement implements IFragmentManagement {
    private final FragmentActivity mContext;
    private final NavigationView mNavigationView;
    private final FeedbackUtils mFeedbackUtils;
    private final FragmentUtils mFragmentUtils;
    private boolean mDoubleBackToExitPressedOnce = false;

    public FragmentManagement(FragmentActivity context, NavigationView navigationView) {
        mContext = context;
        mNavigationView = navigationView;
        mFeedbackUtils = new FeedbackUtils(mContext);
        mFragmentUtils = new FragmentUtils(mContext);
    }

    public void favouritesFragmentOption() {
        Fragment currFragment = mContext.getSupportFragmentManager().findFragmentById(R.id.content);

        Fragment fragment = new FavouritesFragment();
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.content, fragment);
        if (!(currFragment instanceof HomeFragment)) {
            transaction.addToBackStack(mFragmentUtils.getFragmentName(currFragment));
        }
        transaction.commit();
    }

    public Fragment checkForAppShortcutClicked() {
        Fragment fragment = new HomeFragment();
        if (mContext.getIntent().getAction() != null) {
            switch (Objects.requireNonNull(mContext.getIntent().getAction())) {
                case ACTION_SELECT_IMAGES -> {
                    fragment = new ImageToPdfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(OPEN_SELECT_IMAGES, true);
                    fragment.setArguments(bundle);
                }
                case ACTION_VIEW_FILES -> {
                    fragment = new ViewFilesFragment();
                    setNavigationViewSelection(R.id.nav_gallery);
                }
                case ACTION_TEXT_TO_PDF -> {
                    fragment = new TextToPdfFragment();
                    setNavigationViewSelection(R.id.nav_text_to_pdf);
                }
                case ACTION_MERGE_PDF -> {
                    fragment = new MergeFilesFragment();
                    setNavigationViewSelection(R.id.nav_merge);
                }
                default -> fragment = new HomeFragment(); // Set default fragment
            }
        }
        if (areImagesReceived())
            fragment = new ImageToPdfFragment();

        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        return fragment;
    }

    public boolean handleBackPressed() {
        Fragment currentFragment = mContext.getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (currentFragment instanceof HomeFragment) {
            return checkDoubleBackPress();
        } else {
            if (mFragmentUtils.handleFragmentBottomSheetBehavior(currentFragment))
                return false;
        }
        handleBackStackEntry();
        return false;
    }

    public boolean handleNavigationItemSelected(int itemId) {
        Fragment fragment = null;
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        Bundle bundle = new Bundle();

        switch (itemId) {
            case (R.id.nav_home) -> fragment = new HomeFragment();
            case (R.id.nav_camera) -> fragment = new ImageToPdfFragment();
            case (R.id.nav_qrcode) -> fragment = new QrBarcodeScanFragment();
            case (R.id.nav_gallery) -> fragment = new ViewFilesFragment();
            case (R.id.nav_merge) -> fragment = new MergeFilesFragment();
            case (R.id.nav_split) -> fragment = new SplitFilesFragment();
            case (R.id.nav_text_to_pdf) -> fragment = new TextToPdfFragment();
            case (R.id.nav_history) -> fragment = new HistoryFragment();
            case (R.id.nav_add_text) -> fragment = new AddTextFragment();
            case (R.id.nav_add_password) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_PWD);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_remove_password) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PWd);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_share) -> mFeedbackUtils.shareApplication();
            case (R.id.nav_about) -> fragment = new AboutUsFragment();
            case (R.id.nav_settings) -> fragment = new SettingsFragment();
            case (R.id.nav_extract_images) -> {
                fragment = new PdfToImageFragment();
                bundle.putString(BUNDLE_DATA, EXTRACT_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_pdf_to_images) -> {
                fragment = new PdfToImageFragment();
                bundle.putString(BUNDLE_DATA, PDF_TO_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_excel_to_pdf) -> fragment = new ExceltoPdfFragment();
            case (R.id.nav_remove_pages) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REMOVE_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_rearrange_pages) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, REORDER_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_compress_pdf) -> {
                fragment = new RemovePagesFragment();
                bundle.putString(BUNDLE_DATA, COMPRESS_PDF);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_add_images) -> {
                fragment = new AddImagesFragment();
                bundle.putString(BUNDLE_DATA, ADD_IMAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_help) -> {
                Intent intent = new Intent(mContext, WelcomeActivity.class);
                intent.putExtra(SHOW_WELCOME_ACT, true);
                mContext.startActivity(intent);
            }
            case (R.id.nav_remove_duplicate_pages) -> fragment = new RemoveDuplicatePagesFragment();
            case (R.id.nav_invert_pdf) -> fragment = new InvertPdfFragment();
            case (R.id.nav_add_watermark) -> {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ADD_WATERMARK);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_zip_to_pdf) -> fragment = new ZipToPdfFragment();
            case (R.id.nav_rateus) ->
                    mFeedbackUtils.openWebPage("https://play.google.com/store/apps/details?id=swati4star.createpdf");
            case (R.id.nav_rotate_pages) -> {
                fragment = new ViewFilesFragment();
                bundle.putInt(BUNDLE_DATA, ROTATE_PAGES);
                fragment.setArguments(bundle);
            }
            case (R.id.nav_text_extract) -> fragment = new ExtractTextFragment();
            case (R.id.nav_faq) -> fragment = new FAQFragment();
        }

        try {
            if (fragment != null)
                fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if help or share is clicked then return false, as we don't want
        // them to be selected
        return itemId != R.id.nav_share && itemId != R.id.nav_help;
    }

    /**
     * Closes the app only when double clicked
     */
    private boolean checkDoubleBackPress() {
        if (mDoubleBackToExitPressedOnce) {
            return true;
        }
        mDoubleBackToExitPressedOnce = true;
        Toast.makeText(mContext, R.string.confirm_exit_message, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Back stack count will be 1 when we open a item from favourite menu
     * on clicking back, return back to fav menu and change title
     */
    private void handleBackStackEntry() {
        int count = mContext.getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            String s = mContext.getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
            mContext.setTitle(s);
            mContext.getSupportFragmentManager().popBackStack();
        } else {
            Fragment fragment = new HomeFragment();
            mContext.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            mContext.setTitle(R.string.app_name);
            setNavigationViewSelection(R.id.nav_home);
        }
    }

    private boolean areImagesReceived() {
        Intent intent = mContext.getIntent();
        String type = intent.getType();
        return type != null && type.startsWith("image/");
    }

    private void setNavigationViewSelection(int id) {
        mNavigationView.setCheckedItem(id);
    }
}

package sara.converter.util;

import static sara.converter.util.Constants.THEME_BLACK;
import static sara.converter.util.Constants.THEME_DARK;
import static sara.converter.util.Constants.THEME_WHITE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.FrameLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.dd.morphingbutton.MorphingButton;

import sara.converter.R;

public class MorphButtonUtility {

    private final Activity mActivity;
    private boolean mDarkModeEnabled = false;

    public MorphButtonUtility(Activity activity) {
        mActivity = activity;
        checkDarkMode();
    }

    public int integer() {
        return mActivity.getResources().getInteger(R.integer.mb_animation);
    }

    private int dimen(@DimenRes int resId) {
        return (int) mActivity.getResources().getDimension(resId);
    }

    private int color(@ColorRes int resId) {
        return ContextCompat.getColor(mActivity, resId);
    }

    private void checkDarkMode() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String themeName = sharedPreferences.getString(Constants.DEFAULT_THEME_TEXT,
                Constants.DEFAULT_THEME);
        switch (themeName) {
            case THEME_WHITE -> mDarkModeEnabled = false;
            case THEME_BLACK, THEME_DARK -> mDarkModeEnabled = true;
            default ->
                    mDarkModeEnabled = (mActivity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        }
    }

    /**
     * Converts morph button ot square shape
     *
     * @param btnMorph the button to be converted
     * @param duration time period of transition
     */
    public void morphToSquare(@NonNull final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = defaultButton(duration);
        String text = btnMorph.getText().toString().isEmpty() ?
                mActivity.getString(R.string.create_pdf) :
                btnMorph.getText().toString();
        if (mDarkModeEnabled) {
            square.color(color(R.color.colorBlackAltLight));
            square.colorPressed(color(R.color.colorBlackAlt));
        } else {
            square.color(color(R.color.mb_blue));
            square.colorPressed(color(R.color.mb_blue_dark));
        }
        square.text(text);
        btnMorph.morph(square);
    }

    /**
     * Converts morph button into success shape
     *
     * @param btnMorph the button to be converted
     */
    public void morphToSuccess(@NonNull final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer())
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_check_white_24dp);
        btnMorph.morph(circle);
    }

    /**
     * Converts morph button ot square shape
     *
     * @param btnMorph the button to be converted
     * @param duration time period of transition
     */
    public void morphToGrey(@NonNull final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = defaultButton(duration);
        square.color(color(R.color.mb_gray));
        square.colorPressed(color(R.color.mb_gray));
        square.text(btnMorph.getText().toString());
        btnMorph.morph(square);
    }

    /**
     * Return morphing button params with default values
     *
     * @param duration - duration of transition
     * @return - params object
     */
    private MorphingButton.Params defaultButton(int duration) {
        return MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(FrameLayout.LayoutParams.MATCH_PARENT)
                .height(FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setTextAndActivateButtons(String path,
                                          @NonNull MorphingButton toSetPathOn,
                                          @NonNull MorphingButton toEnable) {
        toSetPathOn.setText(path);
        toSetPathOn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.mb_green_dark));
        toEnable.setEnabled(true);
        morphToSquare(toEnable, integer());
    }

    public void initializeButton(@NonNull MorphingButton button,
                                 MorphingButton buttonToDisable) {
        button.setText(R.string.merge_file_select);
        if (mDarkModeEnabled) {
            button.setBackgroundColor(color(R.color.colorBlackAltLight));
        }
        morphToGrey(buttonToDisable, integer());
        buttonToDisable.setEnabled(false);
    }

    public void initializeButtonForAddText(@NonNull MorphingButton pdfButton, @NonNull MorphingButton textButton,
                                           MorphingButton buttonToDisable) {
        pdfButton.setText(R.string.select_pdf_file);
        textButton.setText(R.string.select_text_file);
        if (mDarkModeEnabled) {
            pdfButton.setBackgroundColor(color(R.color.colorBlackAltLight));
            textButton.setBackgroundColor(color(R.color.colorBlackAltLight));
        }
        morphToGrey(buttonToDisable, integer());
        buttonToDisable.setEnabled(false);
    }
}

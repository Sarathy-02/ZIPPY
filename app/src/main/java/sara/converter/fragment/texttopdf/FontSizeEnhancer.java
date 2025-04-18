package sara.converter.fragment.texttopdf;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;

import sara.converter.R;
import sara.converter.interfaces.Enhancer;
import sara.converter.model.EnhancementOptionsEntity;
import sara.converter.model.TextToPDFOptions;
import sara.converter.preferences.TextToPdfPreferences;
import sara.converter.util.StringUtils;

/**
 * An {@link Enhancer} that lets you select font size.
 */
public class FontSizeEnhancer implements Enhancer {

    private final Activity mActivity;
    private final TextToPdfContract.View mView;
    private final TextToPdfPreferences mPreferences;
    private final TextToPDFOptions.Builder mBuilder;
    @NonNull private final EnhancementOptionsEntity mEnhancementOptionsEntity;

    FontSizeEnhancer(@NonNull final Activity activity,
                     @NonNull final TextToPdfContract.View view,
                     @NonNull final TextToPDFOptions.Builder builder) {
        mActivity = activity;
        mPreferences = new TextToPdfPreferences(activity);
        mBuilder = builder;
        mBuilder.setFontSizeTitle(
                String.format(mActivity.getString(R.string.edit_font_size),
                        mPreferences.getFontSize()));
        mEnhancementOptionsEntity = new EnhancementOptionsEntity(
                ContextCompat.getDrawable(mActivity, R.drawable.ic_font_black_24dp),
                mBuilder.getFontSizeTitle());
        mView = view;
    }

    /**
     * Function to take the font size of pdf as user input
     */
    @Override
    public void enhance() {
        new MaterialDialog.Builder(mActivity)
                .title(mBuilder.getFontSizeTitle())
                .customView(R.layout.dialog_font_size, true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    final EditText fontInput = dialog.getCustomView().findViewById(R.id.fontInput);
                    final CheckBox cbSetDefault = dialog.getCustomView().findViewById(R.id.cbSetFontDefault);
                    try {
                        int check = Integer.parseInt(String.valueOf(fontInput.getText()));
                        if (check > 1000 || check < 0) {
                            StringUtils.getInstance().showSnackbar(mActivity, R.string.invalid_entry);
                        } else {
                            mBuilder.setFontSize(check);
                            showFontSize();
                            StringUtils.getInstance().showSnackbar(mActivity, R.string.font_size_changed);
                            if (cbSetDefault.isChecked()) {
                                mPreferences.setFontSize(mBuilder.getFontSize());
                                mBuilder.setFontSizeTitle(String.format(mActivity.getString(R.string.edit_font_size),
                                        mPreferences.getFontSize()));
                            }
                        }
                    } catch (NumberFormatException e) {
                        StringUtils.getInstance().showSnackbar(mActivity, R.string.invalid_entry);
                    }
                })
                .show();
    }

    @NonNull
    @Override
    public EnhancementOptionsEntity getEnhancementOptionsEntity() {
        return mEnhancementOptionsEntity;
    }

    /**
     * Displays font size in UI
     */
    private void showFontSize() {
        mEnhancementOptionsEntity
                .setName(String.format(mActivity.getString(R.string.font_size),
                        String.valueOf(mBuilder.getFontSize())));
        mView.updateView();
    }
}

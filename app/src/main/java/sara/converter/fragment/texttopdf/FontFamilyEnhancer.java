package sara.converter.fragment.texttopdf;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.itextpdf.text.Font;

import sara.converter.R;
import sara.converter.interfaces.Enhancer;
import sara.converter.model.EnhancementOptionsEntity;
import sara.converter.model.TextToPDFOptions;
import sara.converter.preferences.TextToPdfPreferences;

/**
 * An {@link Enhancer} that lets you select the font family.
 */
public class FontFamilyEnhancer implements Enhancer {

    private final Activity mActivity;
    private final TextToPdfPreferences mPreferences;
    private final TextToPDFOptions.Builder mBuilder;
    @NonNull private final EnhancementOptionsEntity mEnhancementOptionsEntity;
    private final TextToPdfContract.View mView;

    FontFamilyEnhancer(@NonNull final Activity activity,
                       @NonNull final TextToPdfContract.View view,
                       @NonNull final TextToPDFOptions.Builder builder) {
        mActivity = activity;
        mPreferences = new TextToPdfPreferences(activity);
        mBuilder = builder;
        mEnhancementOptionsEntity = new EnhancementOptionsEntity(
                mActivity, R.drawable.ic_font_family_24dp,
                String.format(mActivity.getString(R.string.default_font_family_text),
                        mBuilder.getFontFamily().name()));
        mView = view;
    }

    /**
     * Shows dialog to change font size
     */
    @Override
    public void enhance() {
        String fontFamily = mPreferences.getFontFamily();
        int ordinal = Font.FontFamily.valueOf(fontFamily).ordinal();
        MaterialDialog materialDialog = new MaterialDialog.Builder(mActivity)
                .title(String.format(mActivity.getString(R.string.default_font_family_text), fontFamily))
                .customView(R.layout.dialog_font_family, true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    RadioGroup radioGroup = view.findViewById(R.id.radio_group_font_family);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = view.findViewById(selectedId);
                    String fontFamily1 = radioButton.getText().toString();
                    mBuilder.setFontFamily(Font.FontFamily.valueOf(fontFamily1));
                    final CheckBox cbSetDefault = view.findViewById(R.id.cbSetDefault);
                    if (cbSetDefault.isChecked()) {
                        mPreferences.setFontFamily(fontFamily1);
                    }
                    showFontFamily();
                })
                .build();
        RadioGroup radioGroup = materialDialog.getCustomView().findViewById(R.id.radio_group_font_family);
        RadioButton rb = (RadioButton) radioGroup.getChildAt(ordinal);
        rb.setChecked(true);
        materialDialog.show();
    }

    @NonNull
    @Override
    public EnhancementOptionsEntity getEnhancementOptionsEntity() {
        return mEnhancementOptionsEntity;
    }

    /**
     * Displays font family in UI
     */
    private void showFontFamily() {
        mEnhancementOptionsEntity.setName(mActivity.getString(R.string.font_family_text)
                + mBuilder.getFontFamily().name());
        mView.updateView();
    }
}

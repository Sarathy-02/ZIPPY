package sara.converter.fragment.texttopdf;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import sara.converter.R;
import sara.converter.interfaces.Enhancer;
import sara.converter.model.EnhancementOptionsEntity;
import sara.converter.model.TextToPDFOptions;
import sara.converter.preferences.TextToPdfPreferences;
import sara.converter.util.ColorUtils;
import sara.converter.util.StringUtils;

/**
 * An {@link Enhancer} that lets you select the page color.
 */
public class PageColorEnhancer implements Enhancer {

    private final Activity mActivity;
    @NonNull private final EnhancementOptionsEntity mEnhancementOptionsEntity;
    private final TextToPdfPreferences mPreferences;
    private final TextToPDFOptions.Builder mBuilder;

    PageColorEnhancer(@NonNull final Activity activity,
                      @NonNull final TextToPDFOptions.Builder builder) {
        mActivity = activity;
        mPreferences = new TextToPdfPreferences(activity);
        mBuilder = builder;
        mEnhancementOptionsEntity = new EnhancementOptionsEntity(
                mActivity, R.drawable.ic_page_color, R.string.page_color);
    }

    @Override
    public void enhance() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(mActivity)
                .title(R.string.page_color)
                .customView(R.layout.dialog_color_chooser, true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    ColorPickerView colorPickerView = view.findViewById(R.id.color_picker);
                    CheckBox defaultCheckbox = view.findViewById(R.id.set_default);
                    final int pageColor = colorPickerView.getColor();
                    final int fontColor = mPreferences.getFontColor();
                    if (ColorUtils.getInstance().colorSimilarCheck(fontColor, pageColor)) {
                        StringUtils.getInstance().showSnackbar(mActivity, R.string.snackbar_color_too_close);
                    }
                    if (defaultCheckbox.isChecked()) {
                        mPreferences.setPageColor(pageColor);
                    }
                    mBuilder.setPageColor(pageColor);
                })
                .build();
        ColorPickerView colorPickerView = materialDialog.getCustomView().findViewById(R.id.color_picker);
        colorPickerView.setColor(mBuilder.getPageColor());
        materialDialog.show();
    }

    @NonNull
    @Override
    public EnhancementOptionsEntity getEnhancementOptionsEntity() {
        return mEnhancementOptionsEntity;
    }
}

package sara.converter.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.itextpdf.text.Rectangle;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.List;

import sara.converter.R;
import sara.converter.preferences.TextToPdfPreferences;

public class PageSizeUtils {

    public static final String PAGE_SIZE_FIT_SIZE = "FIT_SIZE";

    public static String mPageSize;
    private final Context mActivity;
    private final String mDefaultPageSize;
    private final HashMap<Integer, Integer> mPageSizeToString;
    private final TextToPdfPreferences mPreferences;

    /**
     * Utils object to modify the page size
     *
     * @param mActivity - current context
     */
    public PageSizeUtils(Context mActivity) {
        this.mActivity = mActivity;
        mPreferences = new TextToPdfPreferences(mActivity);
        mDefaultPageSize = mPreferences.getPageSize();
        mPageSize = mPreferences.getPageSize();
        mPageSizeToString = new HashMap<>();
        mPageSizeToString.put(R.id.page_size_default, R.string.a4);
        mPageSizeToString.put(R.id.page_size_fit_size, R.string.fit_size);
        mPageSizeToString.put(R.id.page_size_legal, R.string.legal);
        mPageSizeToString.put(R.id.page_size_executive, R.string.executive);
        mPageSizeToString.put(R.id.page_size_ledger, R.string.ledger);
        mPageSizeToString.put(R.id.page_size_tabloid, R.string.tabloid);
        mPageSizeToString.put(R.id.page_size_letter, R.string.letter);
    }

    /**
     * @param selectionId   - id of selected radio button
     * @param spinnerAValue - Value of A0 to A10 spinner
     * @param spinnerBValue - Value of B0 to B10 spinner
     * @return - Rectangle page size
     */
    private String getPageSize(int selectionId, String spinnerAValue, String spinnerBValue) {
        String stringPageSize;
        switch (selectionId) {
            case (R.id.page_size_a0_a10) -> {
                stringPageSize = spinnerAValue;
                mPageSize = stringPageSize.substring(0, stringPageSize.indexOf(" "));
            }
            case (R.id.page_size_b0_b10) -> {
                stringPageSize = spinnerBValue;
                mPageSize = stringPageSize.substring(0, stringPageSize.indexOf(" "));
            }
            case (R.id.page_size_fit_size) -> mPageSize = PAGE_SIZE_FIT_SIZE;
            default -> mPageSize = mActivity.getString(mPageSizeToString.get(selectionId));
        }
        return mPageSize;
    }

    /**
     * Show a dialog to modify the page size
     *
     * @param saveValue - save the value in shared preferences
     * @return - dialog object
     */
    public MaterialDialog showPageSizeDialog(boolean saveValue) {
        MaterialDialog materialDialog = getPageSizeDialog(saveValue);

        View view = materialDialog.getCustomView();
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_page_size);
        Spinner spinnerA = view.findViewById(R.id.spinner_page_size_a0_a10);
        Spinner spinnerB = view.findViewById(R.id.spinner_page_size_b0_b10);
        RadioButton radioButtonDefault = view.findViewById(R.id.page_size_default);
        radioButtonDefault.setText(String.format(mActivity.getString(R.string.default_page_size), mDefaultPageSize));

        if (saveValue)
            view.findViewById(R.id.set_as_default).setVisibility(View.GONE);

        if (mPageSize.equals(mDefaultPageSize)) {
            radioGroup.check(R.id.page_size_default);
        } else if (mPageSize.startsWith("A")) {
            radioGroup.check(R.id.page_size_a0_a10);
            spinnerA.setSelection(java.lang.Integer.parseInt(mPageSize.substring(1)));
        } else if (mPageSize.startsWith("B")) {
            radioGroup.check(R.id.page_size_b0_b10);
            spinnerB.setSelection(java.lang.Integer.parseInt(mPageSize.substring(1)));
        } else {
            Integer key = getKey(mPageSizeToString, mPageSize);
            if (key != null)
                radioGroup.check(key);
        }
        materialDialog.show();
        return materialDialog;
    }

    /**
     * Calculates common page size for given images to fit all of them.
     * - If images have different sizes than max width, max height is used to fit all of them.
     * - If images have same size, than output returns exact size of image.
     *
     * @param imagesUri - input images URIs as Strings.
     * @return common size of input images as Rectangle.
     * @see com.itextpdf.text.Rectangle
     */
    @NonNull
    @Contract("_ -> new")
    public static Rectangle calculateCommonPageSize(@NonNull List<String> imagesUri) {
        float maxWidth = 0; float maxHeight = 0;
        for (String imageUri : imagesUri) {
            Rectangle imageSize = ImageUtils.getImageSize(imageUri);
            float imageWidth = imageSize.getWidth(); float imageHeight = imageSize.getHeight();
            if (imageWidth > maxWidth) maxWidth = imageWidth;
            if (imageHeight > maxHeight) maxHeight = imageHeight;
        }
        return new Rectangle(maxWidth, maxHeight);
    }

    /**
     * Private show page size utils dialog
     *
     * @param saveValue - save the value in shared prefs
     * @return - dialog object
     */
    private MaterialDialog getPageSizeDialog(boolean saveValue) {
        MaterialDialog.Builder builder = DialogUtils.getInstance()
                .createCustomDialogWithoutContent((Activity) mActivity, R.string.set_page_size_text);
        return builder.customView(R.layout.set_page_size_dialog, true)
                .onPositive((dialog1, which) -> {
                    View view = dialog1.getCustomView();
                    RadioGroup radioGroup = view.findViewById(R.id.radio_group_page_size);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    Spinner spinnerA = view.findViewById(R.id.spinner_page_size_a0_a10);
                    Spinner spinnerB = view.findViewById(R.id.spinner_page_size_b0_b10);
                    mPageSize = getPageSize(selectedId, spinnerA.getSelectedItem().toString(),
                            spinnerB.getSelectedItem().toString());
                    CheckBox mSetAsDefault = view.findViewById(R.id.set_as_default);
                    if (saveValue || mSetAsDefault.isChecked()) {
                        mPreferences.setPageSize(mPageSize);
                    }
                }).build();
    }

    /**
     * Get key from the value
     *
     * @param map   - hash map
     * @param value - the value for which we want the key
     * @return - key value
     */
    @Nullable
    private Integer getKey(@NonNull HashMap<Integer, Integer> map, String value) {
        for (HashMap.Entry<Integer, Integer> entry : map.entrySet()) {
            if (value.equals(mActivity.getString(entry.getValue()))) {
                return entry.getKey();
            }
        }
        return null;
    }
}
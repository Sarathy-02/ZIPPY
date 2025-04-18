package sara.converter.fragment.texttopdf;

import android.content.Context;

import androidx.annotation.NonNull;

import sara.converter.R;
import sara.converter.interfaces.Enhancer;
import sara.converter.model.EnhancementOptionsEntity;
import sara.converter.preferences.TextToPdfPreferences;
import sara.converter.util.PageSizeUtils;

/**
 * An {@link Enhancer} that lets you select page size.
 */
public class PageSizeEnhancer implements Enhancer {

    private final PageSizeUtils mPageSizeUtils;
    @NonNull private final EnhancementOptionsEntity mEnhancementOptionsEntity;

    PageSizeEnhancer(@NonNull final Context context) {
        mPageSizeUtils = new PageSizeUtils(context);
        mEnhancementOptionsEntity = new EnhancementOptionsEntity(
                context, R.drawable.ic_page_size_24dp, R.string.set_page_size_text);

        PageSizeUtils.mPageSize = new TextToPdfPreferences(context).getPageSize();
    }

    @Override
    public void enhance() {
        mPageSizeUtils.showPageSizeDialog(false);
    }

    @NonNull
    @Override
    public EnhancementOptionsEntity getEnhancementOptionsEntity() {
        return mEnhancementOptionsEntity;
    }

}

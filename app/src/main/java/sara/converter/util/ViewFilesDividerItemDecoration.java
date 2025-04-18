package sara.converter.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import sara.converter.R;

/**
 * @author David Wu (david10608@gmail.com)
 * Created on 29.10.17.
 * Item decoration which draws a simple dividing line between elements
 */

public class ViewFilesDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;

    public ViewFilesDividerItemDecoration(@NonNull Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.files_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }
}

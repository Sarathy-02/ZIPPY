package sara.converter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sara.converter.R;
import sara.converter.interfaces.OnItemClickListener;
import sara.converter.model.BrushItem;

public class BrushItemAdapter extends RecyclerView.Adapter<BrushItemAdapter.BrushItemViewHolder> {

    private final Context mContext;
    private final OnItemClickListener mOnItemClickListener;
    private final List<BrushItem> mBrushItems;

    public BrushItemAdapter(Context context,
                            OnItemClickListener onItemClickListener,
                            List<BrushItem> brushItems) {
        mBrushItems = brushItems;
        mOnItemClickListener = onItemClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public BrushItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brush_color_item, parent, false);
        return new BrushItemAdapter.BrushItemViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull BrushItemViewHolder holder, int position) {
        int color = mBrushItems.get(position).getColor();
        if (position == mBrushItems.size() - 1)
            holder.doodleButton.setBackground(ContextCompat.getDrawable(mContext, color));
        else
            holder.doodleButton.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }

    @Override
    public int getItemCount() {
        return mBrushItems.size();
    }

    public class BrushItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView((R.id.doodle_color))
        Button doodleButton;

        BrushItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

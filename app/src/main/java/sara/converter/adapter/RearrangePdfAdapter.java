package sara.converter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sara.converter.R;

public class RearrangePdfAdapter extends RecyclerView.Adapter<RearrangePdfAdapter.ViewHolder> {
    /** @noinspection FieldCanBeLocal, unused */
    private final Context mContext;
    private final OnClickListener mOnClickListener;
    private ArrayList<Bitmap> mBitmaps;

    public RearrangePdfAdapter(OnClickListener onClickListener,
                               ArrayList<Bitmap> uris, Context context) {
        mOnClickListener = onClickListener;
        mBitmaps = uris;
        mContext = context;
    }

    @NonNull
    @Override
    public RearrangePdfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rearrange_images, parent, false);
        return new RearrangePdfAdapter.ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull RearrangePdfAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            holder.buttonUp.setVisibility(View.GONE);
        } else {
            holder.buttonUp.setVisibility(View.VISIBLE);
        }
        if (position == getItemCount() - 1) {
            holder.buttonDown.setVisibility(View.GONE);
        } else {
            holder.buttonDown.setVisibility(View.VISIBLE);
        }
        holder.imageView.setImageBitmap(mBitmaps.get(position));
        holder.pageNumber.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return mBitmaps.size();
    }

    public void positionChanged(ArrayList<Bitmap> images) {
        mBitmaps = images;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onUpClick(int position);

        void onDownClick(int position);

        void onRemoveClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView((R.id.image))
        ImageView imageView;
        @BindView((R.id.buttonUp))
        ImageButton buttonUp;
        @BindView((R.id.buttonDown))
        ImageButton buttonDown;
        @BindView((R.id.pageNumber))
        TextView pageNumber;
        @BindView((R.id.removeImage))
        ImageButton mRemoveImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            buttonDown.setOnClickListener(this);
            buttonUp.setOnClickListener(this);
            mRemoveImage.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()) {
                case (R.id.buttonUp) -> mOnClickListener.onUpClick(getAdapterPosition());
                case (R.id.buttonDown) -> mOnClickListener.onDownClick(getAdapterPosition());
                case (R.id.removeImage) -> mOnClickListener.onRemoveClick(getAdapterPosition());
            }
        }
    }
}
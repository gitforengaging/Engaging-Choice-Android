package com.aap.filmtime.home.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aap.filmtime.R;
import com.aap.filmtime.databinding.RowOfEngagingchoiceBinding;
import com.aap.filmtime.home.OnItemClickListener;
import com.aap.filmtime.model.DummyContentResp;
import com.bumptech.glide.Glide;

import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder> {
    private final Context mContext;
    private final List<DummyContentResp> mList;
    private OnItemClickListener mListener;

    public MostPopularAdapter(Context context, List<DummyContentResp> mDummyList) {
        this.mContext = context;
        this.mList = mDummyList;
    }

    @NonNull
    @Override
    public MostPopularAdapter.MostPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowOfEngagingchoiceBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_of_engagingchoice, parent, false);
        return new MostPopularViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularViewHolder holder, int position) {
        if (mList.get(position).isPoweredBy()) {
            holder.mBinding.rowOfEngagingEcView.setVisibilityOfPoweredBy(View.VISIBLE);
        } else {
            holder.mBinding.rowOfEngagingEcView.setVisibilityOfPoweredBy(View.GONE);
        }
        Glide.with(mContext).load(mList.get(position).getUrl()).into(holder.mBinding.rowOfEngagingEcView.mCoverIv);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MostPopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowOfEngagingchoiceBinding mBinding;

        public MostPopularViewHolder(RowOfEngagingchoiceBinding itemView) {
            super(itemView.getRoot());
            this.mBinding = itemView;
            mBinding.rowOfEngagingEcView.setOnClickListener(this);
            mBinding.rowOfEngagingEcView.setImageInFilmView(R.drawable.bitmap_copy);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getAdapterPosition());
            }
        }
    }
}

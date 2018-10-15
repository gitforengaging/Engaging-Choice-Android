package com.aap.filmtime.home.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aap.engagingchoice.pojo.EcContentResponse;
import com.aap.filmtime.R;
import com.aap.filmtime.databinding.RowOfEngagingchoiceBinding;
import com.aap.filmtime.home.OnItemClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Lenovo on 19-09-2018.
 */

public class EngagingChoiceAdapter extends RecyclerView.Adapter<EngagingChoiceAdapter.EngagingViewHolder> {
    private final List<EcContentResponse.DataBean> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public EngagingChoiceAdapter(Context context, List<EcContentResponse.DataBean> mDummyList) {
        this.mContext = context;
        this.mList = mDummyList;
    }

    @NonNull
    @Override
    public EngagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowOfEngagingchoiceBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_of_engagingchoice, parent, false);
        return new EngagingViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EngagingViewHolder holder, int position) {
        if (!TextUtils.isEmpty(mList.get(position).getCover_image())) {
            Glide.with(mContext).load(mList.get(position).getCover_image()).into(holder.mBinding.rowOfEngagingEcView.mCoverIv);
        } else {
            holder.mBinding.rowOfEngagingEcView.setBackgroundResource(R.color.black);
        }
        holder.mBinding.rowOfEngagingEcView.setVisibilityOfPoweredBy(View.VISIBLE);
    }

    public void setListener(OnItemClickListener clickListener) {
        this.mListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class EngagingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowOfEngagingchoiceBinding mBinding;

        public EngagingViewHolder(RowOfEngagingchoiceBinding itemView) {
            super(itemView.getRoot());
            this.mBinding = itemView;
            mBinding.rowOfEngagingEcView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onEngagingChoiceClick(v, getAdapterPosition());
            }
        }
    }
}

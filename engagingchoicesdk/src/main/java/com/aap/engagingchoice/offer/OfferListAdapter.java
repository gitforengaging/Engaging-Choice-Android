package com.aap.engagingchoice.offer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aap.engagingchoice.R;
import com.aap.engagingchoice.databinding.RowOfOfferListBinding;
import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.DateUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This class is used as adapter for OfferList Data
 */
public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.OfferListViewHolder> {
    private  int Width;
    private final Context mContext;
    private final List<EcOfferListResponse.DataBean> mList;
    private int Height;
    private OnItemClickListener mListener;
    private EcOfferListResponse mOfferResp;

    public OfferListAdapter(Context context, List<EcOfferListResponse.DataBean> mOfferList) {
        this.mContext = context;
        this.mList = mOfferList;
    }

    public void setHeightWidth(int height,int width){
        this.Height = height;
        this.Width = width;
    }

    public void setOfferResponse(EcOfferListResponse mEcOfferResponse) {
        this.mOfferResp = mEcOfferResponse;
    }

    @NonNull
    @Override
    public OfferListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowOfOfferListBinding rowOfOfferListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_of_offer_list, parent, false);
        return new OfferListViewHolder(rowOfOfferListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferListViewHolder holder, int position) {
        holder.mBinding.rowTvOffername.setText(mList.get(position).getOffer_title());
        holder.mBinding.rowTvDate.setText(mContext.getString(R.string.valid_till) + " " + DateUtils.getDate(mList.get(position).getOffer_end_date()));
        int discountType = mList.get(position).getDiscount_type();
        if (discountType == 1) {
            holder.mBinding.rowTvOfferOff.setText(mList.get(position).getDiscount());
            holder.mBinding.rowTvOfferOff.setVisibility(View.VISIBLE);
            holder.mBinding.rowOfOfferListLlContOfferOff.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBinding.rowTvOffername.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.LEFT_OF, R.id.row_tv_offer_off);
            holder.mBinding.rowTvOffername.setLayoutParams(params);
        } else {
            holder.mBinding.rowOfOfferTvNumeric.setText(mList.get(position).getDiscount() + " ");
            holder.mBinding.rowTvOfferOff.setVisibility(View.GONE);
            holder.mBinding.rowOfOfferListLlContOfferOff.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBinding.rowTvOffername.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.LEFT_OF, R.id.row_of_offer_list_ll_cont_offer_off);
            holder.mBinding.rowTvOffername.setLayoutParams(params);
        }

        int fileType = mList.get(position).getFile_type();
        if (fileType == 1) {
            String offerUrl = mOfferResp.getPagination().getFile_url();
            String fileName = mList.get(position).getFile_name();
            String thumbNail = offerUrl + fileName;
            Glide.with(mContext).load(thumbNail).into(holder.mBinding.rowIvOffer);
        } else {
            Glide.with(mContext).load(mList.get(position).getCover_image()).into(holder.mBinding.rowIvOffer);
        }


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class OfferListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowOfOfferListBinding mBinding;

        public OfferListViewHolder(RowOfOfferListBinding itemView) {
            super(itemView.getRoot());
            this.mBinding = itemView;
            mBinding.rowIvView.setOnClickListener(this);
            mBinding.rowFrMainCont.setOnClickListener(this);
            ViewGroup.LayoutParams params = mBinding.rowFrMainCont.getLayoutParams();
            params.height = Height / 3;
            params.width = Width;
            mBinding.rowFrMainCont.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}

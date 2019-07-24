package com.common.esimrfid.ui.invdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

public class InvDetailAdapter extends RecyclerView.Adapter<InvDetailAdapter.ViewHolder> {

    private List<InvDetail> invdtails;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public InvDetailAdapter(List<InvDetail> invdtails, Context mContext) {
        this.invdtails = invdtails;
        this.mContext = mContext;

    }

    public void setRefreshDataList(List<InvDetail> list) {
        invdtails = list;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_invdetail,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        InvDetail invDetail = invdtails.get(i);
        viewHolder.mAccountName.setText(invDetail.getAccName());
        viewHolder.mCompanyAccount.setText(invDetail.getCorpAccount());
        viewHolder.mCompanyName.setText(invDetail.getCorpName());
        viewHolder.mLatestCard.setText(invDetail.getCardCode());
        viewHolder.mBranchName.setText(invDetail.getBankName());
        viewHolder.mEntryDate.setText(DateUtils.date2String(invDetail.getCardRecordDate()));
        viewHolder.mCoountStatus.setText(invDetail.getIvnStatusString());
        viewHolder.mItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemLongClick(i);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return invdtails == null ? 0 :invdtails.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         TextView mAccountName;
         TextView mCompanyAccount;
         TextView mCompanyName;
         TextView mLatestCard;
         TextView mBranchName;
         TextView mEntryDate;
         TextView mCoountStatus;
         LinearLayout mItemLayout;
         ViewHolder(View view){
            super(view);
            mAccountName =  view.findViewById(R.id.account_name);
            mCompanyAccount =  view.findViewById(R.id.company_account);
            mCompanyName =  view.findViewById(R.id.company_name);
            mLatestCard =  view.findViewById(R.id.latest_card);
            mBranchName =  view.findViewById(R.id.branch_name);
            mEntryDate =  view.findViewById(R.id.entry_date);
            mCoountStatus = view.findViewById(R.id.count_status);
            mItemLayout = view.findViewById(R.id.invdetail_item);
        }
    }

    public interface OnItemClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

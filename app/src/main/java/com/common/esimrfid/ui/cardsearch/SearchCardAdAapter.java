package com.common.esimrfid.ui.cardsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

public class SearchCardAdAapter extends RecyclerView.Adapter<SearchCardAdAapter.ViewHolder> {
    private List<SignatureCard> mSignatureCard;
    private Context mContext;

    public SearchCardAdAapter(List<SignatureCard> signatureCard, Context context) {
        this.mSignatureCard = signatureCard;
        this.mContext = context;
    }

    public void setRefreshDataList(List<SignatureCard> signatureCard) {
        mSignatureCard = signatureCard;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchCardAdAapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_search_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SignatureCard signatureCard = mSignatureCard.get(i);
        viewHolder.mAccountName.setText(signatureCard.getAccName());
        viewHolder.mCardInfo.setText(signatureCard.getCardCode());
        viewHolder.mOpenBranch.setText(signatureCard.getBankName());
        viewHolder.mOpenDate.setText(DateUtils.date2String(signatureCard.getCardCreateDate()));
        viewHolder.mEntryDate.setText(DateUtils.date2String(signatureCard.getCardRecordDate()));
    }

    @Override
    public int getItemCount() {
        return mSignatureCard == null ? 0 : mSignatureCard.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAccountName;
        TextView mCardInfo;
        TextView mOpenBranch;
        TextView mOpenDate;
        TextView mEntryDate;
        ViewHolder(View view){
            super(view);
            mAccountName =  view.findViewById(R.id.tv_account_name);
            mCardInfo =  view.findViewById(R.id.iv_card_info);
            mOpenBranch =  view.findViewById(R.id.tv_open_branch);
            mOpenDate =  view.findViewById(R.id.tv_open_date);
            mEntryDate =  view.findViewById(R.id.tv_entry_date);
        }
    }
}

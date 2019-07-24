package com.common.esimrfid.ui.carddeail;

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

public class CardDetailAdapter extends RecyclerView.Adapter<CardDetailAdapter.ViewHolder> {

    private List<SignatureCard> invdtails;
    private Context mContext;

    public CardDetailAdapter(List<SignatureCard> invdtails, Context mContext) {
        this.invdtails = invdtails;
        this.mContext = mContext;

    }

    public void setRefreshDataList(List<SignatureCard> list) {
        invdtails = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_carddetail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        SignatureCard signatureCard = invdtails.get(i);
        viewHolder.mAccountName.setText(signatureCard.getAccName());
        viewHolder.mCardMessage.setText(signatureCard.getCardCode());
        viewHolder.mOpenBranch.setText(signatureCard.getBankName());
        viewHolder.mOpenDate.setText(DateUtils.date2String(signatureCard.getCardCreateDate()));
        viewHolder.mEntryDate.setText(DateUtils.date2String(signatureCard.getCardRecordDate()));
    }


    @Override
    public int getItemCount() {
        return invdtails == null ? 0 : invdtails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAccountName;
        TextView mCardMessage;
        TextView mOpenBranch;
        TextView mOpenDate;
        TextView mEntryDate;

        ViewHolder(View view) {
            super(view);
            mAccountName = view.findViewById(R.id.account_name);
            mCardMessage = view.findViewById(R.id.card_message);
            mOpenBranch = view.findViewById(R.id.open_branch);
            mOpenDate = view.findViewById(R.id.open_date);
            mEntryDate = view.findViewById(R.id.entry_date);
        }
    }

}

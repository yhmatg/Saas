package com.common.esimrfid.ui.invorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

public class InvOrderAdapter extends RecyclerView.Adapter <InvOrderAdapter.ViewHolder>{
    private List<InvOrder> invdtails;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public InvOrderAdapter(List<InvOrder> invdtails, Context mContext) {
        this.invdtails = invdtails;
        this.mContext = mContext;
    }

    public void setRefreshDataList(List<InvOrder> list) {
        invdtails = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_invorder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        InvOrder invOrder = invdtails.get(i);
        viewHolder.mOrderCheck.setText(invOrder.getInvCode());
        viewHolder.mDocState.setText(invOrder.getInvStatusString());
        viewHolder.mDescribe.setText(invOrder.getInvRemark());
        viewHolder.mCreateTime.setText(DateUtils.date2String(invOrder.getCreateDate()));
        viewHolder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(i);
                }
            }
        });
        viewHolder.mItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemLongClick(i);
                }
                return true;
            }
        });

        viewHolder.mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onRightImgClick(i);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return invdtails == null ? 0 :invdtails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mOrderCheck;
        TextView mDocState;
        TextView mDescribe;
        TextView mCreateTime;
        LinearLayout mItemLayout;
        ImageView mRightImg;
        ViewHolder(View view){
            super(view);
            mOrderCheck =  view.findViewById(R.id.tv_check_order);
            mDocState =  view.findViewById(R.id.tv_docu_status);
            mDescribe =  view.findViewById(R.id.tv_describe);
            mCreateTime =  view.findViewById(R.id.tv_create_time);
            mItemLayout = view.findViewById(R.id.item_layout);
            mRightImg = view.findViewById(R.id.imgTitleRight);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
        void onRightImgClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

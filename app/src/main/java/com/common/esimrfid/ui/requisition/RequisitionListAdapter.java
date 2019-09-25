package com.common.esimrfid.ui.requisition;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionItemInfo;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequisitionListAdapter extends RecyclerView.Adapter<RequisitionListAdapter.ViewHolder> {

    private Context mContext;
    private List<RequisitionItemInfo> mData;
    private static final String REQUISITION_ID = "req_id";
    private static final String REQUISITION_USER_NAME = "req_user_name";
    private static final String REQUISITION_ORD_STATUS = "req_ord_status";

    public RequisitionListAdapter(Context mContext, List<RequisitionItemInfo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_requisition, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequisitionItemInfo requisitionItemInfo = mData.get(i);
        viewHolder.tvResName.setText("领用单号："  + requisitionItemInfo.getOdr_code());
        String userName = requisitionItemInfo.getReqUser() == null ? "无信息" : requisitionItemInfo.getReqUser().getUser_real_name();
        viewHolder.tvResUser.setText("领用人："  +  userName);
        String createName = requisitionItemInfo.getCreator() == null ? "无信息" : requisitionItemInfo.getCreator().getUser_real_name();
        viewHolder.tvResCreateor.setText("创建人："  + createName);
        viewHolder.tvCreateTime.setText("创建时间："  + DateUtils.date2String(requisitionItemInfo.getOdr_date()));
        if("已完成".equals(requisitionItemInfo.getOdr_status())){
            viewHolder.tvStatus.setText("已完成");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            viewHolder.tvStatus.setText("处理中");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.un_checked));
        }
        viewHolder.resLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(REQUISITION_ID,requisitionItemInfo.getId());
                intent.putExtra(REQUISITION_USER_NAME,userName);
                intent.putExtra(REQUISITION_ORD_STATUS,requisitionItemInfo.getOdr_status());
                intent.setClass(mContext,RequisitionDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_res_name)
        TextView tvResName;
        @BindView(R.id.tv_res_user)
        TextView tvResUser;
        @BindView(R.id.tv_res_creator)
        TextView tvResCreateor;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.res_layout)
        LinearLayout resLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public void setRefreshDataList(List<RequisitionItemInfo> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }
}


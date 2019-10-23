package com.common.esimrfid.ui.invorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.emun.InvOperateStatus;
import com.common.esimrfid.core.bean.emun.OrderStatusEm;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.ui.invdetail.InvdetailActivity;
import com.common.esimrfid.utils.DateUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

public class InvsAdapter extends RecyclerView.Adapter<InvsAdapter.ViewHolder> {
    public static final String INV_ID = "inv_id";
    public static final String INV_STATUS = "inv_status";

    public static final String STATUS_DOING = "STATUS_DOING";
    public static final String STATUS_FINISH = "STATUS_FINISH";
    public static final String STATUS_INIT = "STATUS_INIT";

    private static final String TAG = "InvsAdapter";
    private List<ResultInventoryOrder> inventoryOrders;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public InvsAdapter(List<ResultInventoryOrder> inventoryOrders, Context mContext) {
        this.inventoryOrders = inventoryOrders;
        this.mContext = mContext;
    }

    public void setRefreshDataList(List<ResultInventoryOrder> list) {
        inventoryOrders = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.assest_list_item, viewGroup, false);
        convertView.setBackgroundColor(Color.WHITE);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDetailsPage(inventoryOrders.get(i));
            }
        });
        ResultInventoryOrder nventoryOrder = inventoryOrders.get(i);
        int status = nventoryOrder.getInv_status().getIndex();
        if (status == OrderStatusEm.INIT.getIndex()) {
            viewHolder.tvStatus.setText(OrderStatusEm.INIT.getName());
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.un_checked));
        } else if (status == OrderStatusEm.FINISH.getIndex()) {
            viewHolder.tvStatus.setText(OrderStatusEm.FINISH.getName());
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        } else if (status == OrderStatusEm.PROCESSING.getIndex()) {
            viewHolder.tvStatus.setText(OrderStatusEm.PROCESSING.getName());
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.processing));
        }
        // 指定单号
        viewHolder.tvOrderNum.setText("单号：" + nventoryOrder.getInv_code());
        if (nventoryOrder.getInv_status().getIndex() == 0) {
            // 未完成
            viewHolder.tvFinishDate.setVisibility(View.GONE);
        } else {
            // 已完成
            viewHolder.tvCredDate.setVisibility(View.VISIBLE);
            viewHolder.tvFinishDate.setText("结束时间：");//+ DateUtils.stampToDate(bean.getOd_finishdate())
        }
        //modify null 20190813 start
        String creator = "";
        String userRealName = nventoryOrder.getCreator() == null ? "未知" : nventoryOrder.getCreator().getUser_real_name();
        String userName = nventoryOrder.getCreator() == null ? "未知" : nventoryOrder.getCreator().getUser_name();
        if (!"未知".equals(userRealName)) {
            creator = userRealName;
        } else if (!"未知".equals(userName)) {
            creator = userName;
        } else {
            creator = "未知";
        }
        //modify null 20190813 end
        viewHolder.tvCreator.setText("创建人：" + creator);
        //add assigner name start 20192022
        String assigner = "";
        String assRealName = nventoryOrder.getAssigner() == null ? "未知" : nventoryOrder.getAssigner().getUser_real_name();
        String assName = nventoryOrder.getAssigner() == null ? "未知" : nventoryOrder.getAssigner().getUser_name();
        if (!"未知".equals(assRealName)) {
            assigner = assRealName;
        } else if (!"未知".equals(assName)) {
            assigner = assName;
        } else {
            assigner = "未知";
        }
        viewHolder.tvAssigner.setText("盘点人：" +  assigner);
        //add assigner name end 20192022

        viewHolder.tvCredDate.setText("创建时间：" + DateUtils.date2String(nventoryOrder.getCreate_date()));
        int tempStatus = nventoryOrder.getInv_status().getIndex();
        if (tempStatus == 11) {
            viewHolder.tvDeadLine.setText("完成时间：" + DateUtils.date2String(nventoryOrder.getUpdate_date()));
        } else if (tempStatus == 0 || tempStatus == 10) {
            viewHolder.tvDeadLine.setText("预计完成：" + DateUtils.date2String(nventoryOrder.getInv_exptfinish_date()));
        }

        if (nventoryOrder.getOpt_status() != null && nventoryOrder.getOpt_status() == InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex()) {
            viewHolder.optImg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.optImg.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(nventoryOrder.getInv_remark())) {
            viewHolder.tvRemark.setVisibility(View.VISIBLE);
            viewHolder.tvRemark.setText("备注：" + nventoryOrder.getInv_remark());
        } else {
            viewHolder.tvRemark.setVisibility(View.GONE);
        }
        viewHolder.tvRemark.setText("备注：" + nventoryOrder.getInv_remark());
        if (nventoryOrder.getFinish_remark() != null) {
            viewHolder.tvFinishRemark.setVisibility(View.VISIBLE);
            viewHolder.tvFinishRemark.setText("结束备注：" + nventoryOrder.getFinish_remark());
        } else {
            viewHolder.tvFinishRemark.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return inventoryOrders == null ? 0 : inventoryOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView typeImg;
        TextView tvCreator;
        TextView tvStatus;
        TextView tvCredDate;
        TextView tvFinishDate;
        TextView tvFinishRemark;
        TextView tvOrderNum;
        TextView tvOrderStatus;
        TextView tvDeadLine;
        ImageView optImg;
        TextView tvRemark;
        LinearLayout itemLayout;
        TextView tvAssigner;

        ViewHolder(View view) {
            super(view);
            typeImg = view.findViewById(R.id.typeImg);
            tvCreator = view.findViewById(R.id.tv_creator);
            tvStatus = view.findViewById(R.id.tv_status);
            tvCredDate = view.findViewById(R.id.tv_cred_date);
            tvFinishDate = view.findViewById(R.id.tv_finish_date);
            tvFinishRemark = view.findViewById(R.id.tv_finish_remark);
            tvOrderNum = view.findViewById(R.id.tv_order_num);
            tvOrderStatus = view.findViewById(R.id.tv_order_status);
            tvDeadLine = view.findViewById(R.id.tv_deadline);
            optImg = view.findViewById(R.id.opt_status);
            tvRemark = view.findViewById(R.id.tv_remark);
            itemLayout = view.findViewById(R.id.inv_recycle_item);
            tvAssigner = view.findViewById(R.id.tv_assigner);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ResultInventoryOrder invOder);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void switchDetailsPage(ResultInventoryOrder order) {
        long l = System.currentTimeMillis();
        Logger.e("======== BEGAIN START =======> " + (l));

        Intent intent = new Intent();

        intent.putExtra(INV_ID, order.getId());
        intent.putExtra(InvdetailActivity.INV_TOTAL_COUNT, order.getInv_total_count());
        intent.putExtra(InvdetailActivity.INV_FININSHED_COUNT, order.getInv_finish_count());

        if (order.getInv_status().getIndex() == 0) {//"初始创建"
            intent.putExtra(INV_STATUS, STATUS_INIT);
        } else if (order.getInv_status().getIndex() == OrderStatusEm.FINISH.getIndex()) {//完成
            intent.putExtra(INV_STATUS, STATUS_FINISH);
        }
        intent.setClass(mContext, InvdetailActivity.class);

        mContext.startActivity(intent);
        Logger.e("======== BEGIN SWITCH =======> " + (System.currentTimeMillis() - l));

    }
}

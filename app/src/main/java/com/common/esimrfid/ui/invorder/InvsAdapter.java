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
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.ui.invdetail.InvdetailActivity;
import com.common.esimrfid.utils.DateUtils;

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
        int status = nventoryOrder.getInv_status();
        if (status == 10) {
            viewHolder.tvStatus.setText("处理中");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.un_checked));
        } else if (status == 11) {
            viewHolder.tvStatus.setText("已完成");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        // 指定单号
        viewHolder.tvOrderNum.setText("单号：" + nventoryOrder.getInv_code());
        if (nventoryOrder.getInv_status() == 10) {
            // 未完成
            viewHolder.tvFinishDate.setVisibility(View.GONE);
        } else if (nventoryOrder.getInv_status() == 11) {
            // 已完成
            viewHolder.tvFinishDate.setVisibility(View.VISIBLE);
            String finishTiem = nventoryOrder.getUpdate_date() == null ? "无信息" : DateUtils.date2String(nventoryOrder.getUpdate_date());
            viewHolder.tvFinishDate.setText("完成时间：" + finishTiem);
        }
        String userRealName = nventoryOrder.getCreator() == null ? "无信息" : nventoryOrder.getCreator().getUser_real_name();
        userRealName = TextUtils.isEmpty(userRealName) ? "无信息" : userRealName;
        viewHolder.tvCreator.setText("创建人：" + userRealName);

        String assRealName = nventoryOrder.getAssigner() == null ? "无信息" : nventoryOrder.getAssigner().getUser_real_name();
        assRealName = TextUtils.isEmpty(assRealName) ? "无信息" : assRealName;
        viewHolder.tvAssigner.setText("盘点人：" + assRealName);

        viewHolder.tvCredDate.setText("创建时间：" + DateUtils.date2String(nventoryOrder.getCreate_date()));
        viewHolder.tvDeadLine.setText("预计完成：" + DateUtils.date2String(nventoryOrder.getInv_exptfinish_date()));

        String invName = TextUtils.isEmpty(nventoryOrder.getInv_name()) ? "无信息" : nventoryOrder.getInv_name();
        viewHolder.tvOrderName.setText("单名：" + invName);

        if (!TextUtils.isEmpty(nventoryOrder.getInv_remark())) {
            viewHolder.tvRemark.setVisibility(View.VISIBLE);
            viewHolder.tvRemark.setText("备注：" + nventoryOrder.getInv_remark());
        } else {
            viewHolder.tvRemark.setVisibility(View.GONE);
        }

        if (nventoryOrder.getInv_status() == 11 && !TextUtils.isEmpty(nventoryOrder.getInv_finish_remark())) {
            viewHolder.tvFinishRemark.setVisibility(View.VISIBLE);
            viewHolder.tvFinishRemark.setText("结束备注：" + nventoryOrder.getInv_finish_remark());
        } else {
            viewHolder.tvFinishRemark.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return inventoryOrders == null ? 0 : inventoryOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderName;
        TextView tvCreator;
        TextView tvStatus;
        TextView tvCredDate;
        TextView tvFinishDate;
        TextView tvFinishRemark;
        TextView tvOrderNum;
        TextView tvDeadLine;
        ImageView optImg;
        TextView tvRemark;
        LinearLayout itemLayout;
        TextView tvAssigner;

        ViewHolder(View view) {
            super(view);
            tvOrderName = view.findViewById(R.id.tv_order_name);
            tvCreator = view.findViewById(R.id.tv_creator);
            tvStatus = view.findViewById(R.id.tv_status);
            tvCredDate = view.findViewById(R.id.tv_cred_date);
            tvFinishDate = view.findViewById(R.id.tv_finish_date);
            tvFinishRemark = view.findViewById(R.id.tv_finish_remark);
            tvOrderNum = view.findViewById(R.id.tv_order_num);
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
        Intent intent = new Intent();
        intent.putExtra(INV_ID, order.getId());
        intent.putExtra(InvdetailActivity.INV_TOTAL_COUNT, order.getInv_total_count());
        intent.putExtra(InvdetailActivity.INV_FININSHED_COUNT, order.getInv_finish_count());
        if (order.getInv_status() == 10) {//"处理中"
            intent.putExtra(INV_STATUS, STATUS_INIT);
        } else if (order.getInv_status() == 11) {//完成
            intent.putExtra(INV_STATUS, STATUS_FINISH);
        }
        intent.setClass(mContext, InvdetailActivity.class);
        mContext.startActivity(intent);
    }
}

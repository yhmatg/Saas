package com.common.esimrfid.ui.distribute;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrder;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistributeOrderAdapter extends RecyclerView.Adapter<DistributeOrderAdapter.ViewHolder> {

    private Context mContext;
    private List<DistributeOrder> distributeOrders;
    private OnItemClickListener mOnItemClickListener;

    public DistributeOrderAdapter(Context mContext, List<DistributeOrder> distributeOrders) {
        this.mContext = mContext;
        this.distributeOrders = distributeOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dist_order_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DistributeOrder distributeOrder = distributeOrders.get(i);
        viewHolder.mDistTitle.setText(distributeOrder.getOdr_remark());
        viewHolder.mDistCode.setText(distributeOrder.getOdr_code());
        viewHolder.mDistCreatorName.setText(distributeOrder.getCreator_name());
        viewHolder.mDistCreatorDate.setText(DateUtils.date2String(distributeOrder.getCreate_date()));
        String odrStatus = distributeOrder.getOdr_status();
        String changeStatus= "待派发";
        boolean isEnable = true;
        Drawable drawable = mContext.getDrawable(R.drawable.bt_background);
        switch (odrStatus) {
            case "领用审批中":
                changeStatus= "待派发";
                isEnable = true;
                drawable = mContext.getDrawable(R.drawable.bt_background);
                break;
            case "领用已完成":
                changeStatus= "已完成";
                isEnable = false;
                drawable = mContext.getDrawable(R.drawable.bt_disable_background);
                break;
            case "领用已驳回":
                changeStatus= "已驳回";
                isEnable = false;
                drawable = mContext.getDrawable(R.drawable.bt_disable_background);
                break;
        }
        viewHolder.mOrderStatus.setText(changeStatus);
        viewHolder.mDistNumb.setText(String.valueOf(distributeOrder.getApply_detail_count()));
        viewHolder.mBtReject.setEnabled(isEnable);
        viewHolder.mBtReject.setBackground(drawable);
        viewHolder.mBtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRejectDist(distributeOrder);
                }
            }
        });
        viewHolder.mBtStartDist.setEnabled(isEnable);
        viewHolder.mBtStartDist.setBackground(drawable);
        viewHolder.mBtStartDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onStartDist(distributeOrder);
                }
            }
        });
        viewHolder.mOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(distributeOrder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return distributeOrders == null ? 0 : distributeOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_dist_order_item)
        RelativeLayout mOrderItem;
        @BindView(R.id.distribute_title)
        TextView mDistTitle;
        @BindView(R.id.distribute_code)
        TextView mDistCode;
        @BindView(R.id.dist_creator_name)
        TextView mDistCreatorName;
        @BindView(R.id.create_date)
        TextView mDistCreatorDate;
        @BindView(R.id.order_status)
        TextView mOrderStatus;
        @BindView(R.id.distribute_num)
        TextView mDistNumb;
        @BindView(R.id.bt_reject)
        Button mBtReject;
        @BindView(R.id.bt_start_dist)
        Button mBtStartDist;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onRejectDist(DistributeOrder distributeOrder);

        void onStartDist(DistributeOrder distributeOrder);

        void onItemClick(DistributeOrder distributeOrder);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}

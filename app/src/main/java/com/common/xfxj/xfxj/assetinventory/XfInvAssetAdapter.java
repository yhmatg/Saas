package com.common.xfxj.xfxj.assetinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.xfxj.R;
import com.common.xfxj.core.bean.nanhua.xfxj.XfResultInventoryOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XfInvAssetAdapter extends RecyclerView.Adapter<XfInvAssetAdapter.ViewHolder> {

    private static final String INV_ID = "inv_id";
    private static final String INV_NAME = "inv_name";
    private static final String INV_STATUS = "inv_status";
    private static final String INTENT_FROM = "intent_from";
    private List<XfResultInventoryOrder> mInvTaskorders;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    public XfInvAssetAdapter(List<XfResultInventoryOrder> invTaskorders, Context mContext) {
        this.mInvTaskorders = invTaskorders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.xf_invtask_item_layout, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        XfResultInventoryOrder invTaskItem = mInvTaskorders.get(i);
        String invName = TextUtils.isEmpty(invTaskItem.getInv_name()) ? "" : invTaskItem.getInv_name();
        viewHolder.mInvTitle.setText(invName);

        viewHolder.mInvCreatorName.setText(invTaskItem.getInv_person_name());

        viewHolder.mCreateDate.setText(invTaskItem.getXjDate());
        viewHolder.mExpFinishDate.setText(invTaskItem.getXjTime());
        Integer inv_total_count = invTaskItem.getInv_total_count();
        Integer inv_finish_count = invTaskItem.getInv_finish_count();
        viewHolder.mAllNum.setText(String.valueOf(inv_total_count));
        viewHolder.mFinishedNum.setText(String.valueOf(inv_total_count - inv_finish_count));
        viewHolder.mUnfinishedNum.setText(String.valueOf(inv_finish_count));
        if(invTaskItem.getInv_status() == 1){
            viewHolder.mStartInv.setVisibility(View.GONE);
            viewHolder.mSyncInv.setVisibility(View.GONE);
            viewHolder.mFinishInv.setVisibility(View.GONE);
            viewHolder.divideLine.setVisibility(View.GONE);
        }else {
            viewHolder.mStartInv.setVisibility(View.VISIBLE);
            viewHolder.mSyncInv.setVisibility(View.VISIBLE);
            viewHolder.mFinishInv.setVisibility(View.VISIBLE);
            viewHolder.divideLine.setVisibility(View.VISIBLE);
        }

        viewHolder.mStartInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNormalClick()) {
                    showInvDetailActivity(invTaskItem);
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onDetailInv(invTaskItem, i);
                    }

                }
            }
        });

        viewHolder.mSyncInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNormalClick()) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onSyncData(invTaskItem, i);
                    }
                }
            }
        });

        viewHolder.mFinishInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNormalClick()) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onFinishInv(invTaskItem, i);
                    }

                }
            }
        });

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNormalClick()) {
                    showInvDetailActivity(invTaskItem);
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onDetailInv(invTaskItem, i);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInvTaskorders == null ? 0 : mInvTaskorders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.invtask_title)
        TextView mInvTitle;
        @BindView(R.id.inv_creator_name)
        TextView mInvCreatorName;
        @BindView(R.id.create_date)
        TextView mCreateDate;
        @BindView(R.id.expect_finish_date)
        TextView mExpFinishDate;
        @BindView(R.id.inv_all_num)
        TextView mAllNum;
        @BindView(R.id.unfinished_num)
        TextView mUnfinishedNum;
        //盘点任务界面显示未待提交
        @BindView(R.id.inv_finished_num)
        TextView mFinishedNum;
        @BindView(R.id.bt_start_inv)
        Button mStartInv;
        @BindView(R.id.bt_sync_data)
        Button mSyncInv;
        @BindView(R.id.bt_finish_inv)
        Button mFinishInv;
        @BindView(R.id.divider_three)
        View divideLine;
        @BindView(R.id.rl_task_item)
        RelativeLayout item;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onSyncData(XfResultInventoryOrder invOder, int position);

        void onFinishInv(XfResultInventoryOrder invOder, int position);

        void onDetailInv(XfResultInventoryOrder invOder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void showInvDetailActivity(XfResultInventoryOrder invOrder) {
        Intent intent = new Intent();
        intent.putExtra(INV_ID, invOrder.getId());
        intent.putExtra(INV_NAME, invOrder.getInv_name());
        intent.putExtra(INV_STATUS, invOrder.getInv_status());
        intent.putExtra(INTENT_FROM, "InvAssetAdapter");
        intent.setClass(mContext, XfInvdetialActivity.class);
        mContext.startActivity(intent);
    }

    public boolean isNormalClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

}

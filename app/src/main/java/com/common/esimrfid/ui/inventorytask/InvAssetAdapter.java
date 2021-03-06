package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvAssetAdapter extends RecyclerView.Adapter<InvAssetAdapter.ViewHolder> {

    private static final String INV_ID = "inv_id";
    private static final String INV_NAME = "inv_name";
    private static final String INV_STATUS = "inv_status";
    private static final String INTENT_FROM = "intent_from";
    private List<ResultInventoryOrder> mInvTaskorders;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    public InvAssetAdapter(List<ResultInventoryOrder> invTaskorders, Context mContext) {
        this.mInvTaskorders = invTaskorders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.invtask_item_layout, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ResultInventoryOrder invTaskItem = mInvTaskorders.get(i);
        String invName = TextUtils.isEmpty(invTaskItem.getInv_name()) ? "" : invTaskItem.getInv_name();
        viewHolder.mInvTitle.setText(invName);

        String invCode = TextUtils.isEmpty(invTaskItem.getInv_code()) ? "" : invTaskItem.getInv_code();
        viewHolder.mInvCode.setText(invCode);

        String userRealName = invTaskItem.getInv_creator_name();
        userRealName = TextUtils.isEmpty(userRealName) ? "" : userRealName;
        viewHolder.mInvCreatorName.setText(userRealName);

        viewHolder.mCreateDate.setText(DateUtils.date2String(invTaskItem.getInv_exptbegin_date()));
        viewHolder.mExpFinishDate.setText(DateUtils.date2String(invTaskItem.getInv_exptfinish_date()));
      /*  Integer inv_total_count = invTaskItem.getInv_total_count();
        Integer inv_finish_count = invTaskItem.getInv_finish_count();
        Integer not_submit_count = invTaskItem.getInv_notsubmit_count() == null ? 0 : invTaskItem.getInv_notsubmit_count();*/
        Integer managerNum = invTaskItem.getInv_total_count();
        Integer staffNum = invTaskItem.getInv_emp_count() == null ? 0 : invTaskItem.getInv_emp_count();
        viewHolder.mAllNum.setText(String.valueOf(managerNum + staffNum));
        viewHolder.mUnfinishedNum.setText(String.valueOf(managerNum));
        viewHolder.mFinishedNum.setText(String.valueOf(staffNum));

        //viewHolder.mTvName.setText(R.string.not_submit);
        viewHolder.mStartInv.setVisibility(View.VISIBLE);
        viewHolder.mStartInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNormalClick()) {
                    showInvDetailActivity(invTaskItem);
                    mOnItemClickListener.onDetailInv(invTaskItem, i);
                }
            }
        });
        viewHolder.mSyncInv.setVisibility(View.VISIBLE);
        viewHolder.mSyncInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNormalClick()) {
                    mOnItemClickListener.onSyncData(invTaskItem, i);
                }
            }
        });
        viewHolder.mFinishInv.setVisibility(View.VISIBLE);
        viewHolder.mFinishInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNormalClick()) {
                    mOnItemClickListener.onFinishInv(invTaskItem, i);
                }
            }
        });
        viewHolder.divideLine.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mInvTaskorders == null ? 0 : mInvTaskorders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.invtask_title)
        TextView mInvTitle;
        @BindView(R.id.invtask_code)
        TextView mInvCode;
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
        //????????????????????????????????????
        @BindView(R.id.inv_finished_num)
        TextView mFinishedNum;
        @BindView(R.id.bt_start_inv)
        Button mStartInv;
        @BindView(R.id.bt_sync_data)
        Button mSyncInv;
        @BindView(R.id.bt_finish_inv)
        Button mFinishInv;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.divider_three)
        View divideLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onSyncData(ResultInventoryOrder invOder, int position);

        void onFinishInv(ResultInventoryOrder invOder, int position);

        void onDetailInv(ResultInventoryOrder invOder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void showInvDetailActivity(ResultInventoryOrder invOrder) {
        Intent intent = new Intent();
        intent.putExtra(INV_ID, invOrder.getId());
        intent.putExtra(INV_NAME, invOrder.getInv_name());
        intent.putExtra(INV_STATUS, invOrder.getInv_status());
        intent.putExtra(INTENT_FROM, "InvAssetAdapter");
        intent.setClass(mContext, InvdetialActivity.class);
        mContext.startActivity(intent);
    }

}

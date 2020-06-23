package com.common.esimrfid.ui.assetinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.ui.inventorytask.InvdetialActivity;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvTaskAdapter extends RecyclerView.Adapter<InvTaskAdapter.ViewHolder> {

    private static final String INV_ID = "inv_id";
    private static final String INV_NAME = "inv_name";
    private static final String INV_STATUS = "inv_status";
    private static final String INTENT_FROM = "intent_from";
    private List<ResultInventoryOrder> mInvTaskorders;
    private Context mContext;

    public InvTaskAdapter(List<ResultInventoryOrder> invTaskorders, Context mContext) {
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

        String userRealName = invTaskItem.getCreator() == null ? "" : invTaskItem.getCreator().getUser_real_name();
        userRealName = TextUtils.isEmpty(userRealName) ? "" : userRealName;
        viewHolder.mInvCreatorName.setText(userRealName);

        viewHolder.mCreateDate.setText(DateUtils.date2String(invTaskItem.getCreate_date()));
        String date=DateUtils.date2String(invTaskItem.getInv_finish_date());
        if(date.equals("")){
            viewHolder.text.setText(R.string.inv_expect_finish_date);
            viewHolder.mExpFinishDate.setText(DateUtils.date2String(invTaskItem.getInv_exptfinish_date()));
        }else {
            viewHolder.text.setText(R.string.inv_finish_date);
            viewHolder.mExpFinishDate.setText(date);
        }
        Integer inv_total_count = invTaskItem.getInv_total_count();
        Integer inv_finish_count = invTaskItem.getInv_finish_count();
        viewHolder.mAllNum.setText(String.valueOf(inv_total_count));
        viewHolder.mFinishedNum.setText(String.valueOf(inv_finish_count));
        viewHolder.mUnfinishedNum.setText(String.valueOf(inv_total_count - inv_finish_count));

        viewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvDetailActivity(invTaskItem);
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
        @BindView(R.id.invtask_code)
        TextView mInvCode;
        @BindView(R.id.inv_creator_name)
        TextView mInvCreatorName;
        @BindView(R.id.create_date)
        TextView mCreateDate;
        @BindView(R.id.expect_finish_date)
        TextView mExpFinishDate;
        @BindView(R.id.tv_text)
        TextView text;
        @BindView(R.id.inv_all_num)
        TextView mAllNum;
        @BindView(R.id.unfinished_num)
        TextView mUnfinishedNum;
        @BindView(R.id.inv_finished_num)
        TextView mFinishedNum;
        @BindView(R.id.rl_task_item)
        RelativeLayout mRelativeLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    private void showInvDetailActivity(ResultInventoryOrder invOrder) {
        Intent intent = new Intent();
        intent.putExtra(INV_ID, invOrder.getId());
        intent.putExtra(INV_NAME, invOrder.getInv_name());
        intent.putExtra(INV_STATUS, invOrder.getInv_status());
        intent.putExtra(INTENT_FROM, "InvTaskAdapter");
        intent.setClass(mContext, InvdetialActivity.class);
        mContext.startActivity(intent);
    }

}

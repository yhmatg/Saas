package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.ui.home.AssetLocationNum;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvTaskAdapter extends RecyclerView.Adapter<InvTaskAdapter.ViewHolder> {

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
        String invName = TextUtils.isEmpty(invTaskItem.getInv_name()) ? "无信息" : invTaskItem.getInv_name();
        viewHolder.mInvTitle.setText(invName);

        String userRealName = invTaskItem.getCreator() == null ? "无信息" : invTaskItem.getCreator().getUser_real_name();
        userRealName = TextUtils.isEmpty(userRealName) ? "无信息" : userRealName;
        viewHolder.mInvCreatorName.setText(userRealName);

        viewHolder.mCreateDate.setText(DateUtils.date2String(invTaskItem.getCreate_date()));
        viewHolder.mExpFinishDate.setText(DateUtils.date2String(invTaskItem.getInv_exptfinish_date()));
        Integer inv_total_count = invTaskItem.getInv_total_count();
        Integer inv_finish_count = invTaskItem.getInv_finish_count();
        viewHolder.mAllNum.setText(String.valueOf(inv_total_count));
        viewHolder.mFinishedNum.setText(String.valueOf(inv_finish_count));
        viewHolder.mUnfinishedNum.setText(String.valueOf(inv_total_count - inv_finish_count));


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
        @BindView(R.id.inv_finished_num)
        TextView mFinishedNum;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


}

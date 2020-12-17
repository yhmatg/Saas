package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.customview.CircleNumberProgress;
import com.common.esimrfid.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvLocationAdapter extends RecyclerView.Adapter<InvLocationAdapter.ViewHolder> {
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    public static final String LOC_Name = "loc_name";
    private static final String INTENT_FROM = "intent_from";

    private List<InvLocationBean> mLoctionBeans;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    InvLocationBean preInvLocationBean;

    public InvLocationAdapter(List<InvLocationBean> mLoctionBeans, Context mContext) {
        this.mLoctionBeans = mLoctionBeans;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.inv_location_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InvLocationBean invLocationBean = mLoctionBeans.get(i);
        viewHolder.mCircleProgress.setProgress(invLocationBean.getProgress());
        viewHolder.locName.setText(invLocationBean.getLocNmme());
        viewHolder.allNum.setText(String.valueOf(invLocationBean.getAllNum()));
        viewHolder.notInvNum.setText(String.valueOf(invLocationBean.getNotInvNum()));
        viewHolder.invNum.setText(String.valueOf(invLocationBean.getInvNum()));
        viewHolder.moreInvNum.setText(String.valueOf(invLocationBean.getMoreInvNum()));
        viewHolder.lessInvNum.setText(String.valueOf(invLocationBean.getLessInvNum()));
        if(invLocationBean.getNotInvNum() == 0){
            viewHolder.invStatus.setText("已完成");
            viewHolder.invStatus.setTextColor(mContext.getColor(R.color.loc_finished_color));
        }else {
            viewHolder.invStatus.setText("未完成");
            viewHolder.invStatus.setTextColor(mContext.getColor(R.color.loc_no_finish));
        }
        viewHolder.mFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null && CommonUtils.isNormalClick()){
                    mOnItemClickListener.onItemSelected(invLocationBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLoctionBeans == null ? 0 : mLoctionBeans.size();
    }

    public interface OnItemClickListener {
        void onItemSelected(InvLocationBean InvLocationBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.filter_layout)
        RelativeLayout mFilterLayout;
        @BindView(R.id.circle_progress)
        CircleNumberProgress mCircleProgress;
        @BindView(R.id.loc_name)
        TextView locName;
        @BindView(R.id.all_num)
        TextView allNum;
        @BindView(R.id.not_inved_num)
        TextView notInvNum;
        @BindView(R.id.tv_inved_num)
        TextView invNum;
        @BindView(R.id.more_inved_num)
        TextView moreInvNum;
        @BindView(R.id.less_inved_num)
        TextView lessInvNum;
        @BindView(R.id.inv_status)
        TextView invStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

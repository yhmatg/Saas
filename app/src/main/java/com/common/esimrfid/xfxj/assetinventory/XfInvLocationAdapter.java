package com.common.esimrfid.xfxj.assetinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.customview.CircleNumberProgress;
import com.common.esimrfid.ui.inventorytask.InvLocationBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XfInvLocationAdapter extends RecyclerView.Adapter<XfInvLocationAdapter.ViewHolder> {
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    public static final String LOC_Name = "loc_name";
    private static final String INTENT_FROM = "intent_from";

    private List<InvLocationBean> mLoctionBeans;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    InvLocationBean preInvLocationBean;

    public XfInvLocationAdapter(List<InvLocationBean> mLoctionBeans, Context mContext) {
        this.mLoctionBeans = mLoctionBeans;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.xf_inv_location_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InvLocationBean invLocationBean = mLoctionBeans.get(i);
        viewHolder.mCircleProgress.setProgress(invLocationBean.getProgress());
        viewHolder.locName.setText(invLocationBean.getLocNmme());
        viewHolder.notInvNum.setText(String.valueOf(invLocationBean.getAllNum()));
        viewHolder.invNum.setText(String.valueOf(invLocationBean.getInvNum()));
        viewHolder.lessInvNum.setText(String.valueOf(invLocationBean.getNotInvNum()));
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
                Intent intent = new Intent();
                intent.putExtra(INV_ID, invLocationBean.getInvId());
                intent.putExtra(LOC_IC, invLocationBean.getLocId());
                intent.putExtra(LOC_Name, invLocationBean.getLocNmme());
                intent.setClass(mContext, XfInvAssetLocActivity.class);
                mContext.startActivity(intent);
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
        @BindView(R.id.not_inved_num)
        TextView notInvNum;
        @BindView(R.id.tv_inved_num)
        TextView invNum;
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

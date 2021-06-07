package com.common.esimrfid.ui.distribute;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistTypeDetail;
import com.common.esimrfid.customview.CircleNumberProgress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistributeTypeAdapter extends RecyclerView.Adapter<DistributeTypeAdapter.ViewHolder> {
    private Context mContext;
    private List<DistTypeDetail> detail;
    private OnItemClickListener mOnItemClickListener;
    private boolean isFinish;

    public DistributeTypeAdapter(Context mContext, List<DistTypeDetail> detail, boolean isFinish) {
        this.mContext = mContext;
        this.detail = detail;
        this.isFinish = isFinish;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dist_type_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DistTypeDetail distTypeDetail = detail.get(i);
        if(isFinish){
            distTypeDetail.setAlreadyAdd(distTypeDetail.getAmount());
        }
        viewHolder.mCircleProgress.setProgress(distTypeDetail.getProgress());
        viewHolder.mBigType.setText(distTypeDetail.getClass_name());
        viewHolder.mSmallType.setText(distTypeDetail.getType_name());
        viewHolder.mExplainContent.setText(distTypeDetail.getRemark());
        viewHolder.mCollectNumb.setText(String.valueOf(distTypeDetail.getAmount()));
        viewHolder.mAlreadyAdd.setText(String.valueOf(distTypeDetail.getAlreadyAdd()));
        viewHolder.mNotAdd.setText(String.valueOf(distTypeDetail.getNotAdd()));
        viewHolder.typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClicked(distTypeDetail);
                }
            }
        });
        if (distTypeDetail.getAlreadyAdd() > distTypeDetail.getAmount()) {
            viewHolder.mAlreadyAdd.setTextColor(mContext.getColor(R.color.red_color));
        } else {
            viewHolder.mAlreadyAdd.setTextColor(mContext.getColor(R.color.home_text_one));
        }
    }

    @Override
    public int getItemCount() {
        return detail == null ? 0 : detail.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_type_layout)
        RelativeLayout typeLayout;
        @BindView(R.id.circle_progress)
        CircleNumberProgress mCircleProgress;
        @BindView(R.id.tv_big_type)
        TextView mBigType;
        @BindView(R.id.tv_small_type)
        TextView mSmallType;
        @BindView(R.id.tv_explain_content)
        TextView mExplainContent;
        @BindView(R.id.tv_collect_numb)
        TextView mCollectNumb;
        @BindView(R.id.tv_already_add_numb)
        TextView mAlreadyAdd;
        @BindView(R.id.tv_not_add_numb)
        TextView mNotAdd;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(DistTypeDetail distTypeDetail);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}

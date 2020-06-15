package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.common.esimrfid.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltterAdapter extends RecyclerView.Adapter<FiltterAdapter.ViewHolder> {

    private List<FilterBean> mFilterBeans;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    FilterBean preFilterBean;

    public FiltterAdapter(List<FilterBean> mFilterBeans, Context mContext) {
        this.mFilterBeans = mFilterBeans;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.filter_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FilterBean filterBean = mFilterBeans.get(i);
        viewHolder.mFilterName.setText(filterBean.getName());
        if(filterBean.getSelected()){
            viewHolder.mFilterName.setTextColor(mContext.getColor(R.color.titele_color));
            viewHolder.mSelectImg.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mFilterName.setTextColor(mContext.getColor(R.color.repair_text));
            viewHolder.mSelectImg.setVisibility(View.GONE);
        }
        viewHolder.filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preFilterBean != null){
                    preFilterBean.setSelected(false);
                }
                filterBean.setSelected(true);
                preFilterBean = filterBean;
                notifyDataSetChanged();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemSelected(filterBean);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterBeans == null ? 0 : mFilterBeans.size();
    }

    public interface OnItemClickListener {
        void onItemSelected(FilterBean filterBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.filter_name)
        TextView mFilterName;
        @BindView(R.id.select_img)
        ImageView mSelectImg;
        @BindView(R.id.filter_layout)
        RelativeLayout filterLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

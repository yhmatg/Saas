package com.common.esimrfid.ui.assetsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetDetailItemAdapter extends RecyclerView.Adapter<AssetDetailItemAdapter.ViewHolder> {
    private Context context;
    private List<AssetDetailItem> mData;

    public AssetDetailItemAdapter(Context context, List<AssetDetailItem> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.asset_detial_item_layout, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetDetailItem assetDetailItem = mData.get(i);
        viewHolder.tvName.setText(assetDetailItem.getName());
        String content = StringUtils.isEmpty(assetDetailItem.getContent()) ? "" : assetDetailItem.getContent();
        viewHolder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

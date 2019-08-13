package com.common.esimrfid.ui.cardsearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.invscannbeans.AssetsInfo;
import com.common.esimrfid.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanRfidAdapter extends RecyclerView.Adapter<ScanRfidAdapter.ViewHolder> {

    private List<AssetsInfo> mAssetsInfos;
    private Context mContext;

    public ScanRfidAdapter(Context mContext, List<AssetsInfo> mAssetsInfos) {
        this.mAssetsInfos = mAssetsInfos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_scan_entering, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetsInfo assetsInfo = mAssetsInfos.get(i);
        viewHolder.tvBrand.setText("名称：" + assetsInfo.getAst_name());
        String status = assetsInfo.getAst_used_status() == null ? "" : assetsInfo.getAst_used_status().getName();
        viewHolder.tvStatus.setText("状态：" + status);
        String type = assetsInfo.getType_info() == null ? "" : assetsInfo.getType_info().getType_name();
        viewHolder.tvType.setText("类型：" + type);
        viewHolder.tvModel.setText("型号：" + assetsInfo.getAst_model());
        viewHolder.tvNum.setText("编号：" + assetsInfo.getAst_code());
        String imgUrl = assetsInfo.getAst_img_url();
        String finalUrl = "";
        if (TextUtils.isEmpty(imgUrl)) {
            finalUrl = "";
        } else if (imgUrl.contains("http")) {
            int index = imgUrl.lastIndexOf(".");
            StringBuilder builder = new StringBuilder(imgUrl);
            finalUrl = builder.replace(index, index + 1,
                    "_" + DensityUtil.dip2px(mContext, 60) +
                            "x" + DensityUtil.dip2px(mContext, 60) + ".").toString();
        } else {
            finalUrl = imgUrl;
        }

        Glide.with(mContext)
                .load(finalUrl)
                .centerCrop()
                .placeholder(R.drawable.icon_wait)
                .error(R.drawable.icon_nofind)
                .into(viewHolder.typeImg);

        viewHolder.typeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(mContext,PhotoViewActivity.class);
                viewIntent.putExtra("imgurl", imgUrl);
                mContext.startActivity(viewIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAssetsInfos == null ? 0 : mAssetsInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.typeImg)
        ImageView typeImg;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_brand)
        TextView tvBrand;
        @BindView(R.id.tv_model)
        TextView tvModel;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_owner)
        TextView tvOwner;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}

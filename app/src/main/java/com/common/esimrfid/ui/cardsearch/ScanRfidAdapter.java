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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.esimrfid.R;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

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
        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "无信息" : assetsInfo.getAst_name();
        viewHolder.tvName.setText("名称：" + astName);

        Integer status = assetsInfo.getAst_used_status();
        String statusName = TextUtils.isEmpty(AssetsUseStatus.getName(status)) ? "无信息" : AssetsUseStatus.getName(status);
        viewHolder.tvStatus.setText("状态：" + statusName);

        String type = assetsInfo.getType_info() == null ? "无信息" : assetsInfo.getType_info().getType_name();
        type = TextUtils.isEmpty(type) ? "无信息" : type;
        viewHolder.tvType.setText("类型：" + type);

        String astMode = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "无信息" : assetsInfo.getAst_model();
        viewHolder.tvModel.setText("型号：" + astMode);

        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "无信息" : assetsInfo.getAst_barcode();
        viewHolder.tvNum.setText("编号：" + astBarcode);

        String imgUrl = TextUtils.isEmpty(assetsInfo.getAst_img_url()) ? "" : assetsInfo.getAst_img_url();
        String finalUrl = DataManager.getInstance().getHostUrl() + imgUrl;
        /*if (TextUtils.isEmpty(imgUrl)) {
            finalUrl = "";
        } else if (imgUrl.contains("http")) {
            int index = imgUrl.lastIndexOf(".");
            StringBuilder builder = new StringBuilder(imgUrl);
            finalUrl = builder.replace(index, index + 1,
                    "_" + DensityUtil.dip2px(mContext, 60) +
                            "x" + DensityUtil.dip2px(mContext, 60) + ".").toString();
        } else {
            finalUrl = imgUrl;
        }*/

        Glide.with(mContext)
                .load(finalUrl)
                .centerCrop()
                //.placeholder(R.drawable.icon_wait)
                .error(R.drawable.icon_nofind)
                .into(viewHolder.typeImg);

        viewHolder.typeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(mContext, PhotoViewActivity.class);
                viewIntent.putExtra("imgurl", finalUrl);
                mContext.startActivity(viewIntent);
            }
        });
        String model=android.os.Build.MODEL;
        if("ESUR-H600".equals(model)|| "SD60".equals(model)){
            viewHolder.mCommonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchIntent = new Intent(mContext, SearchCardLocaionActivity.class);
                    searchIntent.putExtra("astName", assetsInfo.getAst_name());
                    searchIntent.putExtra("epcData", assetsInfo.getAst_epc_code());
                    searchIntent.putExtra("astBarcode", assetsInfo.getAst_barcode());
                    mContext.startActivity(searchIntent);
                }
            });
        }
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_model)
        TextView tvModel;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_owner)
        TextView tvOwner;
        @BindView(R.id.ll_common_info)
        LinearLayout mCommonInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

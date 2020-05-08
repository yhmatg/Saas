package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvDetailAdapter extends RecyclerView.Adapter<InvDetailAdapter.ViewHolder> {

    private List<InventoryDetail> mInventoryDetails;
    private Context mContext;

    public InvDetailAdapter(List<InventoryDetail> inventoryDetails, Context mContext) {
        this.mInventoryDetails = inventoryDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.asset_detail_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InventoryDetail invDetail = mInventoryDetails.get(i);
        AssetsInfo assetsInfo = invDetail.getAssetsInfos();

        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "无信息" : assetsInfo.getAst_barcode();
        viewHolder.tvAssetId.setText(astBarcode);

        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "无信息" : assetsInfo.getAst_name();
        viewHolder.tvAssetName.setText(astName);

        String location = assetsInfo.getLoc_info() == null ? "无信息" : assetsInfo.getLoc_info().getLoc_name();
        location = TextUtils.isEmpty(location) ? "无信息" : location;
        viewHolder.tvAssetLocation.setText(location);

        Integer status = invDetail.getInvdt_status().getCode();
        if (status == 0) {
            viewHolder.tvInvStatus.setText(R.string.ast_not_inved);
            viewHolder.tvInvStatus.setBackground(mContext.getDrawable(R.drawable.not_invted_bg));
        } else if (status == 1) {
            viewHolder.tvInvStatus.setText(R.string.ast_inved);
            viewHolder.tvInvStatus.setBackground(mContext.getDrawable(R.drawable.btn_background));
        } else if (status == 2) {
            viewHolder.tvInvStatus.setText(R.string.ast_inved);
            viewHolder.tvInvStatus.setBackground(mContext.getDrawable(R.drawable.btn_background));
        }

    }

    @Override
    public int getItemCount() {
        return mInventoryDetails == null ? 0 : mInventoryDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_asset_num)
        TextView tvAssetId;
        @BindView(R.id.tv_asset_name)
        TextView tvAssetName;
        @BindView(R.id.tv_asset_location)
        TextView tvAssetLocation;
        @BindView(R.id.tv_inv_status)
        TextView tvInvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

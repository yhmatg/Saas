package com.common.esimrfid.ui.invdetail;

import android.content.Context;
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
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvDetailAdapter extends RecyclerView.Adapter<InvDetailAdapter.ViewHolder> {

    private static final String COLLAPSE_UP = "COLLAPSE_UP";
    private static final String COLLAPSE_DOWN = "COLLAPSE_DOWN";
    private List<InventoryDetail> inventoryOrders;
    private Map<Integer, String> mCollMap = new HashMap<>();
    private Context mContext;

    public InvDetailAdapter(List<InventoryDetail> inventoryOrders, Context mContext) {
        this.inventoryOrders = inventoryOrders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_inv_detail, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InventoryDetail invDetail = inventoryOrders.get(i);
        AssetsInfo assetsInfo = invDetail.getAssetsInfos();
        Integer status = invDetail.getInvdt_status().getCode();
        if (status == 0) {
            viewHolder.tvStatus.setText("未盘点");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.un_checked));
            //viewHolder.ivCollapse.setVisibility(View.INVISIBLE);
        } else if (status == 1) {
            viewHolder.tvStatus.setText("已盘点");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
            //viewHolder.ivCollapse.setVisibility(View.VISIBLE);
        } else if (status == 2) {
            viewHolder.tvStatus.setText("未提交");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.processing));
            //viewHolder.ivCollapse.setVisibility(View.VISIBLE);
        }
        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "无信息" : assetsInfo.getAst_barcode();
        viewHolder.tvInvId.setText("资产编号：" + astBarcode);

        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "无信息" : assetsInfo.getAst_name();
        viewHolder.tvBrand.setText("资产名称：" + astName);

        String astMode = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "无信息" : assetsInfo.getAst_model();
        viewHolder.tvModel.setText("型号：" + astMode);

        String typeName = assetsInfo.getType_info() == null ? "无信息" : assetsInfo.getType_info().getType_name();
        typeName = TextUtils.isEmpty(typeName) ? "无信息" : typeName;
        viewHolder.tvInvType.setText("类型：" + typeName);

        String roomName = assetsInfo.getOrg_useddept() == null ? "无信息" : assetsInfo.getOrg_useddept().getOrg_name();
        roomName = TextUtils.isEmpty(roomName) ? "无信息" : roomName;
        viewHolder.tvOrg.setText("使用部门：" + roomName);

        String loc_name = assetsInfo.getLoc_info() == null ? "无信息" : assetsInfo.getLoc_info().getLoc_name();
        loc_name = TextUtils.isEmpty(loc_name) ? "无信息" : loc_name;
        viewHolder.tvLoc.setText("位置：" + loc_name);


        Integer astUsedStatus = assetsInfo.getAst_used_status() == null ? 0 : assetsInfo.getAst_used_status();
        if (astUsedStatus == AssetsUseStatus.IN_USED.getIndex()) {
            viewHolder.tvOwner.setVisibility(View.VISIBLE);
            String userName = assetsInfo.getUser_info() == null ? "无信息" : assetsInfo.getUser_info().getUser_real_name();
            userName = TextUtils.isEmpty(userName) ? "无信息" : userName;
            viewHolder.tvOwner.setText("使用人：" + userName);
        } else {
            // 在库
            viewHolder.tvOwner.setVisibility(View.GONE);
        }
        String url = DataManager.getInstance().getHostUrl() + assetsInfo.getAst_img_url();
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                //.transform(new GlideRoundTransform(mContext, 5))
                //.placeholder(R.drawable.icon_wait)
                .error(R.drawable.icon_nofind)
                .into(viewHolder.typeImg);

        /*String s = mCollMap.get(i);
        if (s != null) {
            if (COLLAPSE_UP.equals(s)) {
                // 展开状态
                showDetails(viewHolder, invDetail);
            } else if (COLLAPSE_DOWN.equals(s)) {
                // 初始状态
                hideDetails(viewHolder, invDetail);
            }
        } else {
            // 初始状态
            if (status != null && status == 0) {
                // 未盘点 显示所有信息
                showDetails(viewHolder, invDetail);
                mCollMap.put(i, COLLAPSE_UP);
            } else {
                // 已盘点 隐藏部分信息
                hideDetails(viewHolder, invDetail);
                mCollMap.put(i, COLLAPSE_DOWN);
            }
        }*/

       /* viewHolder.ivCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = mCollMap.get(i);
                if (COLLAPSE_DOWN.equals(st)) {
                    mCollMap.put(i, COLLAPSE_UP);
                    viewHolder.ivCollapse.setVisibility(View.INVISIBLE);
                    showDetails(viewHolder, invDetail);
                    notifyItemChanged(i);

                } else if (COLLAPSE_UP.equals(st)) {
                    mCollMap.put(i, COLLAPSE_DOWN);
                    viewHolder.ivCollapse.setVisibility(View.VISIBLE);
                    hideDetails(viewHolder, invDetail);
                    notifyItemChanged(i);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return inventoryOrders == null ? 0 : inventoryOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.typeImg)
        ImageView typeImg;
        @BindView(R.id.tv_inv_id)
        TextView tvInvId;
        @BindView(R.id.tv_org)
        TextView tvOrg;
        @BindView(R.id.tv_brand)
        TextView tvBrand;
        @BindView(R.id.tv_owner)
        TextView tvOwner;
        @BindView(R.id.tv_loc)
        TextView tvLoc;
        @BindView(R.id.tv_inv_type)
        TextView tvInvType;
        @BindView(R.id.tv_model)
        TextView tvModel;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_collapse)
        ImageView ivCollapse;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void showDetails(ViewHolder viewHolder, InventoryDetail invDetail) {
        viewHolder.ivCollapse.setImageResource(R.drawable.icon_arrow_up);
        viewHolder.tvModel.setVisibility(View.VISIBLE);
        viewHolder.tvInvType.setVisibility(View.VISIBLE);
        viewHolder.tvLoc.setVisibility(View.VISIBLE);
        Integer astUsedStatus = invDetail.getAssetsInfos().getAst_used_status() == null ? 0 : invDetail.getAssetsInfos().getAst_used_status();
        if (astUsedStatus == AssetsUseStatus.IN_USED.getIndex())
            viewHolder.tvOwner.setVisibility(View.VISIBLE);
        else
            viewHolder.tvOwner.setVisibility(View.GONE);
    }

    private void hideDetails(ViewHolder viewHolder, InventoryDetail invDetail) {
        viewHolder.ivCollapse.setImageResource(R.drawable.icon_arrow_down);
        viewHolder.tvModel.setVisibility(View.GONE);
        viewHolder.tvInvType.setVisibility(View.GONE);
        viewHolder.tvOwner.setVisibility(View.GONE);
        viewHolder.tvLoc.setVisibility(View.GONE);
    }
}

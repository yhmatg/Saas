package com.common.esimrfid.ui.newinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.ui.assetsearch.AssetsDetailsActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvAssetAdapter extends RecyclerView.Adapter<InvAssetAdapter.ViewHolder> {

    private static final String ASSETS_ID = "assets_id";
    private static final String WHERE_FROM = "where_from";
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    private List<InventoryDetail> mInventoryDetails;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public InvAssetAdapter(List<InventoryDetail> inventoryDetails, Context mContext) {
        this.mInventoryDetails = inventoryDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.inv_asset_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InventoryDetail invDetail = mInventoryDetails.get(i);
        String astName = TextUtils.isEmpty(invDetail.getAst_name()) ? "" : invDetail.getAst_name();
        viewHolder.tvAstName.setText(astName);
        String astBarcode = TextUtils.isEmpty(invDetail.getAst_barcode()) ? "" : invDetail.getAst_barcode();
        viewHolder.tvAssetNum.setText(astBarcode);
        String storeLoc = invDetail.getLoc_name() == null ? "" : invDetail.getLoc_name();
        viewHolder.tvLocName.setText(storeLoc);
        String useDepart = invDetail.getOrg_useddept_name() == null ? "" : invDetail.getOrg_useddept_name();
        viewHolder.tvDepartName.setText(useDepart);
        String userName = invDetail.getUser_name() == null ? "" : invDetail.getUser_name();
        viewHolder.tvUserName.setText(userName);
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ASSETS_ID, invDetail.getAst_id());
                if (invDetail.getInvdt_status().getCode() == 0) {
                    intent.putExtra(WHERE_FROM, "InvAssetLocActivity");
                    intent.putExtra(INV_ID, invDetail.getInv_id());
                    intent.putExtra(LOC_IC, invDetail.getLoc_id());
                    EsimAndroidApp.invStatus = "notInvEdAsset";
                }else {
                    EsimAndroidApp.invStatus = "InvEdAsset";
                }
                intent.setClass(mContext, AssetsDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        Integer status = invDetail.getInvdt_status().getCode();
         if (status == 0) {
            viewHolder.statusImg.setVisibility(View.GONE);
            viewHolder.tvAddTag.setVisibility(View.VISIBLE);
        } else if (status == 1) {
            viewHolder.statusImg.setImageResource(R.drawable.asset_inv_less);
            viewHolder.statusImg.setVisibility(View.VISIBLE);
            viewHolder.tvAddTag.setVisibility(View.GONE);

        } else if (status == 2) {
            viewHolder.statusImg.setImageResource(R.drawable.asset_inv_more);
            viewHolder.statusImg.setVisibility(View.VISIBLE);
            viewHolder.tvAddTag.setVisibility(View.GONE);

        } else if (status == 10 || status == 101) {
            viewHolder.statusImg.setImageResource(R.drawable.asset_inved);
            viewHolder.statusImg.setVisibility(View.VISIBLE);
            viewHolder.tvAddTag.setVisibility(View.GONE);

        }
        if (!"InventoryTaskActivity".equals(EsimAndroidApp.activityFrom)) {
            viewHolder.tvAddTag.setVisibility(View.GONE);
        }
        viewHolder.tvAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    viewHolder.tvAddTag.setTextColor(mContext.getColor(R.color.white));
                    viewHolder.tvAddTag.setBackground(mContext.getDrawable(R.drawable.add_tag_blu_shape));
                    mOnItemClickListener.onItemClick(invDetail, viewHolder.tvAddTag);
                }
            }
        });
        String signTag = invDetail.getInvdt_sign();
        if(!StringUtils.isEmpty(signTag)){
            viewHolder.tvTagContent.setVisibility(View.VISIBLE);
            viewHolder.tvTagContent.setText(signTag);
        }else {
            viewHolder.tvTagContent.setVisibility(View.GONE);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(InventoryDetail invDetailBean, TextView tvAddTag);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mInventoryDetails == null ? 0 : mInventoryDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_detail)
        RelativeLayout item;
        @BindView(R.id.tv_ast_name)
        TextView tvAstName;
        @BindView(R.id.tv_asset_num)
        TextView tvAssetNum;
        @BindView(R.id.tv_ast_loc_name)
        TextView tvLocName;
        @BindView(R.id.tv_depart_name)
        TextView tvDepartName;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_add_tag)
        TextView tvAddTag;
        @BindView(R.id.inv_status_img)
        ImageView statusImg;
        @BindView(R.id.tv_tag_content)
        TextView tvTagContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

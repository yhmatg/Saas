package com.common.esimrfid.xfxj.assetinventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XfLocAssetAdapter extends RecyclerView.Adapter<XfLocAssetAdapter.ViewHolder> {

    private static final String ASSETS_ID = "assets_id";
    private static final String WHERE_FROM = "where_from";
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    private List<XfInventoryDetail> mInventoryDetails;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public XfLocAssetAdapter(List<XfInventoryDetail> inventoryDetails, Context mContext) {
        this.mInventoryDetails = inventoryDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.xf_inv_asset_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        XfInventoryDetail invDetail = mInventoryDetails.get(i);
        String astName = TextUtils.isEmpty(invDetail.getAst_name()) ? "" : invDetail.getAst_name();
        viewHolder.tvAstName.setText(astName);
        String astBarcode = TextUtils.isEmpty(invDetail.getAst_barcode()) ? "" : invDetail.getAst_barcode();
        viewHolder.tvAssetNum.setText(astBarcode);
        String storeLoc = invDetail.getLoc_name() == null ? "" : invDetail.getLoc_name();
        viewHolder.tvLocName.setText(storeLoc);
        String useDepart = invDetail.getOrg_belongcorp_name() == null ? "" : invDetail.getOrg_belongcorp_name();
        viewHolder.tvDepartName.setText(useDepart);
        int status = invDetail.getResult();
        if (status == 0) {
            viewHolder.tvTagContent.setText("正常");
        } else {
            viewHolder.tvTagContent.setText("异常");
        }

    }

    public interface OnItemClickListener {
        void onItemClick(XfInventoryDetail invDetailBean, TextView tvAddTag);
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
        @BindView(R.id.tv_tag_content)
        TextView tvTagContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

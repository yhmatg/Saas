package com.common.esimrfid.ui.requisition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionAssetInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequAssetAdapter extends RecyclerView.Adapter<RequAssetAdapter.ViewHolder> {

    private List<RequisitionAssetInfo> mAssetsInfos;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private Map<Integer, Boolean> statusMap = new HashMap<>();
    private String mStatus;

    private List<RequisitionAssetInfo> seleceItems = new ArrayList<>();

    public RequAssetAdapter(Context mContext, List<RequisitionAssetInfo> mAssetsInfos, String status) {
        this.mAssetsInfos = mAssetsInfos;
        this.mContext = mContext;
        this.mStatus = status;
        initMap();
    }

    public void initMap() {
        for (int i = 0; i < mAssetsInfos.size(); i++) {
            statusMap.put(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_requisition_asset, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequisitionAssetInfo assetsInfo = mAssetsInfos.get(i);
        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "无信息" : assetsInfo.getAst_name();
        viewHolder.tvBrand.setText("名称：" + astName);

        Integer status = assetsInfo.getAst_used_status() == null ? 0 : assetsInfo.getAst_used_status();
        String statusNmae = TextUtils.isEmpty(AssetsUseStatus.getName(status)) ? "无信息" : AssetsUseStatus.getName(status);
        viewHolder.tvStatus.setText("状态：" + statusNmae);

        String astMode = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "无信息" : assetsInfo.getAst_model();
        viewHolder.tvModel.setText("型号：" + astMode);

        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "无信息" : assetsInfo.getAst_barcode();
        viewHolder.tvNum.setText("编号：" + astBarcode);

        if ("已完成".equals(mStatus)) {
            viewHolder.cbStatus.setVisibility(View.GONE);
        }
        viewHolder.cbStatus.setTag(i);
        viewHolder.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                statusMap.put(pos, isChecked);
                if (isChecked) {
                    if (!seleceItems.contains(assetsInfo)) {
                        seleceItems.add(assetsInfo);
                    }
                } else {
                    seleceItems.remove(assetsInfo);
                }
            }
        });
        if (statusMap.get(i) == null) {
            statusMap.put(i, false);
        }
        viewHolder.cbStatus.setChecked(statusMap.get(i));

    }

    @Override
    public int getItemCount() {
        return mAssetsInfos == null ? 0 : mAssetsInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_brand)
        TextView tvBrand;
        @BindView(R.id.tv_model)
        TextView tvModel;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_owner)
        TextView tvOwner;
        @BindView(R.id.cbx_sta)
        CheckBox cbStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemCheckBoxClick(Boolean status, RequisitionAssetInfo info);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public Map<Integer, Boolean> getStatusMap() {
        return statusMap;
    }

    public List<RequisitionAssetInfo> getSeleceItems() {
        return seleceItems;
    }

}

package com.common.esimrfid.ui.batchedit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.ui.assetsearch.AssetsDetailsActivity;
import com.common.esimrfid.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BatchEditAdapter extends RecyclerView.Adapter<BatchEditAdapter.ViewHolder> {
    private static final String ASSETS_ID = "assets_id";
    private static final String ASSETS_EPC = "assets_epc";
    private Context context;
    private List<SearchAssetsInfo> mData;
    private String model = android.os.Build.MODEL;
    private List<SearchAssetsInfo> mSelectedData = new ArrayList<>();

    public BatchEditAdapter(Context context, List<SearchAssetsInfo> Data) {
        this.context = context;
        this.mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_batch_edit, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SearchAssetsInfo assetsInfo = mData.get(i);
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected() && CommonUtils.isNormalClick()) {
                    Intent intent = new Intent();
                    intent.putExtra(ASSETS_ID, assetsInfo.getId());
                    intent.setClass(context, AssetsDetailsActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "" : assetsInfo.getAst_barcode();
        viewHolder.ast_code.setText(astBarcode);
        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "" : assetsInfo.getAst_name();
        viewHolder.ast_name.setText(astName);
        String astBrand = TextUtils.isEmpty(assetsInfo.getAst_brand()) ? "" : assetsInfo.getAst_brand();
        viewHolder.brand.setText(astBrand);
        String astModel = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "" : assetsInfo.getAst_model();
        viewHolder.model.setText(astModel);
        String astLocation = TextUtils.isEmpty(assetsInfo.getLoc_name()) ? "" : assetsInfo.getLoc_name();
        viewHolder.location.setText(astLocation);

        if(assetsInfo.isSelected()){
            viewHolder.cbRepair.setChecked(true);
        }else {
            viewHolder.cbRepair.setChecked(false);
        }
        viewHolder.cbRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assetsInfo.isSelected()){
                    assetsInfo.setSelected(false);
                    mSelectedData.remove(assetsInfo);
                }else {
                    assetsInfo.setSelected(true);
                    if(!mSelectedData.contains(assetsInfo)){
                        mSelectedData.add(assetsInfo);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.res_layout)
        LinearLayout itemLayout;
        @BindView(R.id.tv_ast_code)
        TextView ast_code;
        @BindView(R.id.tv_ast_name)
        TextView ast_name;
        @BindView(R.id.tv_brand)
        TextView brand;
        @BindView(R.id.tv_model)
        TextView model;
        @BindView(R.id.tv_location)
        TextView location;
        @BindView(R.id.cb_repair)
        CheckBox cbRepair;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String epc);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    }

    public List<SearchAssetsInfo> getmSelectedData() {
        return mSelectedData;
    }
}

package com.common.esimrfid.ui.astlist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.ui.assetsearch.AssetsDetailsActivity;
import com.common.esimrfid.ui.tagwrite.SearchTagActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder> {
    private static final String TAG_EPC = "tag_epc";
    private static final String ASSETS_ID="assets_id";
    private List<AssetsInfo> Data;
    private Context context;

    public AssetListAdapter(Context context, List<AssetsInfo> assetsInfos) {
        this.context = context;
        this.Data = assetsInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_write_tag, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetsInfo assetsInfo = Data.get(i);
        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "" : assetsInfo.getAst_barcode();
        viewHolder.ast_code.setText(astBarcode);
        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "" : assetsInfo.getAst_name();
        viewHolder.ast_name.setText(astName);
        String astBrand = TextUtils.isEmpty(assetsInfo.getAst_brand()) ? "" : assetsInfo.getAst_brand();
        viewHolder.brand.setText(astBrand);
        String astModel = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "" : assetsInfo.getAst_model();
        viewHolder.model.setText(astModel);
        String astLocation = assetsInfo.getLoc_info() == null ? "" : assetsInfo.getLoc_info().getLoc_name();
        viewHolder.location.setText(astLocation);

        viewHolder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String epc = assetsInfo.getAst_epc_code();
                if (!TextUtils.isEmpty(epc)) {
                    Intent intent = new Intent();
                    intent.putExtra(TAG_EPC, epc);
                    intent.setClass(context, SearchTagActivity.class);
                    context.startActivity(intent);
                } else {
                    ToastUtils.showShort("资产Epc为空！");
                }

            }
        });
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isNetworkConnected()){
                    Intent intent=new Intent();
                    intent.putExtra(ASSETS_ID,assetsInfo.getId());
                    intent.setClass(context, AssetsDetailsActivity.class);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Data == null ? 0 : Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.search_layout)
        LinearLayout search;
        @BindView(R.id.res_layout)
        LinearLayout itemLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

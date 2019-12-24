package com.common.esimrfid.ui.assetsearch;

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
import com.common.esimrfid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsSearchAdapter extends RecyclerView.Adapter<AssetsSearchAdapter.ViewHolder> {
    private static final String ASSETS_ID="assets_id";
    private static final String ASSETS_EPC="assets_epc";
    private Context context;
    private List<AssetsInfo> mData;
    private OnItemClickListener mOnItemClickListener;

    public AssetsSearchAdapter(Context context, List<AssetsInfo> Data) {
        this.context = context;
        this.mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_assets_search, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetsInfo assetsInfo = mData.get(i);
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(ASSETS_ID,assetsInfo.getId());
                intent.setClass(context,AssetsDetailsActivity.class);
                context.startActivity(intent);
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
        String astLocation = assetsInfo.getLoc_info() == null ? "" : assetsInfo.getLoc_info().getLoc_name();
        viewHolder.location.setText(astLocation);
        viewHolder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String epc=assetsInfo.getAst_epc_code();
                if(!TextUtils.isEmpty(epc)){
                    Intent intent1=new Intent();
                    intent1.putExtra(ASSETS_EPC,epc);
                    intent1.setClass(context, LocationSearchActivity.class);
                    context.startActivity(intent1);
                } else {
                    ToastUtils.showShort("资产Epc为空！");
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
        @BindView(R.id.search_layout)
        LinearLayout search;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String epc);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

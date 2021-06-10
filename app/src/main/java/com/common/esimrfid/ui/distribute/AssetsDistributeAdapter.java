package com.common.esimrfid.ui.distribute;

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
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsDistributeAdapter extends RecyclerView.Adapter<AssetsDistributeAdapter.ViewHolder> {
    private Context context;
    private List<AssetsListItemInfo> mData;
    private OnDeleteClickListener mDeleteListener;
    private boolean isFinish;

    public AssetsDistributeAdapter(Context context, List<AssetsListItemInfo> mData, boolean isFinish) {
        this.context = context;
        this.mData = mData;
        this.isFinish = isFinish;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dist_asset_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetsListItemInfo assetsInfo = mData.get(i);
        String astName = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "" : assetsInfo.getAst_name();
        viewHolder.tvAstName.setText(astName);
        String astBarcode = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "" : assetsInfo.getAst_barcode();
        viewHolder.tvAstCode.setText(astBarcode);
        String astMode = assetsInfo.getAst_model() == null ? "" : assetsInfo.getAst_model();
        viewHolder.tvAstMode.setText(astMode);
        viewHolder.astDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(i);
                notifyDataSetChanged();
                mDeleteListener.onDeleteClick(assetsInfo);
            }
        });
        if (isFinish){
            viewHolder.astDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_detail)
        RelativeLayout item;
        @BindView(R.id.tv_ast_code)
        TextView tvAstCode;
        @BindView(R.id.tv_asset_name)
        TextView tvAstName;
        @BindView(R.id.tv_ast_mode)
        TextView tvAstMode;
        @BindView(R.id.tv_remove)
        TextView astDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AssetsListItemInfo assetsInfo);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onItemClickListener) {
        this.mDeleteListener = onItemClickListener;
    }

    public List<AssetsListItemInfo> getmData() {
        return mData;
    }

}

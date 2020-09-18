package com.common.xfxj.ui.assetrepair;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.common.xfxj.R;
import com.common.xfxj.core.bean.emun.AssetsUseStatus;
import com.common.xfxj.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsRepairAdapter extends RecyclerView.Adapter<AssetsRepairAdapter.ViewHolder> {
    private Context context;
    private List<AssetsInfo> mData;
    private String area;
    private List<AssetsInfo> mSelectedData = new ArrayList<>();
    private OnDeleteClickListener mDeleteListener;

    public AssetsRepairAdapter(Context context, List<AssetsInfo> mData, String area) {
        this.context = context;
        this.mData = mData;
        this.area = area;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_repair_layout, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetsInfo assetsInfo = mData.get(i);
        int astStatus = assetsInfo.getAst_used_status();
        String statusName = TextUtils.isEmpty(AssetsUseStatus.getName(astStatus)) ? "" : AssetsUseStatus.getName(astStatus);
        viewHolder.astStatus.setText(statusName);
        String name = TextUtils.isEmpty(assetsInfo.getAst_name()) ? "" : assetsInfo.getAst_name();
        viewHolder.astName.setText(name);
        String code = TextUtils.isEmpty(assetsInfo.getAst_barcode()) ? "" : assetsInfo.getAst_barcode();
        viewHolder.astNumber.setText(code);
        String type = TextUtils.isEmpty(assetsInfo.getType_name()) ? "" : assetsInfo.getType_name();
        viewHolder.astType.setText(type);
        String astBrand = TextUtils.isEmpty(assetsInfo.getAst_brand()) ? "" : assetsInfo.getAst_brand();
        viewHolder.astBrand.setText(astBrand);
        String astModel = TextUtils.isEmpty(assetsInfo.getAst_model()) ? "" : assetsInfo.getAst_model();
        viewHolder.astMode.setText(astModel);
        String formatNum = String.format(viewHolder.stringPostion,i + 1);
        viewHolder.tvPosition.setText(formatNum);
        viewHolder.astDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(i);
                notifyDataSetChanged();
                mDeleteListener.onDeleteClick(assetsInfo);
            }
        });
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
        if("AssetRepairActivity".equals(area)){
            viewHolder.astDelete.setVisibility(View.VISIBLE);
            viewHolder.tvPosition.setVisibility(View.VISIBLE);
            viewHolder.cbRepair.setVisibility(View.GONE);
        }else {
            viewHolder.astDelete.setVisibility(View.GONE);
            viewHolder.tvPosition.setVisibility(View.GONE);
            viewHolder.cbRepair.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ast_status)
        TextView astStatus;
        @BindView(R.id.ast_name)
        TextView astName;
        @BindView(R.id.ast_number)
        TextView astNumber;
        @BindView(R.id.ast_type)
        TextView astType;
        @BindView(R.id.ast_brand)
        TextView astBrand;
        @BindView(R.id.ast_mode)
        TextView astMode;
        @BindView(R.id.ast_delete)
        ImageButton astDelete;
        @BindView(R.id.cb_repair)
        CheckBox cbRepair;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindString(R.string.select_count)
        String stringPostion;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AssetsInfo assetsInfo);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onItemClickListener) {
        this.mDeleteListener = onItemClickListener;
    }

    public List<AssetsInfo> getmData() {
        return mData;
    }

    public List<AssetsInfo> getmSelectedData() {
        return mSelectedData;
    }
}

package com.common.esimrfid.ui.home;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.esimrfid.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAssetAdapter extends RecyclerView.Adapter<LocationAssetAdapter.ViewHolder> {

    private List<AssetLocationNum> mLocaitonAsset;
    private Context mContext;
    private int mMaxAssetNum;

    public LocationAssetAdapter(List<AssetLocationNum> mLocaitonAsset, Context mContext, int mMaxAssetNum) {
        this.mLocaitonAsset = mLocaitonAsset;
        this.mContext = mContext;
        this.mMaxAssetNum = mMaxAssetNum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.home_asset_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetLocationNum assetNumber = mLocaitonAsset.get(i);
        viewHolder.locationNmme.setText(assetNumber.getLocation());
        viewHolder.assetNumber.setText(String.valueOf(assetNumber.getNumber()));
        int[] intArray = mContext.getResources().getIntArray(R.array.progress_coloer);
        int colorPos = i % intArray.length;
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) viewHolder.numbetPro.getLayoutParams();
        float astNum = assetNumber.getNumber();
        if(mMaxAssetNum == 0){
            astNum = 0;
        }else {
            astNum = (float)(astNum / mMaxAssetNum) * 620;
        }
        if(astNum > 620){
            linearParams.width = 620;
        }else {
            linearParams.width = (int) astNum;
        }
        viewHolder.numbetPro.setLayoutParams(linearParams);
        GradientDrawable progressBg = new GradientDrawable();
        //设置圆角弧度
        progressBg.setCornerRadius(20);
        //设置绘制颜色
        progressBg.setColor(intArray[colorPos]);
        viewHolder.numbetPro.setProgressDrawable(progressBg);

    }

    @Override
    public int getItemCount() {
        return mLocaitonAsset == null ? 0 : mLocaitonAsset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.location_name)
        TextView locationNmme;
        @BindView(R.id.asset_number)
        TextView assetNumber;
        @BindView(R.id.pb_assets)
        ProgressBar numbetPro;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface OnItemClickListener {
        void onRightImgClick(String epc);
    }

    public int getmMaxAssetNum() {
        return mMaxAssetNum;
    }

    public void setmMaxAssetNum(int mMaxAssetNum) {
        this.mMaxAssetNum = mMaxAssetNum;
    }
}

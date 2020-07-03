package com.common.esimrfid.ui.home;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
    private int mMaxProgressLength = 0;

    public LocationAssetAdapter(List<AssetLocationNum> mLocaitonAsset, Context mContext, int mMaxAssetNum) {
        this.mLocaitonAsset = mLocaitonAsset;
        this.mContext = mContext;
        this.mMaxAssetNum = mMaxAssetNum;
        initMaxLength();
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
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) viewHolder.numbetPro.getLayoutParams();
        float astNum = assetNumber.getNumber();
        if(mMaxAssetNum == 0){
            astNum = 0;
        }else {
            astNum = (float)(astNum / mMaxAssetNum) * mMaxProgressLength;
        }
        if(astNum > mMaxProgressLength){
            linearParams.width = mMaxProgressLength;
        }else if(astNum < 1){
            linearParams.width = 1;
        }else {
            linearParams.width = (int) astNum;
        }
        viewHolder.numbetPro.setLayoutParams(linearParams);
        GradientDrawable progressBg = new GradientDrawable();
        //设置圆角弧度
        progressBg.setCornerRadius(20);
        //设置绘制颜色
        progressBg.setColor(mContext.getColor(R.color.home_progress_color));
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

    public void setmMaxAssetNum(int mMaxAssetNum) {
        this.mMaxAssetNum = mMaxAssetNum;
    }


    private void initMaxLength() {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int usedLength = (int)(165 * scale);
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        mMaxProgressLength = widthPixels - usedLength;
    }
}

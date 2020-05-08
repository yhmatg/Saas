package com.common.esimrfid.ui.inventorytask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.common.esimrfid.R;
import com.common.esimrfid.customview.TextProgressBar;
import com.common.esimrfid.ui.home.AssetLocationNum;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanAssetsAdapter extends RecyclerView.Adapter<ScanAssetsAdapter.ViewHolder> {

    private List<AssetLocationNum> mLocaitonAsset;
    private Context mContext;

    public ScanAssetsAdapter(List<AssetLocationNum> mLocaitonAsset, Context mContext) {
        this.mLocaitonAsset = mLocaitonAsset;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.scan_location_item, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetLocationNum assetNumber = mLocaitonAsset.get(i);
        viewHolder.locationNmme.setText(assetNumber.getLocation());
        viewHolder.assetNumber.setText(String.valueOf(assetNumber.getNumber()));
        viewHolder.locationPro.setMax(assetNumber.getNumber());
        viewHolder.locationPro.setProgress(assetNumber.getProgress());

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
        @BindView(R.id.location_assets)
        TextProgressBar locationPro;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}

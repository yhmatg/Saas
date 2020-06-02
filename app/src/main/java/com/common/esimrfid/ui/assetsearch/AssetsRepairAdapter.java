package com.common.esimrfid.ui.assetsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsRepairAdapter extends RecyclerView.Adapter<AssetsRepairAdapter.ViewHolder>{
    private Context context;
    private List<AssetRepair>mData;

    public AssetsRepairAdapter(Context context,List<AssetRepair> data){
        this.context=context;
        this.mData=data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView= LayoutInflater.from(context).inflate(R.layout.item_repair_record,viewGroup,false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetRepair assetRepair=mData.get(i);
        String tv_code= TextUtils.isEmpty(assetRepair.getOdr_code())?"":assetRepair.getOdr_code();
        viewHolder.code.setText(tv_code);
        String tv_name=assetRepair.getTransactor()==null?"":assetRepair.getTransactor().getUser_real_name();
        tv_name=TextUtils.isEmpty(tv_code)?"":tv_name;
        viewHolder.name.setText(tv_name);
        String rep_name=assetRepair.getRepUser()==null?"":assetRepair.getRepUser().getUser_name();
        rep_name=TextUtils.isEmpty(rep_name)?"":rep_name;
        viewHolder.repair_name.setText(rep_name);
        double tv_cost=assetRepair.getMaintain_price();
        NumberFormat nf = new DecimalFormat("¥#,###.##");//设置金额显示格式
        String str = nf.format(tv_cost);
        if(tv_cost==0){
            viewHolder.cost.setVisibility(View.GONE);
        }else {
            viewHolder.cost.setText(str);
        }

        long rep_date=assetRepair.getOdr_date();
        if(rep_date==0){
            viewHolder.mainten_date.setText("");
        }else {
            viewHolder.mainten_date.setText(DateUtils.long2String(rep_date, DateUtils.FORMAT_TYPE_1));
        }

        long fin_date=assetRepair.getMaintain_finish_date();
        if(fin_date==0){
            viewHolder.finish_date.setText("");
        }else {
            viewHolder.finish_date.setText(DateUtils.long2String(fin_date,DateUtils.FORMAT_TYPE_1));
        }

        String remark=TextUtils.isEmpty(assetRepair.getOdr_remark())?"":assetRepair.getOdr_remark();
        viewHolder.explain.setText(remark);

        String tv_status=TextUtils.isEmpty(assetRepair.getOdr_status())?"":assetRepair.getOdr_status();
        viewHolder.status.setText(tv_status);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_code)
        TextView code;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.repair_name)
        TextView repair_name;
        @BindView(R.id.tv_cost)
        TextView cost;
        @BindView(R.id.tv_mainten_date)
        TextView mainten_date;
        @BindView(R.id.tv_finish_date)
        TextView finish_date;
        @BindView(R.id.tv_mainten_explain)
        TextView explain;
        @BindView(R.id.tv_status)
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

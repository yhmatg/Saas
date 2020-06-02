package com.common.esimrfid.ui.assetsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsResumeAdapter extends RecyclerView.Adapter<AssetsResumeAdapter.ViewHolder> {
    private Context context;
    private List<AssetResume> mData;

    public AssetsResumeAdapter(Context context,List<AssetResume>data){
        this.context=context;
        this.mData=data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView= LayoutInflater.from(context).inflate(R.layout.item_asset_resume,viewGroup,false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssetResume assetResume=mData.get(i);
        String tv_method= TextUtils.isEmpty(assetResume.getOpt_operation())?"":assetResume.getOpt_operation();
        viewHolder.method.setText(tv_method);
        String tv_name=TextUtils.isEmpty(assetResume.getTransactor_name())?"":assetResume.getTransactor_name();
        viewHolder.name.setText(tv_name);
        long tv_time=assetResume.getCreate_date();
        if(tv_time==0){
            viewHolder.time.setText("");
        } else{
            viewHolder.time.setText(DateUtils.long2String(tv_time, DateUtils.FORMAT_TYPE_1));
        }
        String tv_content=TextUtils.isEmpty(assetResume.getOpt_details())?"":assetResume.getOpt_details();
        StringBuilder stringBuilder=new StringBuilder();
        if (!tv_content.isEmpty()){
            JSONArray jsonArray=JSONArray.parseArray(tv_content);
            if(jsonArray.size()>0){
                for(int n = 0 ;n < jsonArray.size() ; n++){
                    String changeto=jsonArray.getJSONObject(n).getString("changeto");
                    if(changeto.isEmpty()){
                        changeto="<空>";
                    }
                    String fieldname=jsonArray.getJSONObject(n).getString("fieldname");
                    if(jsonArray.getJSONObject(n).containsKey("changefrom")){
                        String changefrom=jsonArray.getJSONObject(n).getString("changefrom");
                        if (changefrom.isEmpty()){
                            changefrom="<空>" ;
                        }
                        stringBuilder.append("【").append(fieldname).append("】")
                                .append("字段由")
                                .append(changefrom)
                                .append("变更为").append(" \"").append(changeto).append(" \"")
                                .append("\n");
                    }else {
                        stringBuilder.append(fieldname)
                                .append(":").append(changeto)
                                .append("\n");
                    }

                }
            }
        }



        viewHolder.content.setText(stringBuilder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_method)
        TextView method;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_content)
        TextView content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

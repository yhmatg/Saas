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
        String asset_name=TextUtils.isEmpty(assetResume.getAst_name())?"":assetResume.getAst_name();
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

        //处理方式
        String tv_content=TextUtils.isEmpty(assetResume.getOpt_details())?"":assetResume.getOpt_details();
        StringBuilder stringBuilder=new StringBuilder();
        int state=0;
        if (!tv_content.isEmpty()){
            JSONArray jsonArray=JSONArray.parseArray(tv_content);
            if(tv_method.equals("发起调拨")) {
                stringBuilder.append("管理员")
                        .append("\"").append(tv_name).append("\"")
                        .append("发起调拨,");
                state=1;
            }else if(tv_method.equals("新增资产")){
                stringBuilder.append("由管理员")
                        .append("\"").append(tv_name).append("\"")
                        .append("录入")
                        .append("\n");
            }
            if(jsonArray.size()>0){
                for(int n = 0 ;n < jsonArray.size() ; n++){
                    String changeto=jsonArray.getJSONObject(n).getString("changeto");
                    if(changeto.isEmpty()){
                        changeto="<空>";
                    }
                    String fieldname=jsonArray.getJSONObject(n).getString("fieldname");

                    if (state==1){//发起调拨
                        String changefrom1=jsonArray.getJSONObject(n).getString("changefrom");
                        if (fieldname.equals("使用公司")){
                            stringBuilder.append("将资产从")
                                    .append("\"").append(changefrom1).append("\"")
                                    .append("调入到")
                                    .append("\"").append(changeto).append("\",");
                        }else if (fieldname.equals("管理员")){
                            stringBuilder.append("\"").append(changefrom1).append("\"")
                                    .append("名下").append("\n");
                        }
                    }else if(tv_method.equals("资产维修")){
                        if (fieldname.equals("维修说明")){
                            stringBuilder.append(fieldname)
                                    .append(":")
                                    .append(changeto)
                                    .append("\n");
                        }else if(fieldname.equals("资产名称")){
                            stringBuilder.append("将资产")
                                   .append("\"") .append(changeto).append("\"")
                                    .append("的状态变更为")
                                    .append("【维修中】").append("\n");
                        }
                    } else if(tv_method.equals("资产还原")){
                        String changefrom=jsonArray.getJSONObject(n).getString("changefrom");
                        stringBuilder.append("由管理员")
                                .append("\"").append(tv_name).append("\"")
                                .append("还原了资产")
                                .append(";")
                                .append("状态由")
                                .append("\"").append(changefrom).append("\"")
                                .append("变更为")
                                .append("\"").append(changeto).append("\"").append("\n");
                    }else if (tv_method.equals("资产报废")){
                        String changefrom=jsonArray.getJSONObject(n).getString("changefrom");
                        stringBuilder.append("资产")
                                .append("\"").append(asset_name).append("\"")
                                .append("被清理处置；")
                                .append("状态由")
                                .append("\"").append(changefrom).append("\"")
                                .append("变更为")
                                .append(changeto).append("\n");
                    }else {
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

            //新增资产
        }else if(tv_method.equals("资产复制")){
            stringBuilder.append("由管理员")
                    .append("\" ").append(tv_name).append("\" ")
                    .append("复制新增")
                    .append("\n");
        } else if(tv_method.equals("新增资产")){
            stringBuilder.append("由管理员")
                    .append("\"").append(tv_name).append("\"")
                    .append("录入")
                    .append("\n");
        }else if(tv_method.equals("资产批量导入")){
            stringBuilder.append("由管理员")
                    .append("\"").append(tv_name).append("\"")
                    .append("批量新增")
                    .append("\n");
        }else if(tv_method.equals("批量导入资产信息")){
            if (tv_name.isEmpty()){
                stringBuilder=null;
            }else {
                stringBuilder.append("由管理员")
                        .append("\" ").append(tv_name).append("\" ")
                        .append("批量导入")
                        .append("\n");
            }
        }else if (tv_method.equals("取消调拨")){
            stringBuilder.append("由管理员")
                    .append("\"").append(tv_name).append("\"")
                    .append("取消了调拨").append("\n");
        }else if(tv_method.equals("完成维修")){
            stringBuilder.append("由管理员")
                    .append("\"").append(tv_name).append("\"")
                    .append("完成了资产维修").append("\n");
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

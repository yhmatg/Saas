package com.common.esimrfid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.Menu;
import com.common.esimrfid.ui.assetinventory.AssetInventoryActivity;
import com.common.esimrfid.ui.assetrepair.AssetRepairActivity;
import com.common.esimrfid.ui.assetsearch.AssetsSearchActivity;
import com.common.esimrfid.ui.astlist.AssetListActivity;
import com.common.esimrfid.ui.batchedit.BatchEditActivity;
import com.common.esimrfid.ui.distribute.DistribureOrderActivity;
import com.common.esimrfid.ui.identity.IdentityActivity;
import com.common.esimrfid.ui.inventorytask.InventoryTaskActivity;
import com.common.esimrfid.ui.tagwrite.WriteTagActivity;
import com.common.esimrfid.utils.CommonUtils;

import java.util.List;

public class ModeItemAdapter extends BaseAdapter {
    private List<Menu> menus;
    private Context mContext;

    public ModeItemAdapter(List<Menu> menus, Context mContext) {
        this.menus = menus;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Menu getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, null);
            holder = new ViewHolder();
            holder.menuIcon = (ImageView) convertView.findViewById(R.id.iv_mode_icon);
            holder.menuName = (TextView) convertView.findViewById(R.id.tv_mode_name);
            holder.menuLayout = (LinearLayout) convertView.findViewById(R.id.menu_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Menu menuBean = menus.get(position);
        if (menuBean != null) {
            switch (menuBean.getId()) {
                case 20003:
                    holder.menuIcon.setImageResource(R.drawable.asset_inventory);
                    break;
                case 20004:
                    holder.menuIcon.setImageResource(R.drawable.inventory_task);
                    break;
                case 20005:
                    holder.menuIcon.setImageResource(R.drawable.assets_search);
                    break;
                case 20006:
                    holder.menuIcon.setImageResource(R.drawable.write_tag);
                    break;
                case 20007:
                    holder.menuIcon.setImageResource(R.drawable.ast_identity_icon);
                    break;
                case 20008:
                    holder.menuIcon.setImageResource(R.drawable.ast_repair_icon);
                    break;
                case 20009:
                    holder.menuIcon.setImageResource(R.drawable.ast_list_icon);
                    break;
                case 20010:
                    holder.menuIcon.setImageResource(R.drawable.asset_batch);
                    break;
                case 20011:
                    holder.menuIcon.setImageResource(R.drawable.distribute_task);
                    break;
            }
            holder.menuName.setText(menuBean.getMenu_name());
            holder.menuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (menuBean.getId()) {
                        case 20003:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, InventoryTaskActivity.class));
                            }
                            break;
                        case 20004:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, AssetInventoryActivity.class));
                            }
                            break;
                        case 20005:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, AssetsSearchActivity.class));
                            }
                            break;
                        case 20006:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, WriteTagActivity.class));
                            }
                            break;
                        case 20007:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, IdentityActivity.class));
                            }
                            break;
                        case 20008:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, AssetRepairActivity.class));
                            }
                            break;
                        case 20009:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, AssetListActivity.class));
                            }
                            break;
                        case 20010:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, BatchEditActivity.class));
                            }
                        case 20011:
                            if (CommonUtils.isNormalClick()) {
                                mContext.startActivity(new Intent(mContext, DistribureOrderActivity.class));
                            }
                            break;
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView menuIcon;
        TextView menuName;
        LinearLayout menuLayout;
    }
}

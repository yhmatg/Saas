package com.common.esimrfid.ui.writeepc;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.WriteEpcContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.presenter.home.WriteEpcPressnter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WriteEpcActivity extends BaseActivity<WriteEpcPressnter> implements WriteEpcContract.View {
    @BindView(R.id.imgTitleLeft)
    ImageView imgTitleLeft;
    @BindView(R.id.tvTitleCenter)
    TextView tvTitleCenter;
    @BindView(R.id.tv_start_or_stop)
    TextView tvStartOrStop;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.write_recycleview)
    RecyclerView mWriteRecycle;
    private IEsimUhfService esimUhfService=null;
    private List<String> scanEpcs = new ArrayList<>();
    private WriteEpcAdapter mWriteAdapter;
    @Override
    public WriteEpcPressnter initPresenter() {
        return new WriteEpcPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        tvTitleCenter.setText("写卡列表");
        initRfidAndEventbus();
        mWriteAdapter = new WriteEpcAdapter(this,scanEpcs);
        mWriteAdapter.setOnItemClickListener(new WriteEpcAdapter.OnItemClickListener() {
            @Override
            public void onRightImgClick(String epc) {
                showWriteEpcDialog(epc);
            }
        });
        mWriteRecycle.setLayoutManager(new LinearLayoutManager(this));
        mWriteRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mWriteRecycle.setAdapter(mWriteAdapter);
    }

    private void showWriteEpcDialog(String epc) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_write_epc, null);
        TextView mOldEpc = contentView.findViewById(R.id.old_epc);
        mOldEpc.setText(epc);
        MaterialDialog writeDialog = new MaterialDialog.Builder(this)
                .customView(contentView, false)
                .show();
        EditText mEditEpc = contentView.findViewById(R.id.new_epc);
        TextView mCancel = contentView.findViewById(R.id.tv_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDialog.dismiss();
            }
        });
        TextView mWrite = contentView.findViewById(R.id.tv_write);
        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String epcData = mEditEpc.getText().toString();
                if(epcData == null || "".equals(epcData)){
                    ToastUtils.showShort(R.string.error_epc_write);
                }else if(epcData.length() != 24){
                    ToastUtils.showShort(R.string.error_epc_length);
                }else {
                    if(esimUhfService != null){
                        esimUhfService.writeEpcTag(epc,epcData);
                    }else {
                        ToastUtils.showShort(R.string.not_connect_prompt);
                    }

                    writeDialog.dismiss();
                }

            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_epc;
    }

    @Override
    protected void initToolbar() {
    }

    @OnClick({R.id.imgTitleLeft,R.id.tv_start_or_stop,R.id.tv_clear})
    void performClick(View view){
        switch (view.getId()){
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.tv_start_or_stop:
                if(esimUhfService != null){
                    esimUhfService.startStopScanning();
                }else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case R.id.tv_clear:
                scanEpcs.clear();
                mWriteAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        String epc=null;
        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag=(UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                if(!scanEpcs.contains(epc)){
                    scanEpcs.add(epc);
                }
                break;
            case UhfMsgType.UHF_START:
                tvStartOrStop.setText(R.string.search_stop);
                break;
            case UhfMsgType.UHF_STOP:
                tvStartOrStop.setText(R.string.search_card);
                mWriteAdapter.notifyDataSetChanged();
                break;
            case UhfMsgType.UHF_WRITE_SUC:
                ToastUtils.showShort(R.string.write_epc_sucess);
                break;
            case UhfMsgType.UHF_READ_FAIL:
               ToastUtils.showShort(R.string.write_epc_fail);
                break;
        }
    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F4) { //扳机建扫描
            if (esimUhfService != null) {
                esimUhfService.startStopScanning();
            } else {
                ToastUtils.showShort(R.string.not_connect_prompt);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

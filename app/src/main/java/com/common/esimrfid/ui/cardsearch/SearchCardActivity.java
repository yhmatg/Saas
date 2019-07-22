package com.common.esimrfid.ui.cardsearch;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.CardSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.presenter.home.CardSearchPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.utils.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchCardActivity extends BaseActivity<CardSearchPresenter> implements CardSearchContract.View {

    @BindView(R.id.tvTitleLeft)
    TextView mBackBtn;
    @BindView(R.id.tv_company_name)
    TextView mTvComName;
    @BindView(R.id.tv_company_account)
    TextView mTvComAccount;
    @BindView(R.id.tv_check_state)
    TextView mTvCheckState;
    @BindView(R.id.et_company_name)
    EditText mEtComName;
    @BindView(R.id.et_company_account)
    EditText mEtComAccount;
    @BindView(R.id.et_card_num)
    EditText mEtCardNub;
    @BindView(R.id.bt_inquire)
    Button mInquireBtn;
    @BindView(R.id.search_tk_rflayout)
    TwinklingRefreshLayout mTkRefresh;
    @BindView(R.id.card_search_recycle)
    RecyclerView mSearchRecycle;

    List<SignatureCard> mSignatureCards = new ArrayList<>();
    private SearchCardAdAapter searchCardAdAapter;
    private String epcCode;
    IEsimUhfService esimUhfService=null;

    @Override
    public CardSearchPresenter initPresenter() {
        return new CardSearchPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRFID();
        EventBus.getDefault().register(this);
        mSearchRecycle.setLayoutManager(new LinearLayoutManager(this));
        mSearchRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        searchCardAdAapter = new SearchCardAdAapter(mSignatureCards, this);
        mSearchRecycle.setAdapter(searchCardAdAapter);
        mTkRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefreshCanceled();
                mPresenter.findCorpInfoByAll(getEditTextContext(mEtComName),getEditTextContext(mEtComAccount),getEditTextContext(mEtCardNub));
            }
        });
    }

    private void initRFID() {
        esimUhfService = new ZebraUhfServiceImpl();
        esimUhfService.initRFID();
    }

    private void closeRFID() {
        if (esimUhfService.isStart()){
            esimUhfService.closeRFID();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRFID();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_card;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleSerachData(CorpInfo corpInfo) {
        if(corpInfo != null){
            mTvComName.setText(corpInfo.getCorpName());
            mTvComAccount.setText(corpInfo.getCorpAccount());
            mTvCheckState.setText("未找到");
            epcCode = corpInfo.getCorpEpcCode();
            searchCardAdAapter.setRefreshDataList(corpInfo.getSignatureCards());
            mTkRefresh.finishRefreshing();
        }
    }

    @OnClick({R.id.bt_inquire,R.id.tvTitleLeft})
    void performClick(View view){
        switch(view.getId()){
            case R.id.bt_inquire:
                mPresenter.findCorpInfoByAll(getEditTextContext(mEtComName),getEditTextContext(mEtComAccount),getEditTextContext(mEtCardNub));
                break;
            case R.id.tvTitleLeft:
                finish();
                break;
        }
    }

    private String getEditTextContext(EditText editText){
        return editText.getText().toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        String epc=null;
        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag=(UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                if(epc!=null&&epc.equals(epcCode)){
                    mTvCheckState.setText("正常");
                    closeRFID();
                }

                break;
            case UhfMsgType.UHF_START:
                ToastUtils.showShort("开始扫描...");
                break;
            case UhfMsgType.UHF_STOP:
                ToastUtils.showShort("停止扫描...");
                break;
            case UhfMsgType.UHF_CONNECT:
                ToastUtils.showShort("RFID已连接");
                break;
            case UhfMsgType.UHF_DISCONNECT:
                ToastUtils.showShort("RFID已断开连接");

                break;
        }

    }
}

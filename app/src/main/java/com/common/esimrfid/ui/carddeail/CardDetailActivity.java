package com.common.esimrfid.ui.carddeail;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.CardDetailConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.presenter.home.CardDetailPresenter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CardDetailActivity extends BaseActivity<CardDetailPresenter> implements CardDetailConstract.View {
    @BindView(R.id.twinklingRefreshLayout)
    TwinklingRefreshLayout cardDetailRefresh;
    @BindView(R.id.card_detail_recycle)
    RecyclerView cardDetailRecycle;
    CardDetailAdapter mCardDetailAdapter;
    List<SignatureCard> mSignatureCards = new ArrayList<>();
    private String corpAccount;

    @Override
    public CardDetailPresenter initPresenter() {
        return new CardDetailPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        corpAccount = getIntent().getStringExtra("corpAccount");
        cardDetailRecycle.setLayoutManager(new LinearLayoutManager(this));
        cardDetailRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mCardDetailAdapter = new CardDetailAdapter(mSignatureCards,this);
        cardDetailRecycle.setAdapter(mCardDetailAdapter);
        cardDetailRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.fetchAllSignatureCards(corpAccount);
            }
        });
        mPresenter.fetchAllSignatureCards(corpAccount);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_card_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleSingatureCards(List<SignatureCard> singatureCards) {
        mCardDetailAdapter.setRefreshDataList(singatureCards);
        cardDetailRefresh.finishRefreshing();
    }
}

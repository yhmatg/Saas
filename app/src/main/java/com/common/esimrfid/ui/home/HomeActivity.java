package com.common.esimrfid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.common.esimrfid.R;
import com.common.esimrfid.ui.cardsearch.SearchCardActivity;
import com.common.esimrfid.ui.invorder.InvOrderActicity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.imgHomeAssetsScan)
    ImageView homeAssectScann;
    @BindView(R.id.imgHomeAssetsSearch)
    ImageView homeAssectSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imgHomeAssetsScan,R.id.imgHomeAssetsSearch})
    void performClick(View view){
        switch (view.getId()){
            case R.id.imgHomeAssetsScan:
                startActivity(new Intent(this, InvOrderActicity.class));
                break;
            case R.id.imgHomeAssetsSearch:
                startActivity(new Intent(this, SearchCardActivity.class));
                break;

        }
    }
}

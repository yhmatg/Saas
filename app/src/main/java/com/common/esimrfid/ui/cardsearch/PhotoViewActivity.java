package com.common.esimrfid.ui.cardsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.common.esimrfid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends AppCompatActivity {

    @BindView(R.id.photoview)
    PhotoView photoview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);
        String imgUrl = getIntent().getStringExtra("imgurl");
        if (imgUrl == null) {
            imgUrl = "";
        }
        Glide.with(this)
                .load(imgUrl)
                .error(R.drawable.icon_nofind)
                .placeholder(R.drawable.icon_wait)
                .into(photoview);
    }
}

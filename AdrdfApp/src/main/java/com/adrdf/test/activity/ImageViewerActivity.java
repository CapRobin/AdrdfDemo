package com.adrdf.test.activity;



import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.base.app.base.RdfBaseActivity;

import com.adrdf.base.view.photo.RdfPhotoImageViewPager;
import com.adrdf.base.view.photo.RdfPhotoImageViewPagerAdapter;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：ImageViewerActivity
 * Describe：
 * Date：2018-02-23 11:57:40
 * Author: CapRobin@yeah.net
 *
 */
public class ImageViewerActivity extends RdfBaseActivity {

    private RdfPhotoImageViewPager photoImageViewPager = null;
    private RdfPhotoImageViewPagerAdapter photoImageViewPagerAdapter = null;
    private List<String> urlPath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_image_viewer);

        urlPath = getIntent().getStringArrayListExtra("PATH");
        int position = getIntent().getIntExtra("POSITION",0);

        photoImageViewPager = (RdfPhotoImageViewPager) findViewById(R.id.view_pager);
        photoImageViewPagerAdapter = new RdfPhotoImageViewPagerAdapter(this,photoImageViewPager,urlPath,imageLoader);
        photoImageViewPager.setAdapter(photoImageViewPagerAdapter);

        ImageView backBtn = (ImageView) this.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView  imageCount = (TextView) this.findViewById(R.id.image_count);
        imageCount.setText((position+1)+"/"+urlPath.size());
        photoImageViewPager.setCurrentItem(position);

        photoImageViewPager.setOnPageChangeListener(new RdfPhotoImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                imageCount.setText((i+1)+"/"+urlPath.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
	}


}

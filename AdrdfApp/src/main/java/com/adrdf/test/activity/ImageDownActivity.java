package com.adrdf.test.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfAppUtil;
import com.adrdf.base.util.RdfMathUtil;
/**
 * Copyright © CapRobin
 *
 * Name：ImageDownActivity
 * Describe：
 * Date：2018-02-18 12:57:28
 * Author: CapRobin@yeah.net
 *
 */
public class ImageDownActivity extends RdfBaseActivity {
	
	private MyApplication application;

	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	private ImageView imageView3 = null;

    private TextView textView1 = null;
    private TextView textView2 = null;
    private TextView textView3 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_down);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_image);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView1 = (ImageView)this.findViewById(R.id.image_view_1);
        imageView2 = (ImageView)this.findViewById(R.id.image_view_2);
        imageView3 = (ImageView)this.findViewById(R.id.image_view_3);

        textView1 = (TextView)this.findViewById(R.id.text_view_1);
        textView2 = (TextView)this.findViewById(R.id.text_view_2);
        textView3 = (TextView)this.findViewById(R.id.text_view_3);
        
        String imageUrl1 = "http://f.hiphotos.baidu.com/zhidao/pic/item/a9d3fd1f4134970aed3ef2a594cad1c8a6865def.jpg";
        String imageUrl2 = "http://f.hiphotos.baidu.com/zhidao/pic/item/a9d3fd1f4134970aed3ef2a594cad1c8a6865def.jpg";
        String imageUrl3 = "http://f.hiphotos.baidu.com/zhidao/pic/item/a9d3fd1f4134970aed3ef2a594cad1c8a6865def.jpg";

        float density = RdfAppUtil.getDisplayMetrics(this).density;

        //原图片的下载
        //imageLoader.display(imageView1,imageUrl1);

        //使用AbBaseActivity 内定义的imageLoader 可实现在退出activity 自动结束执行线程
        imageLoader.download(imageUrl1, new RdfImageLoader.OnImageDownloadListener() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView1.setImageBitmap(bitmap);
                textView1.setText("原图尺寸："+bitmap.getWidth()+"x"+bitmap.getHeight()+","+ RdfMathUtil.round((float)bitmap.getWidth()/bitmap.getHeight(),1));
            }
        });


        //imageLoader.display(imageView2,imageUrl2,300,300);
        imageLoader.download(imageUrl2, 180,120, new RdfImageLoader.OnImageDownloadListener() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView2.setImageBitmap(bitmap);
                textView2.setText("结果尺寸："+bitmap.getWidth()+"x"+bitmap.getHeight()+","+ RdfMathUtil.round((float)bitmap.getWidth()/bitmap.getHeight(),1));
            }
        });

        //imageLoader.display(imageView3,imageUrl3,1000, 667);
        imageLoader.download(imageUrl3, 300,200, new RdfImageLoader.OnImageDownloadListener() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView3.setImageBitmap(bitmap);
                textView3.setText("结果尺寸："+bitmap.getWidth()+"x"+bitmap.getHeight()+","+ RdfMathUtil.round((float)bitmap.getWidth()/bitmap.getHeight(),1));
            }
        });
        
    }
    

	@Override
	protected void onResume() {
		super.onResume();
	}
	
}



package com.adrdf.test.activity;

import java.io.File;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.adrdf.test.R;
import com.adrdf.base.util.RdfFileUtil;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.view.cropimage.RdfCropHelper;
import com.adrdf.base.view.cropimage.RdfCropImageView;

/**
 * Copyright © CapRobin
 *
 * Name：CropImageActivity
 * Describe：裁剪界面
 * Date：2018-03-07 11:55:28
 * Author: CapRobin@yeah.net
 *
 */
public class CropImageActivity extends Activity implements OnClickListener{
	private RdfCropImageView cropImageView;
	private Bitmap mBitmap;
	
	private RdfCropHelper cropHelper;
	
	private Button mSave;
	private Button mCancel,rotateLeft,rotateRight;

	private String mPath = null;
	public int screenWidth = 0;
	public int screenHeight = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        init();
    }
    @Override
    protected void onStop(){
    	super.onStop();
    	if(mBitmap!=null){
    		mBitmap=null;
    	}
    }
    
    private void init(){
    	getWindowWH();
    	mPath = getIntent().getStringExtra("PATH");
    	Log.d("TAG", "将要进行裁剪的图片的路径是 = " + mPath);
		cropImageView = (RdfCropImageView) findViewById(R.id.crop_image);
        mSave = (Button) this.findViewById(R.id.okBtn);
        mCancel = (Button) this.findViewById(R.id.cancelBtn);
        rotateLeft = (Button) this.findViewById(R.id.rotateLeft);
        rotateRight = (Button) this.findViewById(R.id.rotateRight);

        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);

        //相册中原来的图片
        File mFile = new File(mPath);
        try{
        	mBitmap = RdfFileUtil.getBitmapFromSD(mFile, RdfImageUtil.SCALEIMG,-1,-1);
            if(mBitmap==null){
            	Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
    			finish();
            }else{
            	resetImageView(mBitmap);
            }
        }catch (Exception e) {
        	Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
			finish();
		}
    }
    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}
    private void resetImageView(Bitmap b){
		cropImageView.clear();
		cropImageView.setImageBitmap(b);
		cropImageView.setImageBitmapResetBase(b, true);
        cropHelper = new RdfCropHelper(this, cropImageView);
        cropHelper.initCropBitmap(b);
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.cancelBtn:
    		finish();
    		break;
    	case R.id.okBtn:
            Bitmap bitmap = cropHelper.getCropBitmap(360,360);
            String fileName = "crop_"+new Random().nextInt(1000) + "-" + System.currentTimeMillis();
            String path = RdfFileUtil.getImageDownloadDir(this) +"/crop/"+fileName+".png";
   		    RdfFileUtil.writeBitmapToSD(path,bitmap, Bitmap.CompressFormat.PNG,70);
    		Intent intent = new Intent();
    		intent.putExtra("PATH", path);
    		setResult(RESULT_OK, intent);
    		finish();
    		break;
    	case R.id.rotateLeft:
            cropHelper.startRotate(270.f);
    		break;
    	case R.id.rotateRight:
            cropHelper.startRotate(90.f);
    		break;
    		
    	}
    }
   
}

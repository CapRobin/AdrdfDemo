package com.adrdf.test.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.adrdf.test.R;
import com.adrdf.base.app.camera.RdfCameraManager;
import com.adrdf.base.app.camera.RdfConfig;
import com.adrdf.base.util.RdfFileUtil;

import java.io.File;
import java.util.Random;
/**
 * Copyright © CapRobin
 *
 * Name：RecoderCameraActivity
 * Describe：
 * Date：2018-02-08 01:58:54
 * Author: CapRobin@yeah.net
 *
 */
public class RecoderCameraActivity extends Activity implements SurfaceHolder.Callback {

	/** UI相关. */
	private SurfaceView surfaceView = null;
	
	/** 拍照按钮. */
	private Button stopBtn;
	
	/** 控制相关. */
	private boolean hasSurface = false;
	
	/** 录制. */
	private MediaRecorder mRecorder;  
	
	/** 录制的文件. */
	private File videoFile;
	
	/**
	 * 开始.
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//屏幕参数
		int cameraId = this.getIntent().getIntExtra("cameraId", 0);
		int orientation = this.getIntent().getIntExtra("orientation", 0);
		RdfConfig.cameraId = cameraId;
		RdfConfig.orientation = orientation;
		
		//强制为横屏
		if(RdfConfig.orientation==0){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setContentView(R.layout.recoder_main);
		
		// 初始化 CameraManager
		RdfCameraManager.init(getApplication());
		
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = (SurfaceHolder)surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.setKeepScreenOn(true);
		
		//UI相关
		stopBtn = (Button)this.findViewById(R.id.stop_btn);
		
		stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("path", videoFile.getPath());
				setResult(RESULT_OK,intent); 
				finish();
			}
		});
	}

    /**
     * 打开相机.
     *
     * @param surfaceHolder the surface holder
     */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			RdfCameraManager manager = RdfCameraManager.get();
			Log.e("initCamera", "initCamera  相机界面初始化:"+ RdfConfig.cameraId);
			manager.openDriver(null, RdfConfig.cameraId);
			
			manager.startPreview();
			startRecorder();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * surfaceChanged.
	 * @param holder
	 * @param format
	 * @param width
	 * @param height
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//Log.e("surfaceChanged", "surfaceChanged  相机界面改变");
	}

	/**
	 * surfaceCreated.
	 * @param holder
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			initCamera(holder);
			hasSurface = true;
		}

	}

	/**
	 * surfaceDestroyed.
	 * @param holder
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}
	
	
	/**
	 * 暂停,将相机关闭.
	 */
	@Override
	protected void onPause() {
		stopRecorder();
		RdfCameraManager.get().stopPreview();
		RdfCameraManager.get().closeDriver();
		
		Log.e("onPause", "onPause  相机界面暂停");
		super.onPause();
	}
	
	/**
	 * 恢复.
	 */
	@Override
	protected void onResume() {
		
		//恢复相机
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		Log.e("hasSurface", "hasSurface："+hasSurface);
		if (hasSurface) {
			//SurfaceView存在就重新打开相机
			initCamera(surfaceHolder);
		} else {
			//SurfaceView不存在，重新设置surfaceHolder，同时SurfaceView会被重建
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			RdfCameraManager.get().startPreview();
			startRecorder();
		}
		//关键代码：恢复数据
		Log.e("onResume", "onResume  相机界面恢复");
		super.onResume();
		
	}
	
	/**
	 * 完成.
	 */
	@Override
	public void finish() {
		super.finish();
		Log.e("finish", "finish  相机界面释放");
	}
    
    
    public void startRecorder(){
		try {
			
			// 创建保存录制视频的视频文件
			String photoDir = RdfFileUtil.getImageDownloadDir(RecoderCameraActivity.this);
			String fileName = "video_"+new Random().nextInt(1000) + "-" + System.currentTimeMillis() + ".mp4";

			videoFile = new File(photoDir, fileName);

	        try {
				if(videoFile.exists()){
					videoFile.delete();
				}
	   
				if(!videoFile.getParentFile().exists()){
					videoFile.getParentFile().mkdirs();
				}
				if(!videoFile.exists()){
					videoFile.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 创建MediaPlayer对象  
	        RdfCameraManager.get().getCamera().unlock();
			mRecorder = new MediaRecorder();  
			mRecorder.reset();  
			mRecorder.setCamera(RdfCameraManager.get().getCamera());
			// 设置从麦克风采集声音(或来自录像机的声音AudioSource.CAMCORDER)  
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
			// 设置从摄像头采集图像  
			mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
			// 设置视频文件的输出格式  
			// 必须在设置声音编码格式、图像编码格式之前设置  
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
			// 设置声音编码的格式  
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
			// 设置图像编码的格式  
			mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
			 
			mRecorder.setOutputFile(videoFile.getAbsolutePath());  
			// 指定使用SurfaceView来预览视频
			mRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
			mRecorder.prepare();  
			// 开始录制  
			mRecorder.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void  stopRecorder(){
		try {
			if(mRecorder!=null){
				// 停止录制  
			    mRecorder.stop();  
			    // 释放资源  
			    mRecorder.release();  
			    mRecorder = null;  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
    
    /**
     * 
     * 释放资源.
     */
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
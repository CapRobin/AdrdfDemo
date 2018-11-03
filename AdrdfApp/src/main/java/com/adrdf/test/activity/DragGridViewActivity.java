package com.adrdf.test.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;

import com.adrdf.test.R;
import com.adrdf.test.adapter.DragPhotoGridViewAdapter;
import com.adrdf.test.global.MyApplication;
import com.adrdf.test.model.ImageUploadInfo;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.util.RdfAppUtil;
import com.adrdf.base.util.RdfDialogUtil;
import com.adrdf.base.util.RdfFileUtil;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfStrUtil;
import com.adrdf.base.util.RdfToastUtil;
import com.adrdf.base.util.RdfViewUtil;
import com.adrdf.base.view.draggrid.RdfDragGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright © CapRobin
 *
 * Name：DragGridViewActivity
 * Describe：
 * Date：2018-02-17 11:56:06
 * Author: CapRobin@yeah.net
 *
 */
public class DragGridViewActivity extends RdfBaseActivity {

	private MyApplication application;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;

	// 照相机拍照得到的图片
	private File currentPhotoFile;
	private String fileName;

    public RdfDragGridView dragGridView = null;
    private DragPhotoGridViewAdapter dragGridViewAdapter = null;
    private ArrayList<ImageUploadInfo> dragPhotoList = null;
    private ArrayList<String> photoListString = null;
    private int maxCount = 18;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_grid_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_drag_grid_view);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		application = (MyApplication) this.getApplication();
        dragPhotoList = new ArrayList<ImageUploadInfo>();
        photoListString = new ArrayList<String>();

		DisplayMetrics dm = RdfAppUtil.getDisplayMetrics(this);
		int width = ((dm.widthPixels-80)/3);


        //默认
        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(String.valueOf(R.drawable.cam_photo));
        imageUploadInfo.setCamBtn(true);
        dragPhotoList.add(imageUploadInfo);

        dragGridView = (RdfDragGridView)findViewById(R.id.drag_grid_view);
        dragGridViewAdapter = new DragPhotoGridViewAdapter(this,dragGridView,dragPhotoList,width-10,width-10,imageLoader);
        dragGridView.setAdapter(dragGridViewAdapter);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        dragGridView.setParentScrollView(scrollView);

        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageUploadInfo imageUploadInfo = dragPhotoList.get(position);
                if(imageUploadInfo.isCamBtn()){
                    View avatarView = View.inflate(DragGridViewActivity.this,R.layout.view_choose_avatar,null);
                    Button albumButton = (Button)avatarView.findViewById(R.id.choose_album);
                    Button camButton = (Button)avatarView.findViewById(R.id.choose_cam);
                    Button cancelButton = (Button)avatarView.findViewById(R.id.choose_cancel);
                    albumButton.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            RdfDialogUtil.removeDialog(DragGridViewActivity.this);
                            // 从相册中去获取
                            try {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                            } catch (Exception e) {
                                RdfToastUtil.showToast(DragGridViewActivity.this,"没有找到照片");
                            }
                        }

                    });

                    camButton.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            RdfDialogUtil.removeDialog(DragGridViewActivity.this);
                            doPickPhotoAction();
                        }

                    });

                    cancelButton.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            RdfDialogUtil.removeDialog(DragGridViewActivity.this);
                        }

                    });
                    RdfDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
                }else{
                    //点击了 其他的
                    //AbToastUtil.showToast(DragGridViewActivity.this,"2点击了：" + position);
                    photoListString.clear();
                    for(ImageUploadInfo imageUploadInfo1:dragPhotoList){
                        if(!imageUploadInfo1.isCamBtn()){
                            photoListString.add(imageUploadInfo1.getPath());
                        }
                    }

                    Intent intent = new Intent(DragGridViewActivity.this, ImageViewerActivity.class);
                    intent.putStringArrayListExtra("PATH",photoListString);
                    intent.putExtra("POSITION",position);
                    startActivity(intent);
                }
            }

        });
		
	}
	
	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			RdfToastUtil.showToast(DragGridViewActivity.this,"没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			fileName = "camera_"+new Random().nextInt(1000) + "-" + System.currentTimeMillis() + ".png";
            String photo_dir = RdfFileUtil.getImageDownloadDir(this);
			currentPhotoFile = new File(photo_dir, fileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			RdfToastUtil.showToast(DragGridViewActivity.this,"未找到系统相机程序");
		}
	}

	/**
	 * 因为调用了Camera和Gally所以要判断他们各自的返回情况,
	 * 他们启动时是这样的startActivityForResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK){
			return;
		}
		String currentFilePath = null;
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				Uri uri = intent.getData();
				currentFilePath = getPath(uri);

				RdfLogUtil.d(this, "从相册获取的图片的路径是 = " + currentFilePath);

				//限制最大6个
				if(dragGridViewAdapter.getCount() == maxCount){
					dragGridViewAdapter.setItem(dragGridViewAdapter.containCam(),new ImageUploadInfo(currentFilePath));
				}else{
                    int pos = dragGridViewAdapter.getCount()-1;
                    if(dragGridViewAdapter.containCam()==0){
                        pos = 0;
                    }
					dragGridViewAdapter.addItem(pos,new ImageUploadInfo(currentFilePath));
                    RdfViewUtil.setAbsListViewHeight(dragGridView,3,220,10);
                }

				break;
			case CAMERA_WITH_DATA:
				RdfLogUtil.d(this, "从拍照获取的图片的路径是 = " + currentPhotoFile.getPath());
				currentFilePath = currentPhotoFile.getPath();

				//限制最大6个
				if(dragGridViewAdapter.getCount() == maxCount){
					dragGridViewAdapter.setItem(dragGridViewAdapter.containCam(),new ImageUploadInfo(currentFilePath));
				}else{
					dragGridViewAdapter.addItem(dragGridViewAdapter.containCam(),new ImageUploadInfo(currentFilePath));
				}


				break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if(RdfStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}

        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
	}

}

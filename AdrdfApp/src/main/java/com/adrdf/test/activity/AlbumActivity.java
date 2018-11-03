package com.adrdf.test.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import com.adrdf.test.R;
import com.adrdf.test.adapter.AlbumGridAdapter;
import com.adrdf.test.global.MyApplication;
import com.adrdf.test.model.ImageInfo;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.asynctask.RdfTaskQueue;
import com.adrdf.base.http.RdfHttpUtil;
import com.adrdf.base.util.RdfAppUtil;
import com.adrdf.base.util.RdfToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：AlbumActivity
 * Describe：
 * Date：2017-06-27 11:47:04
 * Author: CapRobin@yeah.net
 *
 */
public class AlbumActivity extends RdfBaseActivity implements GridView.OnScrollListener{

	private MyApplication application;

	private RdfHttpUtil httpUtil;
	private Toolbar toolbar = null;
	private ArrayList<ImageInfo> imageInfos = null;
	private RdfTaskQueue task;

	private GridView gridView = null;
	private AlbumGridAdapter adapter = null;
	private int column = 3;
	private int lastState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
	private int currentPage = 0;
	private int pageSize = 60;

	private int firstVisiblePosition = 0;
	private int lastVisiblePosition = 0;

    //0  顶部  1中部  2 底部
    private int scrollPosition = 0;

    private boolean loading = false;
    private boolean hasMore = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setTitle(R.string.title_choose_album);
		toolbar.setContentInsetsRelative(0, 0);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		application = (MyApplication)this.getApplication();

		httpUtil = RdfHttpUtil.getInstance(this);

		task = RdfTaskQueue.newInstance();

		gridView = (GridView)this.findViewById(R.id.grid_view);
		imageInfos = new ArrayList<ImageInfo>();
		DisplayMetrics dm = RdfAppUtil.getDisplayMetrics(this);
		int width = (dm.widthPixels-25)/column;
		adapter = new AlbumGridAdapter(this,imageInfos,width,width,imageLoader);
		gridView.setAdapter(adapter);
		gridView.setOnScrollListener(this);
		adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0){
					Intent intent = new Intent(AlbumActivity.this, CaptureActivity.class);
					intent.putExtra("cameraId", 0);
					intent.putExtra("orientation", 0);
					startActivity(intent);
				}else{

					ArrayList<String> pathStringArray = new ArrayList<String>();

					for(ImageInfo imageInfo:imageInfos){
						pathStringArray.add(imageInfo.getPath());
					}

					Intent intent = new Intent(AlbumActivity.this, ImageViewerActivity.class);
					intent.putStringArrayListExtra("PATH",pathStringArray);
					intent.putExtra("POSITION",position);
					startActivity(intent);
				}

				RdfToastUtil.showToast(AlbumActivity.this,"pos:"+position+",path:"+imageInfos.get(position).getPath());
			}
		});

		//Android  版本大于6.0  要代码内申请权限
		RdfAppUtil.requestSDCardPermission(this);

		loadData(currentPage+1);
	}

	public void loadData(final int page){
        loading = true;
		final RdfTaskItem item = new RdfTaskItem();
		item.setListener(new RdfTaskObjectListener() {
			@Override
			public <T> T getObject() {
				List<ImageInfo> list = initData(false,page);
				return (T)list;
			}

			@Override
			public <T> void update(T t) {

				List<ImageInfo> list = (List<ImageInfo>)t;
                if(page == 1){
                    imageInfos.clear();
                }

                if(list == null || list.size() == 0){
                    hasMore = false;
                }else{
                    currentPage+=1;
                    imageInfos.addAll(list);
                    adapter.setFling(false);
                    adapter.notifyDataSetChanged();
                }

                loading = false;
			}
		});
		task.execute(item);
	}

	/**
	 * 初始化
	 */
	public List<ImageInfo>  initData(boolean thumbnails,int page){
		List<ImageInfo> pathList = new ArrayList<ImageInfo>();
		if(page ==1){
			ImageInfo image = new ImageInfo();
			image.setThumbnailsPath(String.valueOf(R.drawable.cam_photo));
			pathList.add(image);
		}

		if(!thumbnails){

			String[] projection = {MediaStore.Images.Media._ID,MediaStore.Images.Media.SIZE};

			Cursor cursor = this.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media._ID+ " DESC limit "+((page-1) * pageSize)+","+pageSize);

			int columnIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnSizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			while (cursor.moveToNext()) {
				int imageID = cursor.getInt(columnIDIndex);
				int size = cursor.getInt(columnSizeIndex);
				Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageID);
				String realPath = getRealFilePath(AlbumActivity.this,imageUri);
				//AbLogUtil.e("TAG","SIZE:"+size);
				if(new File(realPath).exists()){
					pathList.add(new ImageInfo(imageID,realPath,realPath));
				}

			}
			cursor.close();

		}else{

			//缩略图  手机兼容性问题严重  不可用
			String[] projection = {MediaStore.Images.Thumbnails._ID,MediaStore.Images.Thumbnails.IMAGE_ID};

			Cursor cursor = this.getContentResolver().query( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Thumbnails._ID+ " DESC limit "+((page-1) * pageSize)+","+pageSize);

			int columnIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int columnImageIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
			while (cursor.moveToNext()) {
				int imageID = cursor.getInt(columnIDIndex);
				int imageIDReal = cursor.getInt(columnImageIDIndex);
				Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
				pathList.add(new ImageInfo(imageIDReal,null,getRealFilePath(AlbumActivity.this,imageUri)));
			}
			cursor.close();

			for(ImageInfo image:pathList){
				String[] projection2 = {MediaStore.Images.Media._ID,MediaStore.Images.Media.SIZE};

				Cursor cursor2 = this.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection2, ""+MediaStore.Images.Media._ID+"= ? ", new String[]{""+image.getId()}, "");

				int columnIDIndex2 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
				int columnSizeIndex2 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
				if (cursor2.moveToFirst()) {
					int imageID2 = cursor2.getInt(columnIDIndex2);
					int size2 = cursor2.getInt(columnSizeIndex2);
					Uri imageUri2 = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageID2);
					String realPath2 = getRealFilePath(AlbumActivity.this,imageUri2);
					//AbLogUtil.e("TAG","SIZE:"+size);
					image.setPath(realPath2);

				}
				cursor.close();
			}

		}
		return pathList;

	}


	public String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_scan) {
			//扫描
			initData(false,1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
						 int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			//底部
			toolbar.setSubtitle("底部");

            //加载下一页
            if(!loading && hasMore && scrollPosition!=2 && currentPage!=0){
                loadData(currentPage+1);
                //AbLogUtil.e(AlbumActivity.class,"底部");
            }

            scrollPosition = 2;

		} else {
			toolbar.setSubtitle("中部");
            //AbLogUtil.e(AlbumActivity.class,"中部");
            scrollPosition = 1;
		}

		if (firstVisibleItem == 0) {
			toolbar.setSubtitle("顶部");
            //AbLogUtil.e(AlbumActivity.class,"顶部");
            scrollPosition = 0;
		}

	}


	public void onScrollStateChanged(final AbsListView view, int scrollState) {
		switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

				lastState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
				this.setTitle("Idle");

				//已经显示过了
				if(firstVisiblePosition == view.getFirstVisiblePosition()){
					return;
				}

				//已经显示过了
				if(lastVisiblePosition == view.getLastVisiblePosition()){
					return;
				}

				//从sroll切换到空闲状态不需要刷新界面
				if(lastState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					return;
				}

				//当前显示的 从多少到都少
				firstVisiblePosition = view.getFirstVisiblePosition();
				lastVisiblePosition = view.getLastVisiblePosition();


				//可见的数量，从0开始的View
				final int count = lastVisiblePosition - firstVisiblePosition + 1;

				//AbLogUtil.e("TAG","startPosition:"+startPosition +",endPosition:"+endPosition+",count:"+count);

				//当前显示的 从0-count,和scroll 会有重复 已经解决
				for (int i = 0; i <  count; i++) {
					final View  item = view.getChildAt(i);
					int realPosition = i+firstVisiblePosition;
					if(item!=null){
                        //AbLogUtil.e("TAG","realPosition:"+realPosition);
						AlbumGridAdapter.ViewHolder holder = (AlbumGridAdapter.ViewHolder) item.getTag();
						adapter.loadImage(realPosition,holder);
					}

				}

				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				//只能监听到开始滑动的那一次时间，其他的在onScroll中判断
				adapter.setFling(false);

				lastState = AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
				this.setTitle("Touch scroll");

				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

				adapter.setFling(true);

				lastState = AbsListView.OnScrollListener.SCROLL_STATE_FLING;
				this.setTitle("Fling");
				break;
		}
	}


}

package com.adrdf.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adrdf.test.R;
import com.adrdf.test.adapter.FuncListAdapter;
import com.adrdf.test.model.FuncMenu;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.util.RdfStrUtil;

import java.util.ArrayList;
/**
 * Copyright © CapRobin
 *
 * Name：FuncListActivity
 * Describe：
 * Date：2018-02-22 20:56:50
 * Author: CapRobin@yeah.net
 *
 */
public class FuncListActivity extends RdfBaseActivity {


    private ArrayList<FuncMenu> list = null;

    private int[] icons = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher

    };
    private String[] titles = new String[]{
            "Base Activity/fragment",
            "常用基本工具类",
            "浮动标题栏 AbScrollView",
            "AbSampleGirdView自动高度，无滚动条",
            "AbTabPager顶部滑动切换",
            "HTTP工具类的使用",
            "底部菜单栏切换",
            "图像选择器+ 多图预览（移动，放大，缩小）",
            "轮子选择器",
            "相册选择器",
            "SQlite DB  ORM",
            "图像显示ImageView",
            "内置的按钮样式参考",
            "字母筛选列表",
            "拍照录像 + JS调用",
            "拖拽的GridView",
            "下拉刷新，加载更多",
            "漂亮时钟",
            "浮动Tab - title"
    };

    private String[] intros = new String[]{
            "继承Base Activity/fragment可获得主题以及统一管理能力",
            "字符串，日期时间，文件，图像，JSON等一大堆工具类",
            "AbScrollView实现浮动标题栏",
            "自动展开，无滚动条，非常适合嵌入到页面内部使用",
            "Tablayout+ViewPager",
            "简单易于使用的Http工具类",
            "底部菜单栏切换",
            "多选图像选择器，相册，拍照选取 + 多图预览，移动，放大，缩小",
            "滑动轮子选择器，时间，日期,数据，字符串都可选择",
            "自定义相册选择器，有速度,读缓存，多选",
            "SQlite DB  ORM  关联关系 对象映射",
            "图像显示ImageView 下载，裁剪，缩放",
            "内置的按钮样式参考",
            "字母筛选列表()",
            "拍照录像 + JS调用",
            "拖拽的GridView",
            "下拉刷新,加载更多",
            "漂亮时钟",
            "浮动Tab - title"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_func_list);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view);
        list = new ArrayList<FuncMenu>();

        for (int i = 0; i < icons.length; i++) {
            FuncMenu menu = new FuncMenu();
            menu.setIcon(icons[i]);
            menu.setTitle(RdfStrUtil.strFormat2(String.valueOf(i + 1)) + " " + titles[i]);
            menu.setIntro(intros[i]);

            list.add(menu);
        }

        FuncListAdapter funcListAdapter = new FuncListAdapter(this, list);
        listView.setAdapter(funcListAdapter);
        // 获取数据
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FuncMenu menu = list.get(position);
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(FuncListActivity.this, BaseFragmentActivity.class);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:
                        intent = new Intent(FuncListActivity.this, FloatTitleActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(FuncListActivity.this, SampleGridViewActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(FuncListActivity.this, TabPagerActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(FuncListActivity.this, HttpActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(FuncListActivity.this, BottomBarActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(FuncListActivity.this, AddPhotoActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(FuncListActivity.this, WheelActivity.class);
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent(FuncListActivity.this, AlbumActivity.class);
                        startActivity(intent);
                        break;
                    case 10:
                        intent = new Intent(FuncListActivity.this, DBActivity.class);
                        startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent(FuncListActivity.this, ImageDownActivity.class);
                        startActivity(intent);
                        break;
                    case 12:
                        intent = new Intent(FuncListActivity.this, ButtonActivity.class);
                        startActivity(intent);
                        break;
                    case 13:
                        intent = new Intent(FuncListActivity.this, LetterFilterListActivity.class);
                        startActivity(intent);
                        break;
                    case 14:
                        intent = new Intent(FuncListActivity.this, WebCameraActivity.class);
                        startActivity(intent);
                        break;
                    case 15:
                        intent = new Intent(FuncListActivity.this, DragGridViewActivity.class);
                        startActivity(intent);
                        break;
                    case 16:
                        intent = new Intent(FuncListActivity.this, PullToRefreshActivity.class);
                        startActivity(intent);
                        break;
                    case 17:
                        intent = new Intent(FuncListActivity.this, AnalogClockActivity.class);
                        startActivity(intent);
                        break;
                    case 18:
                        intent = new Intent(FuncListActivity.this, FloatTabActivity.class);
                        startActivity(intent);
                        break;
                    case 99:
                        break;



                }

            }
        });


    }


}

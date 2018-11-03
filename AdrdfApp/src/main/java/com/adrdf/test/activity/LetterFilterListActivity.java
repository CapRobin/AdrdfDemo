package com.adrdf.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.adrdf.test.R;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.util.RdfCharacterUtil;
import com.adrdf.base.view.letterlist.RdfLetterFilterTwoListView;
import com.adrdf.base.view.listener.RdfOnItemSelectListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Copyright © CapRobin
 *
 * Name：LetterFilterListActivity
 * Describe：
 * Date：2018-02-21 11:57:50
 * Author: CapRobin@yeah.net
 *
 */
public class LetterFilterListActivity extends RdfBaseActivity {

	private MyApplication application;
	private RdfLetterFilterTwoListView mListView = null;

	private List<RdfSampleItem> groups;
	private List<ArrayList<RdfSampleItem>> childrens;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_letter_filter_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_letter_filter);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		application = (MyApplication) this.getApplication();

		mListView = (RdfLetterFilterTwoListView) this.findViewById(R.id.list_view);

        mListView.setItem1ResId(android.R.color.white,R.color.blue_translucent_light);
        mListView.setItem2ResId(android.R.color.white,R.color.blue_translucent_light);

        mListView.setOnItemSelectListener(new RdfOnItemSelectListener() {
            @Override
            public void onSelect(int position1, int position2) {
                if(position2!=-1){
                    Intent intent = getIntent();
                    intent.putExtra("GROUP_ID",groups.get(position1).getId());
                    intent.putExtra("GROUP_NAME",groups.get(position1).getText());
                    intent.putExtra("CHILD_ID",childrens.get(position1).get(position2).getId());
                    intent.putExtra("CHILD_NAME",childrens.get(position1).get(position2).getText());
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

        TextView allTypeText = (TextView) this.findViewById(R.id.all_type);
        allTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = getIntent();
                intent.putExtra("GROUP_ID","-1");
                intent.putExtra("GROUP_NAME","-1");
                intent.putExtra("CHILD_ID","-1");
                intent.putExtra("CHILD_NAME","-1");
                setResult(RESULT_OK,intent);
                finish();*/

                initData2();
            }
        });

        initData();

	}

    public void initData(){

        //所有分类组
        groups = new ArrayList<RdfSampleItem>();

        //所有子分类
        childrens = new ArrayList<ArrayList<RdfSampleItem>>();

        String [] texts = new String[] {"阿三", "北边", "长江","大地","饿了","发了","谷歌","喝了","i","加法","卡卡","乐乐","妈妈","娜娜","欧了","怕怕","琪琪","热热","死了","提提","u","v","旺旺","嘻嘻","咋咋"};
        for(int i = 0;i<100;i++){
            groups.add(new RdfSampleItem(String.valueOf(i),texts[new Random().nextInt(25)]));
        }


        for(int i = 0;i<groups.size();i++){
            ArrayList<RdfSampleItem>  child1 = new  ArrayList<RdfSampleItem>();
            child1.add(new RdfSampleItem(String.valueOf(i*10),"城市"+String.valueOf(i*10),"Z"));
            child1.add(new RdfSampleItem(String.valueOf(i*10),"城市"+String.valueOf(i*10),"Z"));
            childrens.add(child1);
        }

        groups = filledData(groups);

        mListView.addGroups(groups);
        mListView.addChildrens(childrens);

        mListView.notifyDataSetChanged();

        mListView.setDefaultSelect(0,0);
    }

    public void initData2(){

        //清除只能调用
        mListView.clearAllItems();

        //所有分类组
        groups = new ArrayList<RdfSampleItem>();

        //所有子分类
        childrens = new ArrayList<ArrayList<RdfSampleItem>>();

        String [] texts = new String[] {"阿三", "北边"};
        groups.add(new RdfSampleItem(String.valueOf(1),texts[0]));
        groups.add(new RdfSampleItem(String.valueOf(2),texts[1]));


        for(int i = 0;i<groups.size();i++){
            ArrayList<RdfSampleItem>  child1 = new  ArrayList<RdfSampleItem>();
            for(int j = 0;j<50;j++){
                child1.add(new RdfSampleItem(String.valueOf(i*10+j),"城市"+String.valueOf(i*10+j),"Z"));
            }
            childrens.add(child1);
        }

        groups = filledData(groups);

        //
        mListView.addGroups(groups);
        mListView.addChildrens(childrens);

        mListView.notifyDataSetChanged();

        mListView.setDefaultSelect(0,0);
    }

    /**
     * 为ListView填充数据
     * @return
     */
    private List<RdfSampleItem> filledData(List<RdfSampleItem> groups){
        List<RdfSampleItem> newList = new ArrayList<RdfSampleItem>();
        //实例化汉字转拼音类
        for(int i=0; i<groups.size(); i++){
            RdfSampleItem item = new RdfSampleItem();
            item.setText(groups.get(i).getText());
            //汉字转换成拼音
            String pinyin = RdfCharacterUtil.getSelling(groups.get(i).getText());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                item.setFirstLetter(sortString.toUpperCase());
            }else{
                item.setFirstLetter("#");
            }
            newList.add(item);
        }
        Collections.sort(newList);
        return newList;

    }

}

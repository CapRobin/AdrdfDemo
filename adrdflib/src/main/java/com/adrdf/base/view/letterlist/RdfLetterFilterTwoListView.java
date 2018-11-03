package com.adrdf.base.view.letterlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;

import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.view.listener.RdfOnItemClickListener;
import com.adrdf.base.view.listener.RdfOnItemSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfLetterFilterTwoListView
 * Describe：两个个列表  字母过滤在第一个列表
 * Date：2017-09-22 17:16:06
 * Author: CapRobin@yeah.net
 *
 */
public class RdfLetterFilterTwoListView extends RelativeLayout {

    /** 上下文. */
    private Context context;

    private ListView listView1;
	private ListView listView2;

	private RdfLetterGroupListAdapter listViewAdapter1;
	private RdfLetterChildListAdapter listViewAdapter2;

    /**组数据*/
	private List<RdfSampleItem> groups;

    /**子类数据*/
	private List<ArrayList<RdfSampleItem>> childrens;

	/**当前显示的子类*/
	private List<RdfSampleItem> childrenItem = new ArrayList<RdfSampleItem>();

	private RdfOnItemSelectListener onItemSelectListener;

    /**当前选中*/
    private int  currentGroupPosition = -1;
    private int  currentChildPosition = -1;

    /** 字母表. */
    private RdfLetterView letterView;

    public RdfLetterFilterTwoListView(Context context) {
        super(context);
        //所有分类组
        this.groups = new ArrayList<RdfSampleItem>();
        //所有子分类
        this.childrens = new ArrayList<ArrayList<RdfSampleItem>>();
        init(context);
    }

	public RdfLetterFilterTwoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        //所有分类组
        this.groups = new ArrayList<RdfSampleItem>();
        //所有子分类
        this.childrens = new ArrayList<ArrayList<RdfSampleItem>>();
		init(context);
	}

    public RdfLetterFilterTwoListView(Context context, List<RdfSampleItem> groups, List<ArrayList<RdfSampleItem>> childrens) {
        super(context);
        this.groups = groups;
        this.childrens = childrens;
        init(context);
    }



    /**
	 * 根据ID显示指定的选项
     * @param groupId
	 * @param childId
     */
    public void setDefaultSelect(String groupId, String childId) {
		if (groupId.equals("-1") || childId.equals("-1") ) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getId().equals(groupId)) {
				listViewAdapter1.setSelectedPosition(i);
				childrenItem.clear();
				if (i < childrens.size()) {
					childrenItem.addAll(childrens.get(i));
				}
                currentGroupPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).getId().equals(childId)) {
				listViewAdapter2.setSelectedPosition(j);
                currentChildPosition = j;
				break;
			}
		}
		
		setDefaultSelect(currentGroupPosition,currentChildPosition);
	}


	/**
	 * 初始化
	 * @param context
     */
	private void init(Context context) {
        this.context = context;

		this.setBackgroundResource(android.R.color.white);

        LinearLayout  linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        listView1 = new ListView(context);
        listView1.setCacheColorHint(Color.parseColor("#00000000"));
        listView1.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
        listView1.setDividerHeight(1);
        listView1.setHorizontalScrollBarEnabled(false);
        listView1.setVerticalScrollBarEnabled(false);
        listView1.setBackgroundResource(android.R.color.white);

        listView2 = new ListView(context);
        listView2.setCacheColorHint(Color.parseColor("#00000000"));
        listView2.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
        listView2.setDividerHeight(1);
        listView2.setHorizontalScrollBarEnabled(false);
        listView2.setVerticalScrollBarEnabled(false);
        listView2.setBackgroundResource(android.R.color.white);

        linearLayout.addView(listView1,new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1 ));

        View line = new View(context);
        line.setBackgroundColor(Color.parseColor("#ebebeb"));

        linearLayout.addView(line,new LinearLayout.LayoutParams(1,LayoutParams.MATCH_PARENT));

        linearLayout.addView(listView2,new LinearLayout. LayoutParams(0,LayoutParams.MATCH_PARENT,1 ));

        this.addView(linearLayout,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));


        letterView = new RdfLetterView(context);

        LayoutParams layoutParams = new LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        layoutParams.topMargin=10;
        layoutParams.rightMargin=10;
        layoutParams.bottomMargin=10;
        this.addView(letterView,layoutParams);

        letterView.setOnItemSelectListener(new RdfOnItemSelectListener() {
            @Override
            public void onSelect(int position) {
                if (listView1.getAdapter() != null) {
					SectionIndexer sectionIndexter = null;
					if(listView1.getHeaderViewsCount()>0){
						HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView1.getAdapter();
						sectionIndexter = (SectionIndexer) listAdapter.getWrappedAdapter();
					}else{
						sectionIndexter = (SectionIndexer) listView1.getAdapter();

					}
					int positionList = sectionIndexter.getPositionForSection(letterView.getLetter(position));
					listView1.setSelection(positionList);
                }
            }
        });

		listViewAdapter1 = new RdfLetterGroupListAdapter(context, groups);

		listView1.setAdapter(listViewAdapter1);
		listViewAdapter1.setOnItemClickListener(new RdfOnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				currentGroupPosition = position;
				if (onItemSelectListener != null) {
					onItemSelectListener.onSelect(currentGroupPosition,-1);
				}

				if (position < groups.size()) {

					if(childrens.size() > position){
						listViewAdapter2.addAllItems(childrens.get(position));
					}
					listViewAdapter2.notifyDataSetChanged();
				}
			}
		});

        if(groups.size()>0){
            listViewAdapter1.setSelectedPositionNoNotify(0);
            childrenItem.addAll(childrens.get(0));
        }

	    listViewAdapter2 = new RdfLetterChildListAdapter(context, childrenItem);
	    listViewAdapter2.setSelectedPositionNoNotify(0);
	    listView2.setAdapter(listViewAdapter2);
	    listViewAdapter2.setOnItemClickListener(new RdfOnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				currentChildPosition = position;
				if (onItemSelectListener != null) {
					onItemSelectListener.onSelect(currentGroupPosition,currentChildPosition);
				}

			}
		});

        if(childrenItem.size()>0) {
            setDefaultSelect(0, 0);
        }

	}

	/**
	 * 设置默认列表的选择
	 */
	public void setDefaultSelect(int positionGroup,int positionChild) {
        listViewAdapter1.setSelectedPosition(positionGroup);

        if (positionGroup < groups.size()) {
            childrenItem.clear();
            if(childrens.size() > positionGroup){
                childrenItem.addAll(childrens.get(positionGroup));
            }
            listViewAdapter2.notifyDataSetChanged();
        }

        listViewAdapter2.setSelectedPosition(positionChild);

        currentGroupPosition = positionGroup;
        currentChildPosition = positionChild;
	}

    /**
     * 当前选择的项的文本
     * @return
     */
	public String getCurrentText() {
		return childrenItem.get(currentChildPosition).getText();
	}

	/**
	 * 设置点击的监听器
	 * @param onItemSelectListener
     */
	public void setOnItemSelectListener(RdfOnItemSelectListener onItemSelectListener) {
		this.onItemSelectListener = onItemSelectListener;
	}

	/**
	 * 更新全部数据
	 */
	public void notifyDataSetChanged(){
		notifyDataSetChangedGroup();
		notifyDataSetChangedChildren();
	}

	public void notifyDataSetChangedGroup(){
		 listViewAdapter1.notifyDataSetChanged();
	}
	
	public void notifyDataSetChangedChildren(){
		 listViewAdapter2.notifyDataSetChanged();
	}

	/**
	 * 设置组条目的背景资源
	 * @param item1DefaultResId
	 * @param item1SelectedResId
	 */
	public void setItem1ResId(int item1DefaultResId,int item1SelectedResId){
		listViewAdapter1.setItemDefaultResId(item1DefaultResId);
		listViewAdapter1.setItemSelectedResId(item1SelectedResId);
	}

	/**
	 * 设置子类列表条目的背景资源
	 * @param item2DefaultResId
	 * @param item2SelectedResId
	 */
	public void setItem2ResId(int item2DefaultResId,int item2SelectedResId){
		listViewAdapter2.setItemDefaultResId(item2DefaultResId);
		listViewAdapter2.setItemSelectedResId(item2SelectedResId);
	}

	/**
	 * 添加分组数据
	 * @param items
     */
	public void addGroups(List<RdfSampleItem> items) {
		 groups.addAll(items);
	}

	/**
	 * 添加子类数据
	 * @param items
	 */
	public void addChildrens(List<ArrayList<RdfSampleItem>> items){
		 childrens.addAll(items);
	}

    /**
     * 当前选中的组位置
     */
    public int getCurrentGroupPosition() {
        return currentGroupPosition;
    }

    /**
     * 当前选中的子类位置
     */
    public int getCurrentChildPosition() {
        return currentChildPosition;
    }


	public RdfLetterGroupListAdapter getGroupListViewAdapter() {
		return listViewAdapter1;
	}


	public RdfLetterChildListAdapter getChildListViewAdapter() {
		return listViewAdapter2;
	}

	public void  clearAllItems(){
		listViewAdapter1.clearAllItems();
        childrens.clear();
		listViewAdapter2.clearAllItems();
	}

}

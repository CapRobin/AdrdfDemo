package com.adrdf.base.view.sample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adrdf.library.R;
import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.view.listener.RdfOnItemClickListener;
import com.adrdf.base.view.listener.RdfOnItemSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTwoListView
 * Describe：双列联动的ListView
 * Date：2017-04-20 11:27:28
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTwoListView extends LinearLayout{

	private ListView listView1;
	private ListView listView2;

	private AbItemSelectedAdapter listViewAdapter1;
	private AbItemSelectedAdapter listViewAdapter2;

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


    public RdfTwoListView(Context context) {
        super(context);
        init(context);
    }

	public RdfTwoListView(Context context, List<RdfSampleItem> groups, List<ArrayList<RdfSampleItem>> childrens) {
		super(context);
		this.groups = groups;
		this.childrens = childrens;
		init(context);
	}

	public RdfTwoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	/**
	 * 显示指定的选项
	 * @param groupId
	 * @param childId
	 */
	public void updateCurrent(String groupId, String childId) {
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

        if(this.groups==null){
            //所有分类组
            this.groups = new ArrayList<RdfSampleItem>();
            //所有子分类
            this.childrens = new ArrayList<ArrayList<RdfSampleItem>>();
        }


		this.setBackgroundResource(android.R.color.white);
		this.setOrientation(LinearLayout.HORIZONTAL);
		listView1 = new ListView(context);
		listView1.setCacheColorHint(Color.parseColor("#00000000"));
		listView1.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView1.setDividerHeight(1);
		listView1.setHorizontalScrollBarEnabled(false);
		listView1.setVerticalScrollBarEnabled(false);
		listView1.setBackgroundResource(android.R.color.white);
		this.addView(listView1,new LayoutParams(0,LayoutParams.MATCH_PARENT,1));
		
		View line = new View(context);
		line.setBackgroundColor(Color.parseColor("#ebebeb"));
		this.addView(line,new LayoutParams(1,LayoutParams.MATCH_PARENT));
		
		listView2 = new ListView(context);
		listView2.setCacheColorHint(Color.parseColor("#00000000"));
		listView2.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView2.setDividerHeight(1);
		listView2.setHorizontalScrollBarEnabled(false);
		listView2.setVerticalScrollBarEnabled(false);
		listView2.setBackgroundResource(android.R.color.white);
		this.addView(listView2,new LayoutParams(0,LayoutParams.MATCH_PARENT,1 ));

		
		listViewAdapter1 = new AbItemSelectedAdapter(context, groups);

		listView1.setAdapter(listViewAdapter1);
		listViewAdapter1.setOnItemClickListener(new RdfOnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				currentGroupPosition = position;
				if (onItemSelectListener != null) {
					onItemSelectListener.onSelect(currentGroupPosition,-1);
				}

				if (position < groups.size()) {
					childrenItem.clear();
					if(childrens.size() > position){
						childrenItem.addAll(childrens.get(position));
					}
					listViewAdapter2.notifyDataSetChanged();
				}
			}
		});

        if(groups.size()>0){
            listViewAdapter1.setSelectedPositionNoNotify(0);
            childrenItem.addAll(childrens.get(0));
        }

	    listViewAdapter2 = new AbItemSelectedAdapter(context, childrenItem);
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
		listView1.setSelection(positionGroup);
		listView2.setSelection(positionChild);
        currentGroupPosition = positionGroup;
        currentChildPosition = positionChild;
		if (onItemSelectListener != null) {
			onItemSelectListener.onSelect(currentGroupPosition,currentChildPosition);
		}
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


	/**
	 * Copyright © CapRobin
	 *
	 * Name：RdfTwoListView
	 * Describe：有选择样式的纯文本Adapter
	 * Date：2017-04-23 18:43:50
	 * Author: CapRobin@yeah.net
	 *
	 */
	public class AbItemSelectedAdapter extends ArrayAdapter<RdfSampleItem> {

		private Context context;
		private List<RdfSampleItem> list;
		private int selectedPos = 0;
		private int itemDefaultResId;
		private int itemSelectedResId;
		private OnClickListener onClickListener;
		private RdfOnItemClickListener onItemClickListener;

		public AbItemSelectedAdapter(Context context, List<RdfSampleItem> list) {
			super(context, -1, list);
			this.context = context;
			this.list = list;
			init();
		}

		private void init() {
			onClickListener = new OnClickListener() {

				@Override
				public void onClick(View view) {
					selectedPos = (Integer) view.getTag();
					setSelectedPosition(selectedPos);
					if (onItemClickListener != null) {
						onItemClickListener.onItemClick(view, selectedPos);
					}
				}
			};
		}


		/**
		 * 设置选中的position,并通知列表刷新
		 */
		public void setSelectedPosition(int pos) {
			if (list != null && pos < list.size()) {
				selectedPos = pos;
				notifyDataSetChanged();
			}

		}

		/**
		 * 设置选中的position,但不通知刷新
		 */
		public void setSelectedPositionNoNotify(int pos) {
			selectedPos = pos;
		}

		/**
		 * 获取选中的position
		 */
		public int getSelectedPosition() {
			if (list != null && selectedPos < list.size()) {
				return selectedPos;
			}

			return -1;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.item_sample_text, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}

			final RdfSampleItem item = list.get(position);

			holder.text.setText(item.getText());

			if (selectedPos == position) {
				//设置选中的背景图片
				holder.text.setBackgroundResource(itemSelectedResId);
			} else {
				//设置未选中状态背景图片
				holder.text.setBackgroundResource(itemDefaultResId);
			}
			holder.text.setOnClickListener(onClickListener);

			return convertView;
		}

		public void setOnItemClickListener(RdfOnItemClickListener onItemClickListener) {
			this.onItemClickListener = onItemClickListener;
		}


		/**
		 * 设置列表条目默认的背景资源
		 * @param itemDefaultResId
		 */
		public void setItemDefaultResId(int itemDefaultResId) {
			this.itemDefaultResId = itemDefaultResId;
		}

		/**
		 * 设置列表条目被选择的背景资源
		 * @param itemSelectedResId
		 */
		public void setItemSelectedResId(int itemSelectedResId) {
			this.itemSelectedResId = itemSelectedResId;
		}

		class ViewHolder {
			TextView text;
		}
	}
}

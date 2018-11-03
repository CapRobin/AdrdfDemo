package com.adrdf.base.view.letterlist;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.view.listener.RdfOnItemClickListener;


public class RdfLetterChildListAdapter extends ArrayAdapter<RdfSampleItem> {

	private Context context;
	private List<RdfSampleItem> list;
	private int selectedPos = 0;
	private int itemDefaultResId;
	private int itemSelectedResId;
	private OnClickListener onClickListener;
	private RdfOnItemClickListener onItemClickListener;

	public RdfLetterChildListAdapter(Context context, List<RdfSampleItem> list) {
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
		TextView view;
		if (convertView == null) {
			view = new TextView(context);
			view.setTextColor(Color.BLACK);
			view.setTextSize(13);
		} else {
			view = (TextView) convertView;
		}
		view.setTag(position);
		view.setBackgroundResource(itemDefaultResId);
		String mString = "";
		if (list != null) {
			if (position < list.size()) {
				mString = list.get(position).getText();
			}
		}

		view.setText(mString);

		if (selectedPos == position) {
			//设置选中的背景图片
			view.setBackgroundResource(itemSelectedResId);
		} else {
			//设置未选中状态背景图片
			view.setBackgroundResource(itemDefaultResId);
		}
		view.setPadding(20, 25, 20, 25);
		view.setOnClickListener(onClickListener);
		return view;
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

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void addAllItems(List<RdfSampleItem> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	/**
	 * 清空当前数据
	 */
	public void clearAllItems(){
		this.list.clear();
		notifyDataSetChanged();
	}
}
package com.adrdf.base.view.letterlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.adrdf.library.R;
import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.view.listener.RdfOnItemClickListener;

import java.util.List;


public class RdfLetterGroupListAdapter extends BaseAdapter implements SectionIndexer{
	private List<RdfSampleItem> list = null;
	private Context context;
	private RdfOnItemClickListener onItemClickListener;
    private int itemDefaultResId;
    private int itemSelectedResId;
    private int selectedPos = 0;
    private String selectedText;

	public RdfLetterGroupListAdapter(Context context, List<RdfSampleItem> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final RdfSampleItem sampleItem = list.get(position);
		if (convertView == null) { 
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_letter_group_list, null);
			viewHolder.letterText = (TextView) convertView.findViewById(R.id.letter_text);
			viewHolder.itemText = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.itemTextLayout = (RelativeLayout) convertView.findViewById(R.id.item_text_layout);
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.letterText.setVisibility(View.VISIBLE);
			viewHolder.letterText.setText(sampleItem.getFirstLetter());
		}else{
			viewHolder.letterText.setVisibility(View.GONE);
		}
		
		viewHolder.itemText.setText(sampleItem.getText());

        viewHolder.itemTextLayout.setBackgroundResource(itemDefaultResId);

        if (selectedText != null && selectedText.equals(sampleItem.getText())) {
            //设置选中的背景图片
            viewHolder.itemTextLayout.setBackgroundResource(itemSelectedResId);
        } else {
            //设置未选中状态背景图片
            viewHolder.itemTextLayout.setBackgroundResource(itemDefaultResId);
        }
        viewHolder.itemTextLayout.setClickable(true);
        viewHolder.itemTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                setSelectedPosition(selectedPos);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, selectedPos);
                }
            }
        });

		return convertView;

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
	

	final static class ViewHolder {
		TextView letterText;
		TextView itemText;
        RelativeLayout itemTextLayout;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getFirstLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getFirstLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * @param text
	 * @return
	 */
	private String getFirstLetter(String text) {
		String  sortStr = text.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
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
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (list != null && pos < list.size()) {
            selectedPos = pos;
            selectedText = list.get(pos).getText();
            notifyDataSetChanged();
        }

    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (list != null && pos < list.size()) {
            selectedText = list.get(pos).getText();
        }
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

}
package com.adrdf.test.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.base.app.model.RdfSampleItem;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：SampleTextAdapter
 * Describe：只是文本的适配器，没什么
 * Date：2017-11-27 18:50:39
 * Author: CapRobin@yeah.net
 *
 */
public class SampleTextAdapter extends BaseAdapter {
	private Context context;
	private List<RdfSampleItem> data;

	public SampleTextAdapter(Context context, List<RdfSampleItem> data) {
		super();
		this.context = context;
		this.data = data;
	}

	public List<RdfSampleItem> getData() {
		return data;
	}

	public void setData(List<RdfSampleItem> data) {
		this.data = data;
	}


	public SampleTextAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_grid_text, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RdfSampleItem item = data.get(position);

		holder.text.setText(item.getText());
		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}

}

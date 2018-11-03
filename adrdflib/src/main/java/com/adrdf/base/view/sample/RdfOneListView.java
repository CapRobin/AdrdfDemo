package com.adrdf.base.view.sample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.adrdf.base.view.listener.RdfOnItemClickListener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOneListView
 * Describe：一个普通的ListView,懒得写布局了,场景就是直接new
 * Date：2017-02-17 11:24:12
 * Author: CapRobin@yeah.net
 *
 */
public class RdfOneListView extends RelativeLayout{

	private Context context;
	private ListView listView;
	private RdfOnItemClickListener onItemClickListener;


	public RdfOneListView(Context context) {
		super(context);
		init(context);
	}

	public RdfOneListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public RdfOneListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

		listView = new ListView(context);
		listView.setCacheColorHint(Color.parseColor("#00000000"));
		listView.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView.setDividerHeight(1);
		listView.setBackgroundResource(android.R.color.white);
		this.addView(listView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });

	}

	public void setOnItemClickListener(RdfOnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ListView getListView() {
		return listView;
	}

    public void setAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setSelector(int resID) {
        listView.setSelector(resID);
    }

}

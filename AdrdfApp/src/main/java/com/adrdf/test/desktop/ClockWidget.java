package com.adrdf.test.desktop;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.adrdf.test.R;
/**
 * Copyright © CapRobin
 *
 * Name：ClockWidget
 * Describe：
 * Date：2018-03-215 14:45:38
 * Author: CapRobin@yeah.net
 *
 */
public class ClockWidget extends AppWidgetProvider {
	

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_clock);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	

}

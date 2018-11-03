package com.adrdf.test.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.adrdf.test.R;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.view.scene.RdfAnalogClock;

/**
 * Copyright © CapRobin
 *
 * Name：AnalogClockActivity
 * Describe：
 * Date：2018-03-17 01:54:23
 * Author: CapRobin@yeah.net
 *
 */
public class AnalogClockActivity extends RdfBaseActivity {
	
	private MyApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock);
        application = (MyApplication)this.getApplication();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setTitle(R.string.title_clock);
		toolbar.setContentInsetsRelative(0, 0);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
		Drawable dial = this.getResources().getDrawable(R.drawable.clock_dial);
		Drawable hourHand = this.getResources().getDrawable(R.drawable.clock_hour);
		Drawable minuteHand = this.getResources().getDrawable(R.drawable.clock_minute);
		Drawable secondHand = this.getResources().getDrawable(R.drawable.clock_second);
		RdfAnalogClock analogClock = new RdfAnalogClock(this);
		analogClock.setDialDrawable(dial);
		analogClock.setHourDrawable(hourHand);
		analogClock.setMinuteDrawable(minuteHand);
		analogClock.setSecondDrawable(secondHand);
        
		LinearLayout contentLayout = (LinearLayout)this.findViewById(R.id.contentLayout);
		contentLayout.addView(analogClock);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}



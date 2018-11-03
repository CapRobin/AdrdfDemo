package com.adrdf.test.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseFragment;
/**
 * Copyright © CapRobin
 *
 * Name：TestFragment
 * Describe：
 * Date：2018-03-15 15:46:26
 * Author: CapRobin@yeah.net
 *
 */
public class TestFragment extends RdfBaseFragment {

    MyApplication application;
    TextView view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = new TextView(this.getActivity());
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        application = (MyApplication) this.getActivity().getApplication();
        view.setText("页面");
        view.setGravity(Gravity.CENTER);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}

package com.adrdf.base.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
/**
 * Copyright © CapRobin
 *
 * Name：RdfAlertDialogFragment
 * Describe：弹出框fragment
 * Date：2016-12-01 19:13:37
 * Author: CapRobin@yeah.net
 *
 */
public class RdfAlertDialogFragment extends DialogFragment {

	private View contentView;

	/**
	 * 构造函数.
	 */
	public RdfAlertDialogFragment() {
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		if(contentView!=null){
			builder.setView(contentView);
		}
		
	    return builder.create();
	}

	public View getContentView() {
		return contentView;
	}

	public void setContentView(View contentView) {
		this.contentView = contentView;
	}

}

package com.adrdf.base.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
/**
 * Copyright © CapRobin
 *
 * Name：RdfProgressDialogFragment
 * Describe：进度框fragment
 * Date：2017-09-19 11:14:19
 * Author: CapRobin@yeah.net
 *
 */
public class RdfProgressDialogFragment extends DialogFragment {


	/**
	 * 创建一个新的AbProgressDialogFragment.
	 * @param indeterminateDrawable  进度图片
	 * @param message  消息提示
     * @return
     */
	public static RdfProgressDialogFragment newInstance(int indeterminateDrawable, String message) {
		RdfProgressDialogFragment f = new RdfProgressDialogFragment();
		Bundle args = new Bundle();
		args.putInt("indeterminateDrawable", indeterminateDrawable);
		args.putString("message", message);
		f.setArguments(args);

		return f;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int indeterminateDrawable = getArguments().getInt("indeterminateDrawable");
		String message = getArguments().getString("message");
		
		ProgressDialog progressDialog = new ProgressDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		if(indeterminateDrawable > 0){
			progressDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(indeterminateDrawable));
		}
		
		if(message != null){
			progressDialog.setMessage(message);
		}
		
	    return progressDialog;
	}
	
}

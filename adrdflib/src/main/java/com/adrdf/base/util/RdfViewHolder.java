package com.adrdf.base.util;

import android.util.SparseArray;
import android.view.View;
/**
 * Copyright © CapRobin
 *
 * Name：RdfViewHolder
 * Describe：超简洁的ViewHolder
 * Date：2017-03-21 01:10:39
 * Author: CapRobin@yeah.net
 *
 */
public class RdfViewHolder {
    
    /**
     * ImageView view = AbViewHolder.get(convertView, R.id.imageView);
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}

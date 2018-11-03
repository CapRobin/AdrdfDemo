package com.adrdf.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.adrdf.test.R;
import com.adrdf.test.model.ImageUploadInfo;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.asynctask.RdfTaskQueue;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.util.RdfStrUtil;
import com.adrdf.base.util.RdfViewUtil;
import com.adrdf.base.view.draggrid.RdfDragGridView;
import com.adrdf.base.view.draggrid.RdfDragGridViewAdapter;

import java.io.File;
import java.util.List;
/**
 * Copyright © CapRobin
 *
 * Name：DragPhotoGridViewAdapter
 * Describe：
 * Date：2018-02-17 12:00:31
 * Author: CapRobin@yeah.net
 *
 */
public class DragPhotoGridViewAdapter extends RdfDragGridViewAdapter {

    /** 上下问. */
    private Context context;

    private RdfDragGridView dragGridView;

    /** 图片的路径. */
    private List<ImageUploadInfo> images = null;

    /** 图片宽度. */
    private int width;

    /** 图片高度. */
    private int height;

    /**图片下载器*/
    private RdfImageLoader imageLoader = null;
    private RdfTaskQueue task;
    private AdapterView.OnItemClickListener onItemClickListener;

    public DragPhotoGridViewAdapter(Context context, RdfDragGridView dragGridView, List<ImageUploadInfo> images, int width, int height, RdfImageLoader imageLoader) {
        this.context = context;
        this.images = images;
        this.width = width;
        this.height = height;
        this.dragGridView = dragGridView;
        this.imageLoader = imageLoader;
        this.task = RdfTaskQueue.newInstance();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public ImageUploadInfo getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_drag_image, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.deleteBtn =  (Button) convertView.findViewById(R.id.delete_btn);
            holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_layout);
            holder.parentLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ImageUploadInfo imageUploadInfo = images.get(position);
        if(imageUploadInfo.isCamBtn()){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }else{
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
        holder.deleteBtn.setFocusable(false);
        holder.imageView.setFocusable(false);
        /*holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(null,view,position,position);
                }
            }
        });*/

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                images.remove(position);
                //限制最大6个
                if(containCam()==-1){
                    ImageUploadInfo imageUploadInfo1 =  new ImageUploadInfo(String.valueOf(R.drawable.cam_photo));
                    imageUploadInfo1.setCamBtn(true);
                    addItem(getCount(),imageUploadInfo1);
                }

                RdfViewUtil.setAbsListViewHeight(dragGridView,3,220,10);
                notifyDataSetChanged();
            }
        });

        holder.imageView.setImageBitmap(null);
        //hide时隐藏Text
        if(position != hidePosition) {
            loadImage(position,holder);
        }else{
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }

        convertView.setId(position);

        return convertView;
    }

    public void removeView(int pos) {
        images.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if(draggedPos < destPos) {
            images.add(destPos+1, getItem(draggedPos));
            images.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if(draggedPos > destPos) {
            images.add(destPos, getItem(draggedPos));
            images.remove(draggedPos+1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }

    /**
     * 增加并改变视图.
     * @param position the position
     * @param imageUploadInfo the image paths
     */
    public void addItem(int position,ImageUploadInfo imageUploadInfo) {
        images.add(position,imageUploadInfo);
        notifyDataSetChanged();
    }

    /**
     * 增加多条并改变视图.
     * @param imageUploadInfos the image paths
     */
    public void addItems(List<ImageUploadInfo> imageUploadInfos) {
        images.addAll(imageUploadInfos);
        notifyDataSetChanged();
    }

    /**
     * 改变视图.
     * @param position the position
     * @param imageUploadInfo the image paths
     */
    public void setItem(int position,ImageUploadInfo imageUploadInfo) {
        images.set(position,imageUploadInfo);
        notifyDataSetChanged();
    }

    /**
     * 增加多条并改变视图.
     */
    public void clearItems() {
        images.clear();
        notifyDataSetChanged();
    }

    public int containCam(){
        int pos = -1;
        for(int i=0;i<images.size();i++){
            ImageUploadInfo imageUploadInfo = images.get(i);
            if(imageUploadInfo.isCamBtn()){
                pos = i;
                break;
            }
        }
        return pos;
    }


    /**
     * View元素.
     */
    public static class ViewHolder {
        public ImageView imageView;
        public Button deleteBtn;
        public RelativeLayout parentLayout;
        public String imageTag;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void loadImage(final int position,final ViewHolder holder){

        final ImageUploadInfo imageUploadInfo = images.get(position);
        //AbLogUtil.e(this.context,position+"路径："+image);
        final String image = imageUploadInfo.getPath();
        final String currentTag = image+"_"+position;
        //设置标记
        holder.imageTag = currentTag;

        if(!RdfStrUtil.isEmpty(image)){
            if(image.indexOf("http://")!=-1){
                //图片的下载
                imageLoader.display(holder.imageView,image,this.width,this.height);

            }else if(RdfStrUtil.isNumber(image)){
                //索引图片
                try {
                    int res  = Integer.parseInt(image);
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(res));
                } catch (Exception e) {
                }
            }else {
                final RdfTaskItem item = new RdfTaskItem();
                item.setPosition(position);
                item.setListener(new RdfTaskObjectListener() {
                    @Override
                    public <T> T getObject() {
                        Bitmap bitmap = RdfImageUtil.getThumbnail(new File(image),width, height);
                        return (T) bitmap;
                    }

                    @Override
                    public <T> void update(T t) {
                        if (t == null) {
                            return;
                        }
                        Bitmap bitmap = (Bitmap) t;
                        if (currentTag.equals(holder.imageTag)) {
                            holder.imageView.setImageBitmap(bitmap);
                            //AbLogUtil.e("TAG", "显示：" + position+","+bitmap);
                        }
                    }
                });
                task.execute(item);
            }
        }

    }

}

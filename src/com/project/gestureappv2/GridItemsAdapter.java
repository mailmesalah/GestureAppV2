package com.project.gestureappv2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridItemsAdapter  extends BaseAdapter {
    private Context mContext;    
    private List<Bitmap> images = new ArrayList<Bitmap>();

    public GridItemsAdapter(Context c) {
        mContext = c;
        
    }

    public int getCount() {      	
        return images.size();
    }

    public Object getItem(int position) {    
        return images.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public void setItemAt(int position, Bitmap image){
    	images.add(position, image);
    }
    
    public void removeItemAt(int position){
    	images.remove(position);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setTag(position);
        } else {
            imageView = (ImageView) convertView;
        }

        //DatabaseHandler dh = DatabaseHandler.getInstance(mContext);
        //byte[] imgByte = dh.getImage(position);
        //imageView.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        imageView.setImageBitmap(images.get(position));
       
        return imageView;
    }

    public void loadAllImages(){
    	images.clear();
    	DatabaseHandler dh = DatabaseHandler.getInstance(mContext);
        List<byte[]> imgByteList = dh.getAllImages();
        for (Iterator<byte[]> iterator = imgByteList.iterator(); iterator.hasNext();) {
			byte[] imgByte = (byte[]) iterator.next();
			images.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
		}
         notifyDataSetChanged();
    }
}

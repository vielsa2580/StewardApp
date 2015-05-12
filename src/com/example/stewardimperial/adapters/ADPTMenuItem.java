package com.example.stewardimperial.adapters;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.presentors.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ADPTMenuItem extends BaseAdapter{

	Activity activity;
	List<MODELMenuItems> listSubCat;
	int indicatorVal,height;
	
	public ADPTMenuItem(Activity activity,List<MODELMenuItems> listMenuItem,int height,int indicatorVal){
		super();
		this.activity = activity;
		this.listSubCat = listMenuItem;
		this.indicatorVal = indicatorVal;
		this.height = height;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSubCat.size();
	}

	@Override
	public MODELMenuItems getItem(int pos) {
		// TODO Auto-generated method stub
		return listSubCat.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return Long.parseLong(""+listSubCat.get(pos).getId());
	}

	@Override
	public View getView(int position, View arg1, ViewGroup parent) {
		
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_list, null);
		
		//-----------------------------------------Initializing all the variables-------------------------------------------------------
		TextView title = (TextView) retval.findViewById(R.id.tvItem);
		title.setText(listSubCat.get(position).getItemName().toString());
		
		AbsListView.LayoutParams params = (AbsListView.LayoutParams) retval.getLayoutParams();
		if (params == null) { 
		    params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height); 
		} else {
		    params.height = height;
		}

		retval.setLayoutParams(params);
		
		ImageView ivItemImage = (ImageView)retval.findViewById(R.id.ivItem);
//		new DownloadImageTask(ivItemImage, listSubCat.get(position).getImageUrl().toString()).execute("");
		UrlImageViewHelper.setUrlDrawable(ivItemImage, listSubCat.get(position).getImageUrl().toString());
		
		ImageView ivIndicator = (ImageView)retval.findViewById(R.id.ivPositionIndicator);
		
		if (indicatorVal == (int)getItemId(position)) {
			ivIndicator.setVisibility(View.VISIBLE);	
		}else{
			ivIndicator.setVisibility(View.GONE);
		}
		
		retval.setId((int)getItemId(position));
		return retval;
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView ivMenuItem;
		String url;

		public DownloadImageTask(ImageView bmImage,String url) {
			this.ivMenuItem = bmImage;
			this.url = url;
		}

		protected Bitmap doInBackground(String... urls) {

			Bitmap bmp = null;
			try {
//				InputStream in = new java.net.URL(url).openStream();
//				mIcon11 = BitmapFactory.decodeStream(in);
				URL tempUrl = new URL(url);
				bmp = BitmapFactory.decodeStream(tempUrl.openConnection().getInputStream());
				
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			ivMenuItem.setImageBitmap(result);
		}
	}

}

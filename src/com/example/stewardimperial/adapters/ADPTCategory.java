package com.example.stewardimperial.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stewardimperial.models.MODELMainCategory;
import com.example.stewardimperial.presentors.R;

public class ADPTCategory extends BaseAdapter{

	Activity activity;
	private List<MODELMainCategory> lsCategory;
	int indicator;
	
	public ADPTCategory(Activity activity,List<MODELMainCategory> lsCategory,int indicator){
		super();
		this.lsCategory = lsCategory;
		this.activity = activity;
		this.indicator = indicator;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsCategory.size();
	}

	@Override
	public MODELMainCategory getItem(int pos) {
		// TODO Auto-generated method stub
		return lsCategory.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return Long.parseLong(lsCategory.get(pos).getId());
	}

	@Override
	public View getView(int position, View arg1, ViewGroup parent) {
		
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_list, null);
		
		RelativeLayout rlParentLayout = (RelativeLayout)retval.findViewById(R.id.rlHoriparent);
		
		TextView title = (TextView) retval.findViewById(R.id.tvCatName);
		title.setText(getItem(position).getName());
		
		retval.setId((int)getItemId(position));
		
		if(indicator == getItemId(position)){
			rlParentLayout.setBackgroundResource(R.drawable.dialogback);
		}else{
			rlParentLayout.setBackgroundResource(R.drawable.cat_background);
		}
		
		return retval;
	}
	
}

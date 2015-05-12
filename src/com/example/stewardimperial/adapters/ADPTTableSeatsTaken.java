package com.example.stewardimperial.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stewardimperial.presentors.R;

public class ADPTTableSeatsTaken extends BaseAdapter{

	Activity activity;
	private List<Integer> tableCapacity;
	
	public ADPTTableSeatsTaken(Activity activity, List<Integer> tableCapacity) {
		super();
		this.activity = activity;
		this.tableCapacity = new ArrayList<Integer>();
		
		this.tableCapacity = tableCapacity;
		
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tableCapacity.size();
	}
	
	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return tableCapacity.get(pos);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		
		View retView = activity.getLayoutInflater().inflate(R.layout.table_seats_taken, null);
		
		TextView tvTablePreviewName = (TextView)retView.findViewById(R.id.tvTableSeatsTaken);
		tvTablePreviewName.setText(getItem(pos).toString());
		
		retView.setTag(getItem(pos));
		return retView;
	}
	
}

package com.example.stewardimperial.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stewardimperial.models.MODELTableOrder;
import com.example.stewardimperial.presentors.R;

public class ADPTTableJoinPreview extends BaseAdapter{

	Activity activity;
	private List<MODELTableOrder> lsTableOrderData;
	
	
	public ADPTTableJoinPreview() {
		super();
	}
	
	public ADPTTableJoinPreview(Activity activity, List<MODELTableOrder> lsTableOrderData) {
		super();
		this.activity = activity;
		this.lsTableOrderData = lsTableOrderData;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsTableOrderData.size();
	}
	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return lsTableOrderData.get(pos);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(lsTableOrderData.get(arg0).getOrderId());
	}
	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		
		View retView = activity.getLayoutInflater().inflate(R.layout.table_join_preview, null);
		
		TextView tvTablePreviewName = (TextView)retView.findViewById(R.id.tvTableJoinPreviewData);
		tvTablePreviewName.setText(String.valueOf(((MODELTableOrder)getItem(pos)).getTablePerOrder().get(0).getId()));
		
		return retView;
	}
	
}

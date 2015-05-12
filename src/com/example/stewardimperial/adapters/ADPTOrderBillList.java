package com.example.stewardimperial.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.models.MODELSizeType;
import com.example.stewardimperial.presentors.R;
import com.example.stewardimperial.utils.UTILConstants;

public class ADPTOrderBillList extends BaseAdapter{

	Activity activity;
	private List<MODELMenuItems> lsMenuItem;

	public ADPTOrderBillList(Activity activity,List<MODELMenuItems> lsMenuItems){
		super();
		this.lsMenuItem = lsMenuItems;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsMenuItem.size();
	}

	@Override
	public MODELMenuItems getItem(int pos) {
		// TODO Auto-generated method stub
		return lsMenuItem.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return Long.parseLong(""+lsMenuItem.get(pos).getId());
	}

	@Override
	public View getView(int position, View arg1, ViewGroup parent) {

		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderpreviewbill_per_list, null);
		
		ImageView ivOrderStatus = (ImageView)retval.findViewById(R.id.ivOrderPreviewBillStatus);
		TextView tvOrderItemName = (TextView)retval.findViewById(R.id.tvOrderPreviewBillItemName);
		TextView tvOrderPreviewBillItemQuant = (TextView)retval.findViewById(R.id.tvOrderPreviewBillItemQuant);
		TextView tvOrderPreviewBillItemPrice = (TextView)retval.findViewById(R.id.tvOrderPreviewBillItemPrice);
		TextView tvOrderPreviewBillItemTotal = (TextView)retval.findViewById(R.id.tvOrderPreviewBillItemTotal);
		
		if (getItem(position).getCookingStatus().equals(UTILConstants.STATUS_COOKED)){
			ivOrderStatus.setBackgroundResource(R.drawable.green);
		}else{
			ivOrderStatus.setBackgroundResource(R.drawable.orange);
		}

		tvOrderItemName.setText(getItem(position).getItemName());
		tvOrderPreviewBillItemQuant.setText(getItem(position).getSelectedQuantity());

		List<MODELSizeType> lsSizeType = new ArrayList<MODELSizeType>();
		lsSizeType = getItem(position).getSizeTypeList();
		
		String itemPrice = "";
		
		for (MODELSizeType modelSizeType : lsSizeType) {
			if (modelSizeType.getSizeTypeName().equals(getItem(position).getSelectedSize())) {
				itemPrice = modelSizeType.getPrice();
				break;
			}
		}
		
		tvOrderPreviewBillItemPrice.setText(itemPrice);
		
		int total = Integer.parseInt(itemPrice)*Integer.parseInt(getItem(position).getSelectedQuantity());
		
		tvOrderPreviewBillItemTotal.setText(""+total);
		
		return retval;
	}

}

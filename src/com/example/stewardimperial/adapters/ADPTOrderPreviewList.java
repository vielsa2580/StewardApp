package com.example.stewardimperial.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stewardimperial.custom.CustomImageView;
import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.presentors.R;
import com.example.stewardimperial.utils.UTILConstants;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ADPTOrderPreviewList extends BaseAdapter implements OnClickListener{

	Activity activity;
	private List<MODELMenuItems> lsMenuItem;
	Button btOrderPreviewMenuQuantDecrease;
	TextView tvOrderPreviewListMenuQuant;
	int TYPE_INCREASE=1,TYPE_DECREASE=0;

	public ADPTOrderPreviewList(Activity activity,List<MODELMenuItems> lsMenuItems){
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

		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderpreview_per_list, null);

		CustomImageView ivOrderPreviewListMenuImage = (CustomImageView)retval.findViewById(R.id.ivOrderPreviewListView);
		UrlImageViewHelper.setUrlDrawable(ivOrderPreviewListMenuImage, lsMenuItem.get(position).getImageUrl());

		String commentsString = "";

		for (int i = 0; i < UTILConstants.lsMenuItem.size(); i++) {
			if (UTILConstants.lsMenuItem.get(i).getId() == lsMenuItem.get(position).getId()) {
				commentsString = UTILConstants.lsMenuItem.get(i).getComments();
			}
		}

		String commentsGiven = lsMenuItem.get(position).getComments();

		String [] commentsGivenArray = commentsGiven.split("\\_");
		String [] commentsOptions = commentsString.split("\\_");



		CheckBox cbCommentOne = (CheckBox)retval.findViewById(R.id.cbOrderPreviewListCommentOne);
		cbCommentOne.setTag(getItemId(position));
		//		cbCommentOne.setEnabled(false);
		//		cbCommentOne.setOnCheckedChangeListener(this);

		CheckBox cbCommentTwo = (CheckBox)retval.findViewById(R.id.cbOrderPreviewListCommentTwo);
		cbCommentTwo.setTag(getItemId(position));
		//		cbCommentOne.setEnabled(false);
		//		cbCommentTwo.setOnCheckedChangeListener(this);

		CheckBox cbCommentThree = (CheckBox)retval.findViewById(R.id.cbOrderPreviewListCommentThree);
		cbCommentThree.setTag(getItemId(position));
		//		cbCommentOne.setEnabled(false);
		//		cbCommentThree.setOnCheckedChangeListener(this);

		EditText etCommentFour = (EditText)retval.findViewById(R.id.etOrderPreviewList);


		for (int ij = 0; ij < commentsOptions.length; ij++) {

			switch (ij) {
			case 0:
				if (!commentsOptions[0].equals("")) {
					cbCommentOne.setText(commentsOptions[0]);
					for (int i = 0; i < commentsGivenArray.length; i++) {
						if (commentsOptions[0].equals(commentsGivenArray[i])) {
							cbCommentOne.setChecked(true);
							commentsGivenArray[i]="";
						}
					}
				}		
				break;

			case 1:
				if (!commentsOptions[1].equals("")) {
					cbCommentTwo.setText(commentsOptions[1]);
					for (int i = 0; i < commentsGivenArray.length; i++) {
						if (commentsOptions[1].equals(commentsGivenArray[i])) {
							cbCommentTwo.setChecked(true);
							commentsGivenArray[i]="";
						}
					}
				}
				break;

			case 2:
				if (!commentsOptions[2].equals("")) {
					cbCommentThree.setText(commentsOptions[2]);
					for (int i = 0; i < commentsGivenArray.length; i++) {
						if (commentsOptions[2].equals(commentsGivenArray[i])) {
							cbCommentThree.setChecked(true);
							commentsGivenArray[i]="";
						}
					}
				}

				break;

			default:
				break;
			}

		}

		if (commentsGivenArray.length >0) {
			etCommentFour.setText(commentsGivenArray[commentsGivenArray.length-1]);
		}

		TextView tvOrderPreviewListMenuName = (TextView)retval.findViewById(R.id.tvOrderPreviewListView);
		tvOrderPreviewListMenuName.setText(lsMenuItem.get(position).getItemName());

		//------------------------------------QUANTITY-------------------------------------------------------

		tvOrderPreviewListMenuQuant = (TextView)retval.findViewById(R.id.tvOrderPreviewListQuantity);
		tvOrderPreviewListMenuQuant.setText(lsMenuItem.get(position).getSelectedQuantity());
		tvOrderPreviewListMenuQuant.setTag(position);

		Button btOrderPreviewMenuQuantIncrease = (Button)retval.findViewById(R.id.btOrderPreviewListQuantIncrease);
		btOrderPreviewMenuQuantIncrease.setOnClickListener(this);
		btOrderPreviewMenuQuantIncrease.setTag(position);

		btOrderPreviewMenuQuantDecrease = (Button)retval.findViewById(R.id.btOrderPreviewListQuantDecrease);
		btOrderPreviewMenuQuantDecrease.setOnClickListener(this);
		btOrderPreviewMenuQuantDecrease.setTag(position);
		
		Button btOrederPreviewItemDelete = (Button)retval.findViewById(R.id.btOrderPreviewViewDelete);
		btOrederPreviewItemDelete.setOnClickListener(this);
		btOrederPreviewItemDelete.setTag(position);
		

		return retval;
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btOrderPreviewListQuantDecrease:

			for (int i = 0; i < lsMenuItem.size(); i++) {
				if (i==Integer.parseInt(""+v.getTag())) {
					lsMenuItem.get(i).setSelectedQuantity(""+(Integer.parseInt(lsMenuItem.get(i).getSelectedQuantity())-1));
					tvOrderPreviewListMenuQuant.setText(lsMenuItem.get(i).getSelectedQuantity());
					ListView lv = (ListView)activity.findViewById(R.id.lvOrderPreview);
					lv.setAdapter(new ADPTOrderPreviewList(activity, lsMenuItem));
					lv.setSelection(i);
					break;
				}
			}

			break;

		case R.id.btOrderPreviewListQuantIncrease:

			for (int i = 0; i < lsMenuItem.size(); i++) {

				if (i==Integer.parseInt(""+v.getTag())) {
					lsMenuItem.get(i).setSelectedQuantity(""+(Integer.parseInt(lsMenuItem.get(i).getSelectedQuantity())+1));
					tvOrderPreviewListMenuQuant.setText(lsMenuItem.get(i).getSelectedQuantity());
					ListView lv = (ListView)activity.findViewById(R.id.lvOrderPreview);
					lv.setAdapter(new ADPTOrderPreviewList(activity, lsMenuItem));
					lv.setSelection(i);

					break;
				}
			}

			break;

		case R.id.btOrderPreviewViewDelete:
			
			for (int i = 0; i < lsMenuItem.size(); i++) {

				if (i==Integer.parseInt(""+v.getTag())) {
					lsMenuItem.remove(i);
					ListView lv = (ListView)activity.findViewById(R.id.lvOrderPreview);
					LinearLayout llOrderPreviewViewLeft = (LinearLayout)activity.findViewById(R.id.llOrderPreviewViewLeft);
					LinearLayout llOrderPreviewEmptyLeft = (LinearLayout)activity.findViewById(R.id.llOrderPreviewEmptyLeft);
					
					if (lsMenuItem.size() != 0 && lsMenuItem != null) {
						lv.setAdapter(new ADPTOrderPreviewList(activity, lsMenuItem));	
					}else{
						llOrderPreviewViewLeft.setVisibility(View.GONE);
						llOrderPreviewEmptyLeft.setVisibility(View.VISIBLE);
					}
					

					break;
				}
			}
			
			
			break;

		default:
			break;
		}
	}

}

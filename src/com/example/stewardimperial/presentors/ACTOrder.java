package com.example.stewardimperial.presentors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stewardimperial.adapters.ADPTCategory;
import com.example.stewardimperial.adapters.ADPTMenuItem;
import com.example.stewardimperial.adapters.ADPTOrderBillList;
import com.example.stewardimperial.adapters.ADPTOrderPreviewList;
import com.example.stewardimperial.custom.CustomButton;
import com.example.stewardimperial.datafetch.DFConfirmOrder;
import com.example.stewardimperial.datafetch.DFOrderCooked;
import com.example.stewardimperial.horizontal.HorizontalListView;
import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.models.MODELSizeType;
import com.example.stewardimperial.models.MODELTable;
import com.example.stewardimperial.models.MODELTableOrder;
import com.example.stewardimperial.utils.UTILConstants;
import com.example.stewardimperial.utils.UTILSharedPreference;

public class ACTOrder extends Activity implements View.OnClickListener{

	String orderId,itemSelected;
	//	Button btOrderPreview;
	private DrawerLayout mDrawerLayout;
	Handler drawerHandler;
	HorizontalListView hlvCategory;
	List<MODELMenuItems> tempItemList;
	ListView lvMenuItems;
	int itemListItemheight,TYPE_INCREASE=1,TYPE_DECREASE=0;
	MODELMenuItems selectedItem;

	TextView tvOrderQuantity,tvMenuItemDisplayName,tvCheckBoxCommentTwo,tvMenuItemDes;

	Button btCustomSmall,btCustomMedium,btCustomLarge,btIncreaseOrder,btDecreaseOrder,btMenuItemAdd;
	String selectedSize = "";

	CheckBox cbCommentOne,cbCommentTwo,cbCommentThree;

	LinearLayout llMenuDisplay;

	ImageView ivMenuItemDisp;

	MODELTableOrder modelTableOrder;

	EditText etMenuItemComment;

	Display display;
	Point size;

	//----------------------------ORDER PREVIEW------------------------------------

	LinearLayout llOrderPreviewView,llOrderPreviewBill,llOrderPreviewFeedBack,llOrderPreviewSendToKitchen;
	LinearLayout llOrderPreviewViewLeft,llOrderPreviewBillLeft,llOrderPreviewFeedBackLeft,llOrderPreviewEmptyLeft;/*>> layout for left display >>*/ 
	ImageView ivOrderPreviewViewIndicator,ivOrderPreviewBillIndicator,ivOrderPreviewFeedbackIndicator;
	public ListView lvOrderPreview,lvOrderPreviewBill;
	Button btOrderPreviewBillSubmit;
	EditText etOrderPreviewBillTotal;


	//===================================================================OVERRIDEN METHODS==============================================================================	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		UTILConstants.ACTIVITY = ACTOrder.this;
		UTILConstants.HANDLER = new Handler();
		orderId = getIntent().getExtras().getString("ORDERID");
		initializer();
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		UTILConstants.ACTIVITY = ACTOrder.this;
		UTILConstants.HANDLER = new Handler();
	}


	//===================================================================OVERRIDEN CLICK METHODS==============================================================================

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case 0:
			btCustomSmall.setBackgroundResource(R.drawable.custombuttonnormal);
			btCustomMedium.setBackgroundResource(R.drawable.custombutton);
			btCustomLarge.setBackgroundResource(R.drawable.custombutton);
			selectedSize = "small";
			break;

		case 1:
			btCustomMedium.setBackgroundResource(R.drawable.custombuttonnormal);
			btCustomSmall.setBackgroundResource(R.drawable.custombutton);
			btCustomLarge.setBackgroundResource(R.drawable.custombutton);
			selectedSize = "medium";
			break;

		case 2:
			btCustomLarge.setBackgroundResource(R.drawable.custombuttonnormal);
			btCustomMedium.setBackgroundResource(R.drawable.custombutton);
			btCustomSmall.setBackgroundResource(R.drawable.custombutton);
			selectedSize = "large";			
			break;

		case R.id.btDecreaseOrder:
			tvOrderQuantity.setText(increaseDecreaseValue(TYPE_DECREASE, tvOrderQuantity));
			break;

		case R.id.btIncreaseOrder:
			tvOrderQuantity.setText(increaseDecreaseValue(TYPE_INCREASE, tvOrderQuantity));
			break;

		case R.id.btMenuItemAdd:

			//			Log.v("ORDER DATA", "OrderId: "+orderId+" Item: "+selectedItem.getItemName()+" tableName "+modelTableOrder.getTablePerOrder().get(0).getTableName()
			//					+" comments "+(cbCommentOne.isChecked()?cbCommentOne.getText().toString():" ")+"_"+(cbCommentTwo.isChecked()?tvCheckBoxCommentTwo.getText().toString():" ")+"_" 
			//					+(cbCommentThree.isChecked()?cbCommentThree.getText().toString():" ")+"_"+(etMenuItemComment.length()!=0?etMenuItemComment.getText().toString():" ")
			//					+" SelectedSize: "+selectedSize+" Selected Quantity: "+tvOrderQuantity.getText());


			checkAndMakeOrder();
			Log.v("SUBMIT", ""+modelTableOrder);

			break;

		default:
			break;
		}

	}

	//===================================CUSTOM METHODS=================================================================================================================	

	private void initializer(){

		for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
			if (orderId.equals(UTILConstants.tableOrderData.get(i).getOrderId())) {
				modelTableOrder = UTILConstants.tableOrderData.get(i);
			}
		}

		//		orderItemList = new ArrayList<MODELMenuItems>(modelTableOrder.getMenuItemList());

		display = getWindowManager().getDefaultDisplay();
		size = new Point(); 
		display.getSize(size);
		itemListItemheight = (size.y/6);

		//-------------Order Preview-------------------------------------------------			

		drawerHandler = new Handler();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mDrawerLayout.setDrawerShadow(android.R.color.white, GravityCompat.END);

		mDrawerLayout.setEnabled(false);

		ivOrderPreviewViewIndicator = (ImageView)findViewById(R.id.ivOrderPreviewViewIndicator);
		llOrderPreviewView = (LinearLayout)findViewById(R.id.llOrderPreviewView);
		llOrderPreviewView.setOnClickListener(orderPreviewViewClick);
		llOrderPreviewViewLeft = (LinearLayout)findViewById(R.id.llOrderPreviewViewLeft);
		llOrderPreviewViewLeft.setVisibility(View.VISIBLE);


		ivOrderPreviewBillIndicator = (ImageView)findViewById(R.id.ivOrderPreviewBillIndicator);
		ivOrderPreviewBillIndicator.setVisibility(View.INVISIBLE);
		llOrderPreviewBill = (LinearLayout)findViewById(R.id.llOrderPreviewBill);
		llOrderPreviewBill.setOnClickListener(orderPreviewBillClick);
		llOrderPreviewBillLeft = (LinearLayout)findViewById(R.id.llOrderPreviewBillLeft);
		llOrderPreviewBillLeft.setVisibility(View.GONE);

		llOrderPreviewSendToKitchen = (LinearLayout)findViewById(R.id.llOrderPreviewSendToKitchen);
		llOrderPreviewSendToKitchen.setOnClickListener(orderPreviewSendToKitchenClick);

		ivOrderPreviewFeedbackIndicator = (ImageView)findViewById(R.id.ivOrderPreviewFeedbackIndicator);
		ivOrderPreviewFeedbackIndicator.setVisibility(View.INVISIBLE);
		llOrderPreviewFeedBack = (LinearLayout)findViewById(R.id.llOrderPreviewFeedback);
		llOrderPreviewFeedBack.setOnClickListener(orderPreviewFeedbackClick);
		llOrderPreviewFeedBackLeft = (LinearLayout)findViewById(R.id.llOrderPreviewFeedbackLeft);
		llOrderPreviewFeedBackLeft.setVisibility(View.GONE);

		llOrderPreviewEmptyLeft = (LinearLayout)findViewById(R.id.llOrderPreviewEmptyLeft);
		llOrderPreviewEmptyLeft.setVisibility(View.GONE);

		lvOrderPreview = (ListView)findViewById(R.id.lvOrderPreview);
		lvOrderPreviewBill = (ListView)findViewById(R.id.lvOrderPreviewBill);


		ActionBarDrawerToggle drawerListener = openCloseDrawer();

		mDrawerLayout.setDrawerListener(drawerListener);

		llMenuDisplay = (LinearLayout)findViewById(R.id.llMenuDisplay);

		btOrderPreviewBillSubmit = (Button)findViewById(R.id.btOrderPreviewBillSubmit);
		btOrderPreviewBillSubmit.setOnClickListener(orderPreviewBillSubmit);
		etOrderPreviewBillTotal = (EditText)findViewById(R.id.etOrderPreviewBillTotal);

		//-------------Category Horizontal List view-------------------------------------------------				

		hlvCategory = (HorizontalListView)findViewById(R.id.hlvCategory);

		if (UTILConstants.lsCategory != null && UTILConstants.lsCategory.size() != 0) {
			hlvCategory.setAdapter(new ADPTCategory(ACTOrder.this, UTILConstants.lsCategory,Integer.parseInt(UTILConstants.lsCategory.get(0).getId())));	
		}else{
			Toast.makeText(ACTOrder.this, "No Category Data", Toast.LENGTH_SHORT).show();
		}

		hlvCategory.setOnItemClickListener(categoryItemClick);

		//------------------Item listview-------------------------------------------------------------

		lvMenuItems = (ListView)findViewById(R.id.lvItems);
		tempItemList = new ArrayList<MODELMenuItems>();
		tempItemList = itemListForSelectedCat(UTILConstants.lsCategory.get(0).getId(), UTILConstants.lsMenuItem);

		if (tempItemList != null && tempItemList.size() !=0) {
			lvMenuItems.setVisibility(View.VISIBLE);
			lvMenuItems.setAdapter(new ADPTMenuItem(ACTOrder.this, tempItemList, itemListItemheight, tempItemList.get(0).getId()));
		}else{
			lvMenuItems.setVisibility(View.INVISIBLE);
		}

		lvMenuItems.setOnItemClickListener(menuListItemClick);

		//--------------------------Custom Button------------------------------------------------------

		LinearLayout llCustomHolder = (LinearLayout)findViewById(R.id.llCustomButtonHolder);
		llCustomHolder.addView(new CustomButton(ACTOrder.this).singleSelectButton());

		btCustomLarge = (Button)llCustomHolder.findViewById(2);
		btCustomLarge.setEnabled(false);

		btCustomMedium = (Button)llCustomHolder.findViewById(1);
		btCustomMedium.setEnabled(false);

		btCustomSmall = (Button)llCustomHolder.findViewById(0);
		btCustomSmall.setEnabled(false);

		//--------------------------MEnu Item Display--------------------------------------------------

		etMenuItemComment = (EditText)findViewById(R.id.etMenuItemComment);

		btIncreaseOrder = (Button)findViewById(R.id.btIncreaseOrder);
		btDecreaseOrder = (Button)findViewById(R.id.btDecreaseOrder);

		btIncreaseOrder.setOnClickListener(this);
		btDecreaseOrder.setOnClickListener(this);

		tvOrderQuantity = (TextView)findViewById(R.id.tvOrderQuantity);

		if (Integer.parseInt(tvOrderQuantity.getText().toString()) < 1) {
			btDecreaseOrder.setEnabled(false);
		}

		tvMenuItemDisplayName = (TextView)findViewById(R.id.tvMenuItemDisplayName);

		//-----------------------------------Comments--------------------------------

		cbCommentOne = (CheckBox)findViewById(R.id.cbCommentOne);
		cbCommentOne.setClickable(false);
		cbCommentTwo = (CheckBox)findViewById(R.id.cbCommentTwo);
		cbCommentTwo.setClickable(false);
		cbCommentThree = (CheckBox)findViewById(R.id.cbCommentThree);
		cbCommentThree.setClickable(false);
		tvCheckBoxCommentTwo = (TextView)findViewById(R.id.tvCheckBoxCommentTwo);


		tvMenuItemDes = (TextView)findViewById(R.id.tvMenuItemDes);

		btMenuItemAdd = (Button)findViewById(R.id.btMenuItemAdd);
		btMenuItemAdd.setOnClickListener(this);

		//--------------------------------Menu Image--------------------------------------------------
		ivMenuItemDisp = (ImageView)findViewById(R.id.ivMenuItemDisplay);

		if (tempItemList != null && tempItemList.size() !=0) {
			selectedItem = tempItemList.get(0);
			populateMenuItems(tempItemList, 0);	
		}

	}


	private ActionBarDrawerToggle openCloseDrawer() {
		ActionBarDrawerToggle drawerListener = new ActionBarDrawerToggle(ACTOrder.this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){

			public void onDrawerOpened(View drawerView) {
				drawerHandler.removeCallbacksAndMessages(null); 
				drawerHandler.postDelayed(new Runnable() {
					@Override
					public void run() {


						ivOrderPreviewViewIndicator.setVisibility(View.VISIBLE);
						ivOrderPreviewBillIndicator.setVisibility(View.GONE);
						ivOrderPreviewFeedbackIndicator.setVisibility(View.GONE);

						List<MODELMenuItems> lsMenuItems = new ArrayList<MODELMenuItems>();

						if (orderId.equals(modelTableOrder.getOrderId())) {
							lsMenuItems = modelTableOrder.getMenuItemList();
						}

						if (lsMenuItems !=null && lsMenuItems.size() != 0) {
							llOrderPreviewEmptyLeft.setVisibility(View.GONE);
							llOrderPreviewViewLeft.setVisibility(View.VISIBLE);
							lvOrderPreview.setAdapter(new ADPTOrderPreviewList(ACTOrder.this, lsMenuItems));	
						}else{
							llOrderPreviewEmptyLeft.setVisibility(View.VISIBLE);
							llOrderPreviewViewLeft.setVisibility(View.GONE);
						}


					}
				}, 250);
				mDrawerLayout.openDrawer(Gravity.END);	

			};

			public void onDrawerClosed(View drawerView) {
				Toast.makeText(getApplicationContext(), "CLOSED", Toast.LENGTH_SHORT).show();
			};
		};
		return drawerListener;
	}



	private void re_instantiate(final TextView tv){

		List<MODELTableOrder> tableName = new ArrayList<MODELTableOrder>();

		for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {

			if (orderId.equals(UTILConstants.tableOrderData.get(i).getOrderId())) {

				for (int j = 0; j < UTILConstants.tableOrderData.get(i).getTablePerOrder().size(); j++) {

					for (int j2 = 0; j2 < UTILConstants.lsTableData.size(); j2++) {

						if (UTILConstants.lsTableData.get(j2).getTableName().equals(UTILConstants.tableOrderData.get(i).getTablePerOrder().get(j).getTableName())) {

							MODELTableOrder modelTableOrder = new MODELTableOrder();
							modelTableOrder.setOrderId(UTILConstants.timeStampGenerator()+""+UTILConstants.lsTableData.get(j2).getId());
							List<MODELTable> tempTableList = new ArrayList<MODELTable>();
							tempTableList.add(UTILConstants.lsTableData.get(j2));
							modelTableOrder.setTablePerOrder(tempTableList);

							tableName.add(modelTableOrder);
						}
					}
				}
				UTILConstants.tableOrderData.remove(i);
			}

			Log.v("NEW_TABLE_ORDER", ""+UTILConstants.tableOrderData.size());
		}

		for (int i = 0; i < tableName.size(); i++) {
			UTILConstants.tableOrderData.add(tableName.get(i));
		}

		Collections.sort(UTILConstants.tableOrderData, Collections.reverseOrder(MODELTableOrder.tableIdComparator));

		startActivity(new Intent(ACTOrder.this,ACTTableGridLayout.class));
	}


	//==========================================MAKE ORDER============================================================================================================

	private MODELTableOrder checkAndMakeOrder(){

		if (Integer.parseInt(tvOrderQuantity.getText().toString()) != 0) {
			if (selectedSize != "") {
				MODELMenuItems items = new MODELMenuItems(selectedItem);

				items.setComments((cbCommentOne.isChecked()?cbCommentOne.getText().toString():" ")+"_"+(cbCommentTwo.isChecked()?tvCheckBoxCommentTwo.getText().toString():" ")+"_" 
						+(cbCommentThree.isChecked()?cbCommentThree.getText().toString():" ")+"_"+(etMenuItemComment.length()!=0?etMenuItemComment.getText().toString():" "));
				items.setSelectedSize(selectedSize);
				items.setSelectedQuantity(tvOrderQuantity.getText().toString());
				items.setCookingStatus(UTILConstants.STATUS_COOKING);

				if (modelTableOrder.getMenuItemList() == null || modelTableOrder.getMenuItemList().size() == 0) {
					List<MODELMenuItems> ls = new ArrayList<MODELMenuItems>();
					ls.add(items);
					modelTableOrder.setMenuItemList(ls);
				}else{

					int one = 0, two = 0;
					boolean mustAdd = false;

					for (int j = 0; j < modelTableOrder.getMenuItemList().size(); j++) {

						if (items.getId() == modelTableOrder.getMenuItemList().get(j).getId()){

							if (items.getComments().equals(modelTableOrder.getMenuItemList().get(j).getComments()) 
									&& items.getSelectedSize().equals(modelTableOrder.getMenuItemList().get(j).getSelectedSize())) {

								one = Integer.parseInt(modelTableOrder.getMenuItemList().get(j).getSelectedQuantity());
								two = Integer.parseInt(items.getSelectedQuantity());

								modelTableOrder.getMenuItemList().get(j).setSelectedQuantity(""+(one+two));
								mustAdd = false;
								break;
							}else{
								//										UTILConstants.tableOrderData.get(i).getMenuItemList().add(items);
								mustAdd = true;
							}

						}else{
							mustAdd = true;
						}
					}

					if (mustAdd) {
						modelTableOrder.getMenuItemList().add(items);
					}

				}
				//					}
				//				}
			}else{
				Toast.makeText(getApplicationContext(), "Please select item size", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), "Please select item quantity", Toast.LENGTH_LONG).show();
		}

		return null;
	}

	private String increaseDecreaseValue(int type,TextView edValueDisplay){

		String retValue = edValueDisplay.getText().toString();
		int valueToCheck = Integer.parseInt(edValueDisplay.getText().toString());

		switch (type) {
		case 0:

			//			btIncreaseOrder.setEnabled(true);
			if (valueToCheck == 1) {
				btDecreaseOrder.setEnabled(false);
				retValue = "0"+(valueToCheck-1);
			}else if (valueToCheck <= 10) {
				retValue = "0"+(valueToCheck-1);
			}else{
				retValue = ""+(valueToCheck-1);
			}

			break;

		case 1:

			btDecreaseOrder.setEnabled(true);
			if (valueToCheck < 10) {
				retValue = "0"+(valueToCheck+1);
			}else if (valueToCheck == 9) {
				retValue = ""+(valueToCheck+1);
			}else{
				retValue = ""+(valueToCheck+1);
			}

			break;

		default:
			break;
		}

		return retValue;
	}

	//==========================================ORDER PREVIEW============================================================================================================

	//---------------------ORDER PREVIEW CLICK LISTENERS----------------------------

	private OnClickListener orderPreviewViewClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			ivOrderPreviewViewIndicator.setVisibility(View.VISIBLE);
			ivOrderPreviewBillIndicator.setVisibility(View.INVISIBLE);
			ivOrderPreviewFeedbackIndicator.setVisibility(View.INVISIBLE);


			llOrderPreviewViewLeft.setVisibility(View.VISIBLE);
			llOrderPreviewBillLeft.setVisibility(View.GONE);
			llOrderPreviewFeedBackLeft.setVisibility(View.GONE);

			if (llOrderPreviewEmptyLeft.getVisibility() == View.VISIBLE) {
				llOrderPreviewEmptyLeft.setVisibility(View.GONE);
			}

			List<MODELMenuItems> lsMenuItems = new ArrayList<MODELMenuItems>();

			if (orderId.equals(modelTableOrder.getOrderId())) {
				lsMenuItems = modelTableOrder.getMenuItemList();
			}

			if (lsMenuItems !=null && lsMenuItems.size() != 0) {
				lvOrderPreview.setAdapter(new ADPTOrderPreviewList(ACTOrder.this, lsMenuItems));	
			}else{
				lvOrderPreview.setAdapter(new ADPTOrderPreviewList(ACTOrder.this, lsMenuItems));
				llOrderPreviewViewLeft.setVisibility(View.GONE);
				llOrderPreviewEmptyLeft.setVisibility(View.VISIBLE);
			}

		}
	};


	private OnClickListener orderPreviewBillClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			ivOrderPreviewViewIndicator.setVisibility(View.INVISIBLE);
			ivOrderPreviewBillIndicator.setVisibility(View.VISIBLE);
			ivOrderPreviewFeedbackIndicator.setVisibility(View.INVISIBLE);


			llOrderPreviewViewLeft.setVisibility(View.GONE);
			llOrderPreviewBillLeft.setVisibility(View.VISIBLE);
			llOrderPreviewFeedBackLeft.setVisibility(View.GONE);

			List<MODELMenuItems> lsMenuItems = new ArrayList<MODELMenuItems>();

			if (orderId.equals(modelTableOrder.getOrderId())) {
				lsMenuItems = modelTableOrder.getOrderCookedItemList();
			}

			if (lsMenuItems !=null && lsMenuItems.size() != 0) {
				llOrderPreviewBillLeft.setVisibility(View.VISIBLE);
				llOrderPreviewEmptyLeft.setVisibility(View.GONE);
				lvOrderPreviewBill.setAdapter(new ADPTOrderBillList(ACTOrder.this, lsMenuItems));
				
				int value = 0;
				
				for (int i = 0; i < lsMenuItems.size(); i++) {

					int itemPrice = 0;
					
					for (MODELSizeType modelSizeType : lsMenuItems.get(i).getSizeTypeList()) {
							itemPrice = Integer.parseInt(modelSizeType.getPrice());
							break;
					}
					
					int total = Integer.parseInt(lsMenuItems.get(i).getSelectedQuantity())*itemPrice;
					value = value+total;
					
				}
				
				etOrderPreviewBillTotal.setText(""+value);
				
			}else{
				llOrderPreviewBillLeft.setVisibility(View.GONE);
				llOrderPreviewEmptyLeft.setVisibility(View.VISIBLE);
			}

		}
	};


	private OnClickListener orderPreviewFeedbackClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			ivOrderPreviewViewIndicator.setVisibility(View.INVISIBLE);
			ivOrderPreviewBillIndicator.setVisibility(View.INVISIBLE);
			ivOrderPreviewFeedbackIndicator.setVisibility(View.VISIBLE);


			llOrderPreviewViewLeft.setVisibility(View.GONE);
			llOrderPreviewBillLeft.setVisibility(View.GONE);
			llOrderPreviewFeedBackLeft.setVisibility(View.VISIBLE);


		}
	};


	private OnClickListener orderPreviewSendToKitchenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new sendToKitchen().execute();
		}
	};




	private OnClickListener orderPreviewBillSubmit = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			new reinstance(orderId).execute();
		}
	};



	//==========================================CATEGORY============================================================================================================

	private OnItemClickListener categoryItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,long arg3) {

			hlvCategory.setAdapter(new ADPTCategory(ACTOrder.this, UTILConstants.lsCategory,Integer.parseInt(UTILConstants.lsCategory.get(pos).getId())));

			hlvCategory.setSelection(pos);

			tempItemList = new ArrayList<MODELMenuItems>();
			tempItemList = itemListForSelectedCat(""+view.getId(), UTILConstants.lsMenuItem);

			if (tempItemList != null && tempItemList.size() !=0) {
				lvMenuItems.setVisibility(View.VISIBLE);
				llMenuDisplay.setVisibility(View.VISIBLE);
				lvMenuItems.setAdapter(new ADPTMenuItem(ACTOrder.this, tempItemList, itemListItemheight, tempItemList.get(0).getId()));
			}else{
				lvMenuItems.setVisibility(View.INVISIBLE);
				llMenuDisplay.setVisibility(View.INVISIBLE);
			}
		}
	};


	private List<MODELMenuItems> itemListForSelectedCat(String catId, List<MODELMenuItems> subCat){

		List<MODELMenuItems> lsDivSubCat = new ArrayList<MODELMenuItems>();

		for (int i = 0; i < subCat.size(); i++) {
			if (catId.equals(subCat.get(i).getCatIdFk().toString())) {
				lsDivSubCat.add(subCat.get(i));
			}
		}
		return lsDivSubCat;
	}

	//----------------------------------------------ITEMS--------------------------------------------------------------------------------------------------------------------- 

	private OnItemClickListener menuListItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,long arg3) {
			lvMenuItems.setAdapter(new ADPTMenuItem(ACTOrder.this, tempItemList, itemListItemheight, view.getId()));
			lvMenuItems.setSelection(pos);
			selectedItem = new MODELMenuItems();
			selectedItem = tempItemList.get(pos);
			populateMenuItems(tempItemList, pos);

		}
	};

	private void populateMenuItems(List<MODELMenuItems> lsMenuItems,int pos){

		UTILConstants.setTypefaceCustom(ACTOrder.this, "", 35, tvMenuItemDisplayName, lsMenuItems.get(pos).getItemName());
		tvMenuItemDes.setText(lsMenuItems.get(pos).getItemDes());
		tvOrderQuantity.setText("01");

		btCustomMedium.setEnabled(false);
		btCustomLarge.setEnabled(false);
		btCustomSmall.setEnabled(false);

		btCustomMedium.setBackgroundResource(R.drawable.custombuttonnormal);
		btCustomSmall.setBackgroundResource(R.drawable.custombutton);
		btCustomLarge.setBackgroundResource(R.drawable.custombutton);
		selectedSize = "medium";

		for (int i = 0; i < lsMenuItems.get(pos).getSizeTypeList().size(); i++) {

			MODELSizeType modelSizeType = new MODELSizeType();
			modelSizeType = tempItemList.get(pos).getSizeTypeList().get(i);

			switch (i) {
			case 0:
				btCustomSmall.setEnabled(true);
				btCustomSmall.setText(modelSizeType.getSizeTypeName());
				btCustomSmall.setOnClickListener(this);
				break;
			case 1:
				btCustomMedium.setEnabled(true);
				btCustomMedium.setText(modelSizeType.getSizeTypeName());
				btCustomMedium.setOnClickListener(this);
				break;
			case 2:
				btCustomLarge.setEnabled(true);
				btCustomLarge.setText(modelSizeType.getSizeTypeName());
				btCustomLarge.setOnClickListener(this);
				break;

			default:
				break;
			}

		}

		new DownloadImageTask(ivMenuItemDisp, tempItemList.get(pos).getImageUrl().toString()).execute();

		String[] parts = lsMenuItems.get(pos).getComments().split("\\_");

		if (!parts[0].equals("")) {
			cbCommentOne.setClickable(true);
			cbCommentOne.setText(parts[0]);
		}

		if (!parts[1].equals("")) {
			cbCommentTwo.setClickable(true);
			tvCheckBoxCommentTwo.setText(parts[1]);
		}

		if (!parts[2].equals("")) {
			cbCommentThree.setClickable(true);
			cbCommentThree.setText(parts[2]);
		}

	}

	//=======================================================================CUSTOM CLASSES==========================================================================================




	private class reinstance extends AsyncTask<Void, Void, Void>{

		String orderId;

		public reinstance(String orderId){
			super();
			this.orderId = orderId;
		}

		@Override
		protected Void doInBackground(Void... params) {

			MODELTableOrder modelTableOrder = new MODELTableOrder();

			for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {	
				if (orderId.equals(UTILConstants.tableOrderData.get(i).getOrderId())) {
					if (UTILConstants.tableOrderData.get(i).getMenuItemList().size() != 0) {

						modelTableOrder = new MODELTableOrder(UTILConstants.tableOrderData.get(i));
						if (UTILConstants.tableOrderData.get(i).getJoinNormalSplit().equals(UTILConstants.TABLE_SPLIT)) {

							UTILConstants.tableOrderData.remove(i);

							for (int j = 0; j < UTILConstants.tableOrderData.size(); j++) {
								for (int k = 0; k < UTILConstants.tableOrderData.get(j).getTablePerOrder().size(); k++) {
									for (int k2 = 0; k2 < modelTableOrder.getTablePerOrder().size(); k2++) {
										if (modelTableOrder.getTablePerOrder().get(k2).getId() == UTILConstants.tableOrderData.get(j).getTablePerOrder().get(k).getId()) {

											String tableNameTemp = modelTableOrder.getTablePerOrder().get(k2).getTableName().substring(0, modelTableOrder.getTablePerOrder().get(k2).getTableName().length()-1);
											UTILConstants.tableOrderData.get(j).getTablePerOrder().get(k).setTableName(tableNameTemp);
										}	
									}

								}

							}

						}else	if (UTILConstants.tableOrderData.get(i).getJoinNormalSplit().equals(UTILConstants.TABLE_JOINED)) {
							List<MODELTableOrder> tableName = new ArrayList<MODELTableOrder>();

							for (int i3 = 0; i3 < UTILConstants.tableOrderData.size(); i3++) {

								if (orderId.equals(UTILConstants.tableOrderData.get(i3).getOrderId())) {

									for (int j = 0; j < UTILConstants.tableOrderData.get(i3).getTablePerOrder().size(); j++) {

										for (int j2 = 0; j2 < UTILConstants.lsTableData.size(); j2++) {

											if (UTILConstants.lsTableData.get(j2).getTableName().equals(UTILConstants.tableOrderData.get(i3).getTablePerOrder().get(j).getTableName())) {

												MODELTableOrder modelTableOrder2 = new MODELTableOrder();
												modelTableOrder2.setOrderId(UTILConstants.timeStampGenerator()+""+UTILConstants.lsTableData.get(j2).getId());
												modelTableOrder2.setIsSelected(UTILConstants.TABLE_UNSELECTED);
												modelTableOrder2.setJoinNormalSplit(UTILConstants.TABLE_NORMAL);
												modelTableOrder2.setSeatsTaken(0);
												modelTableOrder2.setTakenStatus(UTILConstants.TABLE_EMPTY);
												modelTableOrder2.setMenuItemList(new ArrayList<MODELMenuItems>());
												modelTableOrder2.setOrderCookedItemList(new ArrayList<MODELMenuItems>());
												List<MODELTable> tempTableList = new ArrayList<MODELTable>();
												tempTableList.add(UTILConstants.lsTableData.get(j2));
												modelTableOrder2.setTablePerOrder(tempTableList);

												tableName.add(modelTableOrder2);
											}
										}
									}
									UTILConstants.tableOrderData.remove(i3);
								}

							}

							for (int i3 = 0; i3 < tableName.size(); i3++) {
								UTILConstants.tableOrderData.add(tableName.get(i3));
							}
						}

					}//
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void tableName) {
			super.onPostExecute(tableName);
			startActivity(new Intent(ACTOrder.this,ACTTableGridLayout.class));
		}
	}



	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView ivMenuItem;
		String url;

		public DownloadImageTask(ImageView bmImage,String url) {
			this.ivMenuItem = bmImage;
			this.url = url;
		}

		protected Bitmap doInBackground(String... urls) {

			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			ivMenuItem.setImageBitmap(result);
		}
	}


	private class sendToKitchen extends AsyncTask<Void, MODELResultSet, MODELResultSet>{

		@Override
		protected MODELResultSet doInBackground(Void... params) {

			MODELResultSet modelResultSet = new MODELResultSet();

			for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
				if (UTILConstants.tableOrderData.get(i).getOrderId().equals(modelTableOrder.getOrderId())) {
					UTILConstants.tableOrderData.get(i).setMenuItemList(modelTableOrder.getMenuItemList());
					JSONArray confirmOrderMenuList = new JSONArray();
					JSONObject confirmOrderJson = new JSONObject();

					try {
						confirmOrderJson.put("orderId", modelTableOrder.getOrderId().toString());
						confirmOrderJson.put("deviceId", UTILConstants.getMacId(ACTOrder.this));
						confirmOrderJson.put("stewardId", UTILSharedPreference.getPreference(ACTOrder.this, UTILConstants.USER_NAME));
						confirmOrderJson.put("tableName", modelTableOrder.getTablePerOrder().get(0).getTableName());
						confirmOrderJson.put("confirmTime", UTILConstants.timeStampGeneratorWithFormat());
						confirmOrderJson.put("tableCapacity", UTILConstants.tableOrderData.get(i).getSeatsTaken());

						for (int j = 0; j < UTILConstants.tableOrderData.get(i).getMenuItemList().size(); j++) {
							JSONObject tempMenuObj = new JSONObject();
							tempMenuObj.put("itemId", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getId());
							tempMenuObj.put("kitchenId", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getKitchenIdFk());
							tempMenuObj.put("venueTypeId", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getVenueTypeIdFk());
							tempMenuObj.put("tax", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getTax());
							tempMenuObj.put("catId", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getCatIdFk());
							tempMenuObj.put("quantity", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getSelectedQuantity());
							tempMenuObj.put("type", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getSelectedSize());
							tempMenuObj.put("comments", UTILConstants.tableOrderData.get(i).getMenuItemList().get(j).getComments());

							confirmOrderMenuList.put(j,tempMenuObj);
						}

						confirmOrderJson.put("orderData", confirmOrderMenuList);

						//						StewardConfirmOrder.php

						Log.v("CONFIRM", confirmOrderJson.toString());
						modelResultSet = new DFConfirmOrder().confirmOrder(confirmOrderJson.toString());

					} catch (JSONException e) {
						modelResultSet.setError(UTILConstants.ERROR_MSG);
						modelResultSet.setMessage(""+e);
					}
				}
			}

			return modelResultSet;
		}


		@Override
		protected void onPostExecute(MODELResultSet result) {
			super.onPostExecute(result);

			if (!result.getError().equals(UTILConstants.ERROR_MSG)) {
				List<MODELMenuItems> lsItemsSentToKitchen;
				for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
					lsItemsSentToKitchen = new ArrayList<MODELMenuItems>();
					if (UTILConstants.tableOrderData.get(i).getOrderId().equals(modelTableOrder.getOrderId())) {
						UTILConstants.tableOrderData.get(i).setTakenStatus(UTILConstants.TABLE_TAKEN);
						lsItemsSentToKitchen = UTILConstants.tableOrderData.get(i).getMenuItemList();
						for (int j = 0; j < lsItemsSentToKitchen.size(); j++) {
							lsItemsSentToKitchen.get(j).setCookingStatus(UTILConstants.STATUS_COOKING);
						}


						if (UTILConstants.tableOrderData.get(i).getOrderCookedItemList() != null) {
							for (int j = 0; j < UTILConstants.tableOrderData.get(i).getOrderCookedItemList().size(); j++) {
								lsItemsSentToKitchen.add(UTILConstants.tableOrderData.get(i).getOrderCookedItemList().get(j));
							}
						}

						UTILConstants.tableOrderData.get(i).setOrderCookedItemList(lsItemsSentToKitchen);
						UTILConstants.tableOrderData.get(i).setMenuItemList(new ArrayList<MODELMenuItems>());

						lvOrderPreview = (ListView)findViewById(R.id.lvOrderPreview);

						if (UTILConstants.tableOrderData.get(i).getMenuItemList() != null && UTILConstants.tableOrderData.get(i).getMenuItemList().size() != 0) {
							lvOrderPreview.setAdapter(new ADPTOrderPreviewList(ACTOrder.this, UTILConstants.tableOrderData.get(i).getMenuItemList()));	
						}else{
							llOrderPreviewViewLeft.setVisibility(View.GONE);
							llOrderPreviewEmptyLeft.setVisibility(View.VISIBLE);
						}

						break;
					}

				}

			}else{
				Toast.makeText(getApplicationContext(), ""+result.getMessage(), Toast.LENGTH_LONG).show();
			}

		}

	}


	//=======================================================================PING METHODS==========================================================================================

	public void onOrderCooked(){
		new onOrderCookedDataReceive().execute();
	}

	//=======================================================================PING CLAS==========================================================================================


	private class onOrderCookedDataReceive extends AsyncTask<Void, MODELResultSet, MODELResultSet>{

		@Override
		protected MODELResultSet doInBackground(Void... params) {

			MODELResultSet modelJsonResponse = new MODELResultSet();
			modelJsonResponse = new DFOrderCooked().cookedOrder(ACTOrder.this);

			MODELTableOrder modelOrderComplete;
			List<MODELTableOrder> lsModelTableOrder = new ArrayList<MODELTableOrder>();

			if (modelJsonResponse.getError().equals(UTILConstants.SUCCESS_MSG)) {

				JSONObject jsonObj = new JSONObject();
				jsonObj = modelJsonResponse.getJsonObject();
				modelOrderComplete = new MODELTableOrder();

				try {
					if ( jsonObj.get("data") != null && ((JSONArray)jsonObj.get("data")).length() != 0) {

						JSONArray jsonArray = new JSONArray();
						jsonArray = ((JSONArray)jsonObj.get("data"));

						JSONObject jsonObject2 = new JSONObject();
						jsonObject2 = (JSONObject) jsonArray.get(0);

						if (jsonObject2.getString("orderList") !=null && jsonObject2.getString("itemId") != null 
								&& jsonObject2.getString("comments") != null && jsonObject2.getString("type") != null) {

							JSONArray orderIdArray = new JSONArray();
							orderIdArray = (JSONArray) jsonObject2.get("orderList");
							MODELMenuItems items;
							List<MODELMenuItems> lsMenuItems;

							for (int j = 0; j < orderIdArray.length(); j++) {

								modelOrderComplete = new MODELTableOrder();
//								JSONObject orderIdObj = (JSONObject) orderIdArray.get(j);
//								modelOrderComplete.setOrderId(orderIdObj.getString("orderId"));
								modelOrderComplete.setOrderId(""+orderIdArray.get(j));

								lsMenuItems = new ArrayList<MODELMenuItems>();
								items = new MODELMenuItems();

								items.setId(Integer.valueOf(jsonObject2.getString("itemId")));
								items.setComments(jsonObject2.getString("comments"));
								items.setSelectedSize(jsonObject2.getString("type"));
								lsMenuItems.add(items);

								modelOrderComplete.setMenuItemList(lsMenuItems);

								lsModelTableOrder.add(modelOrderComplete);
							}

							modelJsonResponse.setList(lsModelTableOrder);
						}else{
							modelJsonResponse.setError(UTILConstants.ERROR_MSG);
							modelJsonResponse.setMessage("Null Value");
						}

					}
				} catch (JSONException e) {
					modelJsonResponse.setError(UTILConstants.ERROR_MSG);
					modelJsonResponse.setMessage(""+e);
				}

			}else{
				modelJsonResponse.setError(UTILConstants.ERROR_MSG);
			}

			return modelJsonResponse;
		}


		@Override
		protected void onPostExecute(MODELResultSet result) {
			super.onPostExecute(result);

			if (result.getError().equals(UTILConstants.SUCCESS_MSG)) {

				List<MODELTableOrder> lsModelTableOrder = new ArrayList<MODELTableOrder>();
				lsModelTableOrder = (List<MODELTableOrder>) result.getList();

				for (int u = 0; u < UTILConstants.tableOrderData.size(); u++) {
					for (int j = 0; j < lsModelTableOrder.size(); j++) {

						if (lsModelTableOrder.get(j).getOrderId().equals(UTILConstants.tableOrderData.get(u).getOrderId())) {

							for (int u2 = 0; u2 < UTILConstants.tableOrderData.get(u).getOrderCookedItemList().size(); u2++) {
								for (int j2 = 0; j2 < lsModelTableOrder.get(j).getMenuItemList().size(); j2++) {

									if (UTILConstants.tableOrderData.get(u).getOrderCookedItemList().get(u2).getId() == lsModelTableOrder.get(j).getMenuItemList().get(j2).getId()
											&& UTILConstants.tableOrderData.get(u).getOrderCookedItemList().get(u2).getComments().equals(lsModelTableOrder.get(j).getMenuItemList().get(j2).getComments())
											&& UTILConstants.tableOrderData.get(u).getOrderCookedItemList().get(u2).getSelectedSize().equals(lsModelTableOrder.get(j).getMenuItemList().get(j2).getSelectedSize())) {

										//UTILConstants.tableOrderData.get(u).getMenuItemList().remove(u2);

										//List<MODELMenuItems> tempList = new ArrayList<MODELMenuItems>();
										//MODELMenuItems tempItem = new MODELMenuItems();

										UTILConstants.tableOrderData.get(u).getOrderCookedItemList().get(u2).setCookingStatus(UTILConstants.STATUS_COOKED);

										//tempItem = UTILConstants.tableOrderData.get(u).getOrderCookedItemList().get(u2);
										//tempItem.setCookingStatus(UTILConstants.STATUS_COOKED);
										//tempList.add(tempItem);
										//UTILConstants.tableOrderData.get(u).setOrderCookedItemList(tempList);

										break;
									}
								}
							}
						}
					}
				}

				for (int u = 0; u < UTILConstants.tableOrderData.size(); u++) {

					List<MODELMenuItems> lsMenuItems = new ArrayList<MODELMenuItems>();
					List<MODELMenuItems> cookedMenuItems = new ArrayList<MODELMenuItems>();

					if (UTILConstants.tableOrderData.get(u).getOrderId().equals(orderId)) {
						lsMenuItems = UTILConstants.tableOrderData.get(u).getMenuItemList();
						cookedMenuItems = UTILConstants.tableOrderData.get(u).getOrderCookedItemList();
					}

					if (llOrderPreviewViewLeft.getVisibility() == View.VISIBLE) {

						if (lsMenuItems != null && lsMenuItems.size() !=0) {
							lvOrderPreview.setAdapter(new ADPTOrderPreviewList(ACTOrder.this, lsMenuItems));	
						}
					}

					if (llOrderPreviewBillLeft.getVisibility() == View.VISIBLE) {
						if (cookedMenuItems != null && cookedMenuItems.size() != 0) {
							lvOrderPreviewBill.setAdapter(new ADPTOrderBillList(ACTOrder.this, cookedMenuItems));
						}
					}
				}

			}else{
				Toast.makeText(ACTOrder.this, ""+result.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	}


}

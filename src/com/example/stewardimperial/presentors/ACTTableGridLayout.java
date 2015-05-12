package com.example.stewardimperial.presentors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stewardimperial.adapters.ADPTTableJoinPreview;
import com.example.stewardimperial.adapters.ADPTTableSeatsTaken;
import com.example.stewardimperial.datafetch.DFLogout;
import com.example.stewardimperial.models.MODELTable;
import com.example.stewardimperial.models.MODELTableOrder;
import com.example.stewardimperial.utils.UTILConstants;

public class ACTTableGridLayout extends Activity{

	GridLayout glTableDisp;
	ScrollView svTableContainer;
	LinearLayout llHeaderContainer,llTableBeforeAction,llTableDuringAction;
	Button btHeaderSettings,btJoinTable,btSplitTable,btJoinConfirm,btJoinCancel,btUnOccupy;
	int CONDITION_JOIN = 1,CONDITION_SPLIT = 2,CONDITION_NORMAL = 0,CURRENT_CONDITION = CONDITION_NORMAL,CONDITION_UNOCCUPY=3;
	int viewWidth;
	List<MODELTableOrder> tableToJoin;
	GridView gvTableJoinPreview;
	MODELTableOrder tempModelTableOrder;
	String orderId;
	TextView tvHeader;
	
	//========================================================================OVERRIDDEN METHODS=====================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_gridlayout);

		UTILConstants.ACTIVITY = ACTTableGridLayout.this;
		UTILConstants.HANDLER = new Handler();
		tempData();
		initializer();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		UTILConstants.HANDLER = new Handler();
		UTILConstants.ACTIVITY = ACTTableGridLayout.this;
		gridLayoutPopulate();
	}
	
	//========================================================================CUSTOM METHODS=====================================================================================	

	private void initializer(){ //---------------------- Initializing the instance and listeners of all the elements in this class

		tvHeader = (TextView)findViewById(R.id.tvHeader);
		UTILConstants.setTypefaceCustom(ACTTableGridLayout.this, "", 30, tvHeader,"Table Page");

		glTableDisp = (GridLayout)findViewById(R.id.glTableDisp);
		svTableContainer = (ScrollView)findViewById(R.id.svTableContainer);
		llHeaderContainer = (LinearLayout)findViewById(R.id.llHeaderContainer);
		btHeaderSettings = (Button)findViewById(R.id.btHeaderSettings);
		btHeaderSettings.setOnClickListener(stewardLogOut);
		btJoinTable = (Button)findViewById(R.id.btJoinTable);
		btJoinTable.setOnClickListener(joinTableClick);
		btSplitTable = (Button)findViewById(R.id.btSplitTable);
		btSplitTable.setOnClickListener(splitTableClick);
		btUnOccupy = (Button)findViewById(R.id.btUnoccupyTable);
		btUnOccupy.setOnClickListener(tableUnOccupy);
		llTableBeforeAction = (LinearLayout)findViewById(R.id.llTableBeforeJoin);

		//---------------------------------SIDE_DURING_ACTION---------------------------------------------------------
		llTableDuringAction = (LinearLayout)findViewById(R.id.llTableDuringJoin);
		llTableDuringAction.setVisibility(View.GONE);

		btJoinConfirm = (Button)findViewById(R.id.btJoinConfirm);
		btJoinConfirm.setOnClickListener(joinTableConfirm);
		btJoinCancel = (Button)findViewById(R.id.btJoinCancel);
		btJoinCancel.setOnClickListener(joinTableCancel);

		//--------------------------------POPULATE_TABLE_CALCULATING_SCREEN_WIDTH-------------------------------------
		ViewTreeObserver viewTreeObserver = svTableContainer.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					svTableContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					viewWidth = svTableContainer.getWidth();
					gridLayoutPopulate();
					gvTableJoinPreview = (GridView)findViewById(R.id.gvJoinSplitTablePreview);
				}
			});
		}

	}


	private void gridLayoutPopulate(){ //---------------------- Filling in data into the table grid display

		if (glTableDisp.getChildCount() > 0) {
			glTableDisp.removeAllViews();
		}

		int colSize = 3;
		int rowSize = ((UTILConstants.tableOrderData.size())/colSize)+1;

		glTableDisp.setRowCount(rowSize);
		glTableDisp.setColumnCount(colSize);

		for(int i =0, c = 0, r = 0; i < UTILConstants.tableOrderData.size(); i++, c++)
		{
			if(c == colSize)
			{
				c = 0;
				r++;
			}

			View gridItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_table_select, null);

			final TextView tvTableItem = (TextView)gridItem.findViewById(R.id.tvTableNumber);
			tvTableItem.setVisibility(View.VISIBLE);
			tvTableItem.setTextSize(30);

			GridLayout.LayoutParams param =new GridLayout.LayoutParams();
			//			param.height = LayoutParams.WRAP_CONTENT;
			//			param.width = LayoutParams.WRAP_CONTENT;-

			int tableCount = UTILConstants.tableOrderData.get(i).getTablePerOrder().size();	

			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			//			int width = (this.getResources().getDisplayMetrics().widthPixels-llTableSideLayout.getWidth());
			int width = viewWidth;
			int height = this.getResources().getDisplayMetrics().heightPixels;

			param.height = height/3;
			param.width = (int)(width/3.1);
			if (tableCount > 1) param.width = ((int)((width/6.5)*(2.05)));

			param.setGravity(Gravity.CENTER);

			String tableName = "";
			int x = 0;
			List<Integer> capToDisp = new ArrayList<Integer>();

			for (int j = 0; j < UTILConstants.tableOrderData.get(i).getTablePerOrder().size(); j++) {
				tableName = tableName+"  "+UTILConstants.tableOrderData.get(i).getTablePerOrder().get(j).getTableName().toString();
				//				x = x+Integer.valueOf( UTILConstants.tableOrderData.get(i).getTablePerOrder().get(j).getQuantity());
				x++;
				capToDisp.add(x);
			}

			//			int [] capacity = new int [x];

			final LinearLayout llSelectSeats = (LinearLayout)gridItem.findViewById(R.id.llSeatsTakenGrid);
			llSelectSeats.setVisibility(View.GONE);

			GridView gvSelectSeatsTaken = (GridView)gridItem.findViewById(R.id.gvSeatsTaken);
			gvSelectSeatsTaken.setAdapter(new ADPTTableSeatsTaken(ACTTableGridLayout.this, capToDisp));
			gvSelectSeatsTaken.setTag(""+UTILConstants.tableOrderData.get(i).getOrderId());

			gvSelectSeatsTaken.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {

					String tag = ""+((GridView)arg1.getParent()).getTag();

					Log.v("parent", ""+((GridView)arg1.getParent()).getTag());

					for (int j = 0; j < UTILConstants.tableOrderData.size();j++) {
						if (UTILConstants.tableOrderData.get(j).getOrderId().equals(tag)){
							UTILConstants.tableOrderData.get(j).setSeatsTaken((Integer) arg1.getTag());
							break;
						}
					}

				}
			});

			Button btSelectSeatsTaken = (Button)gridItem.findViewById(R.id.btSeatsTakenConfirm);
			btSelectSeatsTaken.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					tvTableItem.setVisibility(View.VISIBLE);
					llSelectSeats.setVisibility(View.GONE);
				}
			});
			
//			Map<String, Integer> wordMap = new HashMap<String, Integer>();
//			Set<Entry<String, Integer>> set = wordMap.entrySet();
//			List<Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(set); 
//			Collections.sort(list, new Comparator<Entry<String, Integer>>() {
//
//				@Override
//				public int compare(Entry<String, Integer> lhs,Entry<String, Integer> rhs) {
//
//					return (lhs.getValue()).compareTo(rhs.getValue());
//				}
//			});
			
			
			tvTableItem.setText(tableName);

			param.columnSpec = GridLayout.spec(c);
			param.rowSpec = GridLayout.spec(r);

			gridItem.setTag(""+UTILConstants.tableOrderData.get(i).getOrderId());

			gridItem.setId(i);

			gridItem.setLayoutParams (param);
			glTableDisp.addView(gridItem);

			if (UTILConstants.tableOrderData.get(i).getTakenStatus().equals(UTILConstants.TABLE_EMPTY)) {
				gridItem.setBackgroundResource(R.drawable.dialogback);
			}else{
				gridItem.setBackgroundResource(R.drawable.dialogbackdisables);
			}	

			gridItem.setOnLongClickListener(tableItemOnLongClick);
			//			gridItem.setOnDragListener(tableItemDrag);
			gridItem.setOnClickListener(tableItemOnClick); 


			gridItem.setSelected(false);

			if (CURRENT_CONDITION == CONDITION_SPLIT || CURRENT_CONDITION == CONDITION_UNOCCUPY ) {
				if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_SELECTED)) {
					gridItem.setBackgroundResource(R.drawable.custombuttonnormal);
				}else{
					gridItem.setBackgroundResource(R.drawable.dialogback);
				}	
			}

		}
	}


	private void tempData(){ //---------------------- Creating list of tables with OrderId 

		if ((UTILConstants.tableOrderData != null) || (UTILConstants.tableOrderData.size() != 0)) {

			MODELTableOrder modelTableOrder;
			List<MODELTable> listTemp ;

			if (UTILConstants.lsTableData != null || UTILConstants.lsTableData.size() != 0) {

				for (int i = 0; i < UTILConstants.lsTableData.size(); i++) {

					MODELTable modelTable = new MODELTable(UTILConstants.lsTableData.get(i));
					modelTableOrder = new MODELTableOrder();
					modelTableOrder.setOrderId(modelTable.getId()+""+UTILConstants.timeStampGenerator());
					listTemp = new ArrayList<MODELTable>();
					listTemp.add(modelTable);
					modelTableOrder.setTablePerOrder(listTemp);
					modelTableOrder.setTakenStatus(UTILConstants.TABLE_EMPTY);
					modelTableOrder.setIsSelected(UTILConstants.TABLE_UNSELECTED);
					modelTableOrder.setJoinNormalSplit(UTILConstants.TABLE_NORMAL);
					UTILConstants.tableOrderData.add(modelTableOrder);
				}
			}
		}
	}


	//========================================================================CLICK LISTENERS=====================================================================================

	private OnClickListener stewardLogOut = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new DFLogout().logout(ACTTableGridLayout.this);
		}
	};


	private OnClickListener tableItemOnClick = new OnClickListener() { //---------------------- Table grid item click

		@Override
		public void onClick(View v) {

			switch (CURRENT_CONDITION) {

			case 0: //condition normal

				for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
					if (UTILConstants.tableOrderData.get(i).getOrderId().equals(v.getTag())) {
						if (UTILConstants.tableOrderData.get(i).getSeatsTaken()!=0) {
							Intent intent = new Intent(getApplicationContext(), ACTOrder.class);
							intent.putExtra("ORDERID",""+v.getTag());
							startActivity(intent);
						}else{
							Toast.makeText(getApplicationContext(), "Please select seat taken", Toast.LENGTH_SHORT).show();
						}
					}
				}

				break;

			case 1: //condition join

				boolean canJoin = false;

				for (int j = 0; j < UTILConstants.tableOrderData.size(); j++) {
					if (UTILConstants.tableOrderData.get(j).getOrderId().equals(v.getTag())) {
						if (UTILConstants.tableOrderData.get(j).getTakenStatus().equals(UTILConstants.TABLE_EMPTY)) {

							canJoin = true;
							break;
						}
					}
				}

				if (canJoin) {

					if (!v.isSelected()) { //---------------------------TABLE NOT ADDED
						v.setSelected(true);
						v.setBackgroundResource(R.drawable.custombuttonnormal);

						for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
							if (v.getTag().equals(UTILConstants.tableOrderData.get(i).getOrderId())) {
								tableToJoin.add(UTILConstants.tableOrderData.get(i));
							}
						}

						gvTableJoinPreview.setAdapter(new ADPTTableJoinPreview(ACTTableGridLayout.this, tableToJoin));

					}else{//---------------------------TABLE ADDED
						v.setSelected(false);
						v.setBackgroundResource(R.drawable.dialogback);	

						if (tableToJoin.size() != 0) {
							for (int i = 0; i < tableToJoin.size(); i++) {
								if (v.getTag().equals(tableToJoin.get(i).getOrderId())) {
									tableToJoin.remove(tableToJoin.get(i));
								}
							}	
							gvTableJoinPreview.setAdapter(new ADPTTableJoinPreview(ACTTableGridLayout.this, tableToJoin));
						}
					}
				}else{
					Toast.makeText(getApplicationContext(), "Table taken", Toast.LENGTH_LONG).show();
				}

				break;

			case 2: //condition split

				boolean canSplit = false;

				for (int j = 0; j < UTILConstants.tableOrderData.size(); j++) {
					if (UTILConstants.tableOrderData.get(j).getOrderId().equals(v.getTag())) { //12013114105412
						if (UTILConstants.tableOrderData.get(j).getTakenStatus().equals(UTILConstants.TABLE_EMPTY)) {

							canSplit = true;
							break;
						}
					}
				}

				if (canSplit) {

					for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
						if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_SELECTED)) {
							UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_UNSELECTED);
						}
					}

					for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {

						if (v.getTag().equals(UTILConstants.tableOrderData.get(i).getOrderId())) {
							if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_UNSELECTED)) {
								UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_SELECTED);
								break;
							}else{
								UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_UNSELECTED);
								break;
							}
						}
					}

					gridLayoutPopulate();
				}else{
					Toast.makeText(getApplicationContext(), "Table taken", Toast.LENGTH_LONG).show();
				}
				break;

			case 3: //Condition UnJoin or UnSplit

				orderId = v.getTag().toString();

				for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {

					if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_SELECTED)) {
						UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_UNSELECTED);
					}
				}

				for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {

					if (v.getTag().equals(UTILConstants.tableOrderData.get(i).getOrderId())) {
						if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_UNSELECTED)) {
							UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_SELECTED);
							orderId = ""+v.getTag();
							break;
						}else{
							UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_UNSELECTED);
							break;
						}
					}
				}

				for (int j = 0; j < UTILConstants.tableOrderData.size(); j++) {
					if (UTILConstants.tableOrderData.get(j).getOrderId().equals(v.getTag())) {
						UTILConstants.tableOrderData.get(j).setTakenStatus(UTILConstants.TABLE_EMPTY);
						break;
					}
				}


				gridLayoutPopulate();

				break;

			default:
				break;
			}

		}
	};

	
	private OnClickListener joinTableClick = new OnClickListener() { //---------------------- Select Join option

		@Override
		public void onClick(View v) {

			tableToJoin = new ArrayList<MODELTableOrder>();

			CURRENT_CONDITION = CONDITION_JOIN;
			llTableBeforeAction.setVisibility(View.GONE);
			llTableDuringAction.setVisibility(View.VISIBLE);

			gvTableJoinPreview.setAdapter(new ADPTTableJoinPreview(ACTTableGridLayout.this, tableToJoin));


		}
	};


	private OnClickListener tableUnOccupy = new OnClickListener() { //---------------------- Select Join option

		@Override
		public void onClick(View v) {

			tableToJoin = new ArrayList<MODELTableOrder>();

			CURRENT_CONDITION = CONDITION_UNOCCUPY;
			llTableBeforeAction.setVisibility(View.GONE);
			llTableDuringAction.setVisibility(View.VISIBLE);

			gvTableJoinPreview.setAdapter(new ADPTTableJoinPreview(ACTTableGridLayout.this, tableToJoin));

		}
	};


	private OnLongClickListener tableItemOnLongClick = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {

			TextView tvTableItem = (TextView)v.findViewById(R.id.tvTableNumber);
			tvTableItem.setVisibility(View.GONE);

			LinearLayout llSelectSeats = (LinearLayout)v.findViewById(R.id.llSeatsTakenGrid);
			llSelectSeats.setVisibility(View.VISIBLE);


			return true;
		}
	};


	private OnClickListener splitTableClick = new OnClickListener() { //---------------------- Select Split option

		@Override
		public void onClick(View v) {

			tableToJoin = new ArrayList<MODELTableOrder>();

			CURRENT_CONDITION = CONDITION_SPLIT;

			llTableBeforeAction.setVisibility(View.GONE);
			llTableDuringAction.setVisibility(View.VISIBLE);
			gvTableJoinPreview.setAdapter(new ADPTTableJoinPreview(ACTTableGridLayout.this, tableToJoin));
		}
	};


	private OnClickListener joinTableConfirm = new OnClickListener() { //---------------------- Confirm the option selected

		@Override
		public void onClick(View v) {
			if (CURRENT_CONDITION == CONDITION_JOIN) {
				new makeJoinAsync().execute();

			}else if(CURRENT_CONDITION == CONDITION_SPLIT){
				new makeSplit().execute();

			}else if(CURRENT_CONDITION == CONDITION_UNOCCUPY){
				if (orderId != "" && orderId != null) {
					new reinstance(orderId).execute();	
				}
			}

		}
	};


	private OnClickListener joinTableCancel = new OnClickListener() { //---------------------- Cancel the option selected

		@Override
		public void onClick(View v) {
			tableToJoin = new ArrayList<MODELTableOrder>();
			orderId = "";
			CURRENT_CONDITION = CONDITION_NORMAL;
			llTableBeforeAction.setVisibility(View.VISIBLE);
			llTableDuringAction.setVisibility(View.GONE);
			gridLayoutPopulate();
		}
	};



	//=========================================================================CUSTOME_CLASS===========================================================================================	


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
					}else{

						for (int i4 = 0; i4 < UTILConstants.tableOrderData.size(); i4++) {

							if (orderId.equals(UTILConstants.tableOrderData.get(i4).getOrderId())) {
								if (UTILConstants.tableOrderData.get(i4).getIsSelected().equals(UTILConstants.TABLE_SELECTED)) {
									UTILConstants.tableOrderData.get(i4).setIsSelected(UTILConstants.TABLE_UNSELECTED);
									break;
								}
							}
						}

					}

				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void tableName) {
			super.onPostExecute(tableName);

			//			for (int i = 0; i < tableName.size(); i++) {
			//				UTILConstants.tableOrderData.add(tableName.get(i));
			//			}
			//

			CURRENT_CONDITION = CONDITION_NORMAL;
			Collections.sort(UTILConstants.tableOrderData, Collections.reverseOrder(MODELTableOrder.tableIdComparator));
			llTableBeforeAction.setVisibility(View.VISIBLE);
			llTableDuringAction.setVisibility(View.GONE);
			gridLayoutPopulate();
		}
	}


	private class makeJoinAsync extends  AsyncTask<Void, Void, Void>{ //---------------------- Async task for making the join

		@Override
		protected Void doInBackground(Void... params) {



			List<MODELTable> listTemp = new ArrayList<MODELTable>();

			//-------------------------ADDING ALL THE TABLES INTO TABLE LIST-------------------------------------------------
			for (int j = 0; j < tableToJoin.size(); j++) {
				for (int j2 = 0; j2 < tableToJoin.get(j).getTablePerOrder().size(); j2++) {
					listTemp.add((MODELTable)tableToJoin.get(j).getTablePerOrder().get(j2));
				}

			}

			//-------------------------DELETING ALL THE RECORDS ACCORDING TO THE ORDER ID-------------------------------------------------
			for (int j = 0; j < tableToJoin.size(); j++) {
				for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
					if (UTILConstants.tableOrderData.get(i).getOrderId().equals(""+tableToJoin.get(j).getOrderId())) {
						UTILConstants.tableOrderData.remove(i);
						break;

					}
				}	
			}

			//-------------------------CREATING A NEW RECORD WITH REQUIRED DATA-------------------------------------------------
			MODELTableOrder modelTableOrder = new MODELTableOrder();
			modelTableOrder.setOrderId(tableToJoin.get(0).getOrderId());
			modelTableOrder.setSeatsTaken(0);
			modelTableOrder.setTakenStatus(UTILConstants.TABLE_EMPTY);
			modelTableOrder.setTablePerOrder(listTemp);
			modelTableOrder.setIsSelected(UTILConstants.TABLE_UNSELECTED);
			modelTableOrder.setJoinNormalSplit(UTILConstants.TABLE_JOINED);

			UTILConstants.tableOrderData.add(modelTableOrder);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			llTableBeforeAction.setVisibility(View.VISIBLE);
			llTableDuringAction.setVisibility(View.GONE);

			CURRENT_CONDITION = CONDITION_NORMAL;

			gridLayoutPopulate();

		}

	}


	private class makeSplit extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {

			MODELTableOrder modelTableOrder;
			for (int i = 0; i < UTILConstants.tableOrderData.size(); i++) {
				modelTableOrder = new MODELTableOrder(UTILConstants.tableOrderData.get(i));
				if (UTILConstants.tableOrderData.get(i).getIsSelected().equals(UTILConstants.TABLE_SELECTED)) {
					//					modelTableOrder = UTILConstants.tableOrderData.get(i);
					modelTableOrder.setOrderId(modelTableOrder.getTablePerOrder().get(0).getId()+""+UTILConstants.timeStampGenerator());
					modelTableOrder.getTablePerOrder().get(0).setTableName(modelTableOrder.getTablePerOrder().get(0).getTableName()+"B");
					modelTableOrder.setTakenStatus(UTILConstants.TABLE_EMPTY);
					modelTableOrder.setIsSelected(UTILConstants.TABLE_UNSELECTED);
					modelTableOrder.setJoinNormalSplit(UTILConstants.TABLE_SPLIT);

					UTILConstants.tableOrderData.get(i).getTablePerOrder().get(0).setTableName(UTILConstants.tableOrderData.get(i).getTablePerOrder().get(0).getTableName()+"A");
					UTILConstants.tableOrderData.add(modelTableOrder);
					UTILConstants.tableOrderData.get(i).setIsSelected(UTILConstants.TABLE_UNSELECTED);
					UTILConstants.tableOrderData.get(i).setJoinNormalSplit(UTILConstants.TABLE_SPLIT);
					break;
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			CURRENT_CONDITION = CONDITION_NORMAL;

			llTableBeforeAction.setVisibility(View.VISIBLE);
			llTableDuringAction.setVisibility(View.GONE);

			gridLayoutPopulate();

		}

	}

	
	//=======================================================================PING METHODS==========================================================================================
	
	
		public void onOrderCooked(){
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
//					MODELResultSet modelResultSet = new MODELResultSet();
//					modelResultSet = new DFOrderCooked().cookedOrder(ACTTableGridLayout.this);
//					Log.v("ON PING", ""+modelResultSet);
				}
			}).start();;
			
		}

}
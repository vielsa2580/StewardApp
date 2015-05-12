package com.example.stewardimperial.presentors;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stewardimperial.datafetch.DFCategory;
import com.example.stewardimperial.datafetch.DFLogin;
import com.example.stewardimperial.datafetch.DFMenuItem;
import com.example.stewardimperial.datafetch.DFTables;
import com.example.stewardimperial.models.MODELMainCategory;
import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.models.MODELTable;
import com.example.stewardimperial.socketprogramming.SRVReceiver;
import com.example.stewardimperial.utils.UTILConstants;
import com.example.stewardimperial.utils.UTILSharedPreference;

public class ACTLogin extends Activity{

	EditText etLoginName, etLoginPass;
	Button btLogin;

	//=============================================================================OVER_RIDDEN METHODS=============================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		UTILConstants.ACTIVITY = ACTLogin.this;
		UTILConstants.HANDLER = new Handler();
		
		
		if(isMyServiceRunning()){
			stopService(new Intent(ACTLogin.this,SRVReceiver.class));
		}
		startService(new Intent(ACTLogin.this,SRVReceiver.class));	

		
		initializer();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		UTILConstants.ACTIVITY = ACTLogin.this;
		UTILConstants.HANDLER = new Handler();
		
	}
	
	@Override
	protected void onDestroy() {
		this.finish();
		super.onDestroy();
	}



	//=============================================================================CUSTOM METHODS=============================================================================================

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.example.stewardimperial.socketprogramming.SRVReceiver".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void initializer(){
		etLoginName = (EditText)findViewById(R.id.etLoginName);
		etLoginPass = (EditText)findViewById(R.id.etLoginPass);

		etLoginName.setText("steward1");
		UTILSharedPreference.setPreference(ACTLogin.this, UTILConstants.USER_NAME, etLoginName.getText().toString());
		etLoginPass.setText("steward1");

		btLogin = (Button)findViewById(R.id.btLogin);
		btLogin.setOnClickListener(loginClick);
	}


	//----------------------------------------------------------------CLICK_LISTENERS-------------------------------------------------------

	OnClickListener loginClick = new OnClickListener() { // onClick of login button

		@Override
		public void onClick(View v) {
			if (etLoginName.getText().toString() != null && etLoginPass.getText().toString() != null 
					&& etLoginName.getText().toString().length() !=0 && etLoginPass.getText().toString().length() !=0){

				if (isNetworkAvailable()) {
					new makeLogin().execute("");	
				}else{

					final AlertDialog alertDialog = new AlertDialog.Builder(ACTLogin.this).create();
					alertDialog.setTitle("Warnig");
					alertDialog.setMessage("Please check internet connection");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,int which) 
						{
							alertDialog.dismiss();
						}
					});
					alertDialog.show();

				}

			}
		}
	};


	private class makeLogin extends AsyncTask<String, Void, MODELResultSet>{ // fire login to control

		@Override
		protected MODELResultSet doInBackground(String... params) {
			return new DFLogin().login(etLoginName.getText().toString(), etLoginPass.getText().toString());
		}

		@Override
		protected void onPostExecute(MODELResultSet result) {

			JSONObject jsonObject = new JSONObject();
			jsonObject = result.getJsonObject();

			try {

				if (result.getError().toString().equals("success")) {

					if (jsonObject.get("data") != null) {

						JSONObject jsonObject2 = new JSONObject();
						jsonObject2 = (JSONObject) jsonObject.get("data");

						if (jsonObject2.getString("branchId").length() != 0) {
							UTILConstants.BRANCH_ID = jsonObject2.getString("branchId");
							new dataRetrieval().execute();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Null data", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), ""+result.getMessage(), Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

	}

	public class dataRetrieval extends AsyncTask<Void,Void, String>	{ // Receive data after login

		@Override
		protected String doInBackground(Void... params) {

			String returnValue = "";

			MODELResultSet modelResultSetTable = new MODELResultSet();
			MODELResultSet modelResultSetCat = new MODELResultSet();
			MODELResultSet modelResultSetMenu = new MODELResultSet();

			modelResultSetTable = new DFTables().retrieveTables();
			modelResultSetCat = new DFCategory().retrieveCategories();
			modelResultSetMenu = new DFMenuItem().retrieveMenuItem();

			if (modelResultSetTable.getError().toString().equals(UTILConstants.SUCCESS_MSG)) {
				UTILConstants.lsTableData = new ArrayList<MODELTable>(); 
				UTILConstants.lsTableData = (List<MODELTable>) modelResultSetTable.getList();
				returnValue = "";
			}
			if (modelResultSetCat.getError().toString().equals(UTILConstants.SUCCESS_MSG)) {
				UTILConstants.lsCategory = new ArrayList<MODELMainCategory>(); 
				UTILConstants.lsCategory = (List<MODELMainCategory>) modelResultSetCat.getList();
				returnValue = "";
			}	
			if (modelResultSetMenu.getError().toString().equals(UTILConstants.SUCCESS_MSG)) {
				UTILConstants.lsMenuItem = new ArrayList<MODELMenuItems>();
				UTILConstants.lsMenuItem = (List<MODELMenuItems>) modelResultSetMenu.getList();
				returnValue = "";
			}	

			if(modelResultSetTable.getError().toString().equals(UTILConstants.ERROR_MSG))returnValue = modelResultSetTable.getMessage();

			return returnValue;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == "")
			{
				startActivity(new Intent(ACTLogin.this,ACTTableGridLayout.class));
			}else{
				Toast.makeText(getApplicationContext(),result ,Toast.LENGTH_LONG).show();
			}
		}

	}

}

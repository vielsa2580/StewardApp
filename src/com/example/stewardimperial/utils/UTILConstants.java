package com.example.stewardimperial.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.TextView;

import com.example.stewardimperial.models.MODELMainCategory;
import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.models.MODELTable;
import com.example.stewardimperial.models.MODELTableOrder;

public class UTILConstants {

	public static String serverurl = "http://192.168.1.106/";
	public static List<MODELTable> lsTableData = new ArrayList<MODELTable>();

	public static List<MODELMainCategory> lsCategory = new ArrayList<MODELMainCategory>();
	public static List<MODELMenuItems> lsMenuItem = new ArrayList<MODELMenuItems>();

	public static List<MODELTableOrder> tableOrderData = new ArrayList<MODELTableOrder>();

	public static String BRANCH_ID = "";
	public static String SUCCESS_MSG = "success";
	public static String ERROR_MSG = "Error";
	public static String TABLE_TAKEN = "taken";
	public static String TABLE_EMPTY = "empty";
	public static String TABLE_SELECTED = "selected";
	public static String TABLE_UNSELECTED = "unselected";
	public static String TABLE_JOINED = "joined";
	public static String TABLE_SPLIT = "split";
	public static String TABLE_NORMAL = "normal";
	public static String USER_NAME = "user_name";
	public static String STATUS_COOKING = "status_cooking";
	public static String STATUS_COOKED = "status_cooked";
	public static int TCPRECEIVERPORT=12370;
	
	public static Activity ACTIVITY;
	public static Handler HANDLER;

	//====================================================CUSTOM METHODS==================================================================

	public static String timeStampGenerator(){

		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		return ""+year+""+month+""+date+""+hour+""+minute+""+seconds;
	}

	public static String timeStampGeneratorWithFormat(){

		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

//		return ""+year+"-"+month+"-"+date+"-"+hour+"-"+minute+"-"+seconds;
		return ""+date+"-"+month+"-"+year+"_"+hour+":"+minute+":"+seconds;
	}

	
	public static void setTypefaceCustom(Activity activity,String typeface,int size,TextView textview,String textToEnter){

		if (typeface != null && typeface != "") {
			Typeface font = Typeface.createFromAsset(activity.getAssets(),typeface);
			textview.setTypeface(font);
		}
		
		textview.setTextSize(size);
		textview.setText(textToEnter);
	}

//	public static String getDeviceImei(Activity activity){
//		TelephonyManager mngr = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE); 
//		return mngr.getDeviceId();
//	}

	
	public static String getMacId(Activity activity){
		WifiManager manager = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String address = info.getMacAddress();
		return address;
	}
	
	//===================================================SET_VALUES=======================================================================

	public static int HTTP_GET = 1;
	public static int HTTP_POST = 2;
}

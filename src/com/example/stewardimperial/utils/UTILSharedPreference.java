package com.example.stewardimperial.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UTILSharedPreference {

	


	public static String getPreference(Activity activity,String prefKey){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		return sharedPreferences.getString(prefKey, null);
	}


	public static void setPreference(Activity activity, String prefKey, String prefValue) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(prefKey,prefValue);
		editor.commit();
	}
	
	public static String getPreference(Context context,String prefKey){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getString(prefKey, null);
	}
	
	public static void setPreference(Context context, String prefKey, String prefValue) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(prefKey,prefValue);
		editor.commit();
	}
	
	
	public static Integer getIntegerPreference(Activity activity,String prefKey){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		return sharedPreferences.getInt(prefKey, 0);
	}


	public static void setIntegerPreference(Activity activity, String prefKey, Integer prefValue) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(prefKey,prefValue);
		editor.commit();
	}
	
	

	public static Boolean getBooleanPreference(Context context,String prefKey){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(prefKey, false);
	}
	
	public static void setBooleanPreference(Context context, String prefKey, Boolean prefValue) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(prefKey,prefValue);
		editor.commit();
	}
	

}

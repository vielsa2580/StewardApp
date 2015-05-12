package com.example.stewardimperial.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.stewardimperial.socketprogramming.SRVReceiver;

public class BROnBootup extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		arg0.startService(new Intent(arg0,SRVReceiver.class));	
		
	}
	
	

}

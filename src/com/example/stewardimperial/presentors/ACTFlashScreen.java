package com.example.stewardimperial.presentors;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ACTFlashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp);

		/*Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				startActivity(new Intent(ACTFlashScreen.this,ACTLogin.class));
				ACTFlashScreen.this.finish();
			}
		};timer.schedule(task, 1000);*/
			
	}

	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ACTFlashScreen.this.finish();
	}
}

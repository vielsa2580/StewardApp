package com.example.stewardimperial.custom;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.stewardimperial.presentors.R;


public class CustomButton extends LinearLayout {

	Activity activity;
	Button btCustomSmall,btCustomMedium,btCustomLarge;

	public CustomButton(Activity activity) {
		super(activity);
		this.activity = activity;
	}


	public View singleSelectButton(){

//		View retView = activity.getLayoutInflater().inflate(R.layout.cutom_button,null);

		btCustomSmall = new Button(activity);//(Button)retView.findViewById(R.id.btCustomSmall);
		btCustomMedium = new Button(activity);//(Button)retView.findViewById(R.id.btCustomMedium);
		btCustomLarge = new Button(activity);//(Button)retView.findViewById(R.id.btCustomLarge);
		
		btCustomSmall.setBackgroundResource(R.layout.custom_button_click);
		btCustomMedium.setBackgroundResource(R.layout.custom_button_click);
		btCustomLarge.setBackgroundResource(R.layout.custom_button_click);
		
		LinearLayout llHolder = new LinearLayout(activity);
		llHolder.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		llHolder.setOrientation(LinearLayout.HORIZONTAL);

		llHolder.setWeightSum(3);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		p.weight = 1;
		
		btCustomSmall.setLayoutParams(p);
		btCustomMedium.setLayoutParams(p);
		btCustomLarge.setLayoutParams(p);
		
		btCustomSmall.setId(0);
		btCustomMedium.setId(1);
		btCustomLarge.setId(2);
		
		llHolder.addView(btCustomSmall, 0);
		llHolder.addView(btCustomMedium, 1);
		llHolder.addView(btCustomLarge, 2);
		
		btCustomSmall.setText("Small");


		return llHolder;
	}

}
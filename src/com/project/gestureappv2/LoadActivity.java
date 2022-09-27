package com.project.gestureappv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoadActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		Button bImageActivity = (Button) findViewById(R.id.load_image_activity);
		bImageActivity.setOnClickListener(this);
		
		Button bKeyboardActivity = (Button) findViewById(R.id.load_keyboard_activity);
		bKeyboardActivity.setOnClickListener(this);
		
		//DatabaseHandler dh = DatabaseHandler.getInstance(this);
		//dh.deleteAll();
	}

	@Override
	public void onClick(View v) {
		try {
			//Load Image Activity
			if (((Button) v).getText().equals("Load Image Activity")) {
				Intent i = new Intent(this, ImageActivity.class);
				startActivity(i);
			}
			
			//Load Keyboard Activity
			if (((Button) v).getText().equals("Load Keyboard Activity")) {
				Intent i = new Intent(this, KeyboardActivity.class);
				startActivity(i);
			}
		} catch (Exception e) {

		}

	}
}

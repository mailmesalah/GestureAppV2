package com.project.gestureappv2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class KeyboardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyboard);
		
		EditText keyboardText=(EditText)findViewById(R.id.keyboardText);
		keyboardText.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { 
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            	String currentDateandTime = sdf.format(new Date());
            	 writeToText("Pressed Character \""+s+"\" on "+currentDateandTime);
                    
            }                       
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                                          
            }                       
            @Override
            public void afterTextChanged(Editable s) {
                                          

            }
        });
		
		keyboardText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
								
				return false;
			}
		});
	}
	
	
	private static void writeToText(String text){
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File (sdCard.getAbsolutePath() + "/GestureApp");

		if (!dir.exists()) {
			dir.mkdir();
        }
		
		File file = new File(dir, "KeyboardEvents.txt");

		
		try {
			FileOutputStream f = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(f);
		    myOutWriter.append(text);
		    myOutWriter.close();
		    f.close();
		} catch (FileNotFoundException e) {
			Log.d("Error: ", e.getMessage());
		} catch (IOException e) {
			Log.d("Error: ", e.getMessage());
		}
	}
}

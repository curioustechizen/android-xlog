package com.github.curioustechizen.xlog.sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.github.curioustechizen.xlog.Log;

public class MainActivity extends Activity {

	private static final String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "In onCreate, savedInstanceState = " + savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    
    public void onBtnClick(View v){
    	if(v.getId() == R.id.button1){
    		triggerLog();
    	}
    }


	private void triggerLog() {
		try {
			FileWriter filewriter = new FileWriter(new File("/nonexistent/path/dummy.file"));
		} catch (IOException e) {
			Log.e(LOG_TAG, "Could not open file for writing", e);
		}
		
	}
    
    
    
}

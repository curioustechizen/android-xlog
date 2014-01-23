package com.github.curioustechizen.xlog.sample;

import java.io.File;
import java.io.IOException;

import android.app.Application;

import com.github.curioustechizen.xlog.Log;

public class XlogSampleApplication extends Application {
	
	private static final boolean LOG_TO_FILE = true;
	private File logFilePath;
	
	@Override
	public void onCreate() {
		super.onCreate();
		logFilePath = new File(getFilesDir(), "XlogAppLog.txt");
		try {
			Log.init(this, LOG_TO_FILE, logFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("XlogSampleApplication", "In onCreate() after Log.init()");
	}

}

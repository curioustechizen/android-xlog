/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.curioustechizen.xlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

/**
 *  
 * Wrapper around {@code android.util.Log} that optionally logs to a file in addition to logging to LogCat.
 * This is useful as a debugging tool to get logs from users.
 * <p/>
 * This is <strong>not</strong> meant as a replacement for more sophisticated solutions like <a href="http://acra.ch/">ACRA</a>.
 * <p/>
 * The goal is to have the <em>same API</em> as {@code android.util.Log} so that your existing code that uses {@code android.util.Log} can be made to log to a file by simply changing the imports. 
 * There is one additional step required - initialize the Log with the file path. This can be done once per app, for example in your sub-class of {@code android.app.Application}. 
 * <p/>
 * Note that logging to a file is expensive and should only be present in special debug builds (not even in your regular debug builds - which might write only to LogCat). This is the reason an additional {@code boolean} has been provided in the {@code init} method.
 *
 *
 *	@author Kiran Rao
 */
public final class Log {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private static final String LOG_TIME_STAMP_FORMAT = "HH:mm:ss.SSS";

    private static File sLogFile;
    private static BufferedWriter sBufferedFileWriter;
    private static boolean sLogToFile;
    
    /*
     * The context is not used currently, but it might be used in a future release - for example - to create a Share Intent for the log contents.
     */
    private static Context sContext;
    
  
        
    private Log() {
    }
    
    /**
     * Initialize the logger. This is typically done once per app.
     * <br>
     * If this method is not called to initialize the logger, then this class behaves just like {@code android.util.Log}.
     * @param context
     * 		The Android context.
     * @param logToFile 
     * 		Whether to log to file. 
     * 		If {@code false}, then all subsequent calls will just log to LogCat.
     * 		If {@code true}, then all subsequent calls will log to both LogCat as well as to the specified file.
     * @param file
     * 		The file to log to. This file will be cleared if it already exists.
     * @throws IOException
     */
    public static void init(Context context, boolean logToFile, File file) throws IOException{
    	sContext = context;
    	sLogToFile = logToFile;
    	if(sLogToFile){
    		sLogFile = file;
    		sBufferedFileWriter = new BufferedWriter(new FileWriter(sLogFile, false));
        	
    	}
    }
    
    private static void ensurePathInitialized(){
    	if(sLogFile == null) 
    		throw new IllegalStateException("File path not initialized. Have you called Log.init() method?");
    }
    
    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        return printlnInternal(VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        return printlnInternal(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        return printlnInternal(DEBUG, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        return printlnInternal(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        return printlnInternal(INFO, tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        return printlnInternal(INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        return printlnInternal(WARN, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        return printlnInternal(WARN, tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     *
     *  The default level of any tag is set to INFO. This means that any level above and including
     *  INFO will be logged. Before you make any calls to a logging method you should check to see
     *  if your tag should be logged. You can change the default level by setting a system property:
     *      'setprop log.tag.&lt;YOUR_LOG_TAG> &lt;LEVEL>'
     *  Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
     *  turn off all logging for your tag. You can also create a local.prop file that with the
     *  following in it:
     *      'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>'
     *  and place that in /data/local.prop.
     *
     * @param tag The tag to check.
     * @param level The level to check.
     * @return Whether or not that this is allowed to be logged.
     * @throws IllegalArgumentException is thrown if the tag.length() > 23.
     */
    public static boolean isLoggable(String tag, int level){
    	return android.util.Log.isLoggable(tag, level);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        return printlnInternal(WARN, tag, getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        return printlnInternal(ERROR, tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        return printlnInternal(ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }

    


    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String msg) {
        return printlnInternal(priority, tag, msg);
    }
    
    
    private static int printlnInternal(int priority, String tag, String msg){
    	if(sLogToFile){
    		ensurePathInitialized();
    		SimpleDateFormat sdf = new SimpleDateFormat(LOG_TIME_STAMP_FORMAT);
        	StringBuilder sb = 
        			new StringBuilder(sdf.format(new Date()))
        				.append("\t").append(getDisplayForPriority(priority))
        				.append("\t").append(tag)
        				.append("\t").append(msg);
    		try {
    			if(sBufferedFileWriter != null){
    				sBufferedFileWriter.write(sb.toString(), 0, sb.length());
    				sBufferedFileWriter.newLine();
    				sBufferedFileWriter.flush();
    			}
    			
    		} catch (IOException e) {
    			/*
    			 * If there is any problem while writing the log, just print to logcat and continue.
    			 * Don't crash or abort!
    			 */
    			e.printStackTrace();
    		}
    	}
		return android.util.Log.println(priority, tag, msg);
    }
    private static final String [] PRIORITY_DISPLAY_STRINGS = {"", "", "V", "D", "I", "W" , "E", "A"};
    
	private static String getDisplayForPriority(int priority) {
		return PRIORITY_DISPLAY_STRINGS[priority];
	}
}
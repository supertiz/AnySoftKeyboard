package it.tiz.moduli;

import android.util.Log;

import java.util.Locale;


public class LogModule {
	private static final String LOG_TAG = "it.tiz";
	
	private LogModule(){}
	
	public static void debug(String msg){
		Log.d(LOG_TAG, buildMsg(msg));
	}
	public static void info(String msg){
		Log.i(LOG_TAG, buildMsg(msg));
	}
	public static void error(String msg){
		Log.e(LOG_TAG, buildMsg(msg));
	}
	
	private static String buildMsg(String msg){
		String callerClassName=new Throwable().getStackTrace()[2].getClassName();
		return String.format(Locale.getDefault(),"%1$s - %2$s",callerClassName,msg);
	}
	
}

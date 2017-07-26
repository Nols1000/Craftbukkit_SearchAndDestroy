package com.github.Nols1000.SAD.dev;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log.Level;

import com.github.Nols1000.SAD.config.Config;
import com.github.Nols1000.SAD.config.LoggerData;

public class Logger {

	private static boolean isInit = false;
	
	private final static java.util.logging.Logger logger = Bukkit.getLogger(); 
	
	private static boolean infoEnabled    = true;
	private static boolean debugEnabled   = true;
	private static boolean warningEnabled = true;
	private static boolean errorEnabled   = true; // Highly Recommended
	private static boolean tracerEnabled  = true;
	
	public static void log(java.util.logging.Level l, String msg){
		
		if(!isInit)
			init();
		
		if(l.equals(Level.INFO) && infoEnabled){
			
			logger.log(l, msg);
		}else if(l.equals(Level.DEBUG) && debugEnabled){
			
			logger.log(l, msg);
		}else if(l.equals(Level.WARN) && warningEnabled){
			
			logger.log(l, msg);
		}else if(l.equals(Level.ERROR) && errorEnabled){
			
			logger.log(l, msg);
		}else if(l.equals(Level.TRACE) && tracerEnabled){
			
			logger.log(l, msg);
		}
	}
	
	public static void init(){
		
		LoggerData ld = Config.getLoggerData();
		
		if(ld != null){
			
			infoEnabled    = ld.isInfoEnabled();
			debugEnabled   = ld.isDebugEnabled();
			warningEnabled = ld.isWarningEnabled();
			errorEnabled   = ld.isErrorEnabled();
			
			isInit = true;
		}
	}
}

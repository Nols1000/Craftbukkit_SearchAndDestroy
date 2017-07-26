package com.github.Nols1000.SAD.config;

public class LoggerData {
	
	private boolean error;
	private boolean warning;
	private boolean debug;
	private boolean info;
	
	public LoggerData(boolean e, 
					  boolean w, 
					  boolean d, 
					  boolean i){
		
		error   = e;
		warning = w;
		debug   = d;
		info    = i;
	}
	
	public boolean isErrorEnabled(){
		
		return error;
	}
	
	public boolean isWarningEnabled(){
		
		return warning;
	}
	
	public boolean isDebugEnabled(){
		
		return debug;
	}
	
	public boolean isInfoEnabled(){
		
		return info;
	}
}

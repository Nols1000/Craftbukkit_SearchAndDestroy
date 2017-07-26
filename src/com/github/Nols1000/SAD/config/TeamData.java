package com.github.Nols1000.SAD.config;

public class TeamData {
	
	private String name;
	private String prefix;
	
	private int    maxplayer;
	
	public TeamData(String n, String pre, int mp){
		
		name      = n;
		prefix    = pre;
		maxplayer = mp;
	}

	public String getName() {
		
		return name;
	}

	public String getPrefix() {
		
		return prefix;
	}

	public int getMaxplayer() {
		
		return maxplayer;
	}
}

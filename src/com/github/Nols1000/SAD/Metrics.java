package com.github.Nols1000.SAD;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class Metrics {
	
	org.mcstats.Metrics metrics;
	
	public Metrics(JavaPlugin plugin){
		
		try {
			
			metrics = new org.mcstats.Metrics(plugin);
			metrics.start();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

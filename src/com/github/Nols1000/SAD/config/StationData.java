package com.github.Nols1000.SAD.config;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class StationData {
	
	private ArrayList<Location[]> station = new ArrayList<Location[]>();
	
	private int power;
	private boolean fire;
	private boolean destroy;
	
	public StationData(ConfigurationSection stationSection, int p, boolean f, boolean d){
		
		for(int i = 0; stationSection.isSet(""+i); i++){
			
			int x1 = stationSection.getInt(i+".one.x");
			int y1 = stationSection.getInt(i+".one.y");
			int z1 = stationSection.getInt(i+".one.z");
			
			String world1 = stationSection.getString(i+".one.w");
			
			int x2 = stationSection.getInt(i+".two.x");
			int y2 = stationSection.getInt(i+".two.y");
			int z2 = stationSection.getInt(i+".two.z");
			
			String world2 = stationSection.getString(i+".two.w");
			
			station.add(new Location[]{new Location(Bukkit.getWorld(world1), x1, y1, z1), new Location(Bukkit.getWorld(world2), x2, y2, z2)});
		}
		
		power   = p;
		fire    = f;
		destroy = d;
	}
	
	public ArrayList<Location[]> getStationLocation(){
		
		return station;
	}
	
	public int getPower(){
		
		return power;
	}
	
	public boolean canFire(){
		
		return fire;
	}
	
	public boolean canDestroy(){
		
		return destroy;
	}
}

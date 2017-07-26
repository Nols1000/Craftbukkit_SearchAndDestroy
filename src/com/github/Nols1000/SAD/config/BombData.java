package com.github.Nols1000.SAD.config;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class BombData {
	
	ArrayList<Location> bomb = new ArrayList<Location>();	
	
	public BombData(ConfigurationSection bombSection){
		
		for(int i = 0; bombSection.isSet(""+i); i++){
			
			int x = bombSection.getInt(i+".x");
			int y = bombSection.getInt(i+".y");
			int z = bombSection.getInt(i+".z");
			
			String world = bombSection.getString(i+".w");
			
			bomb.add(new Location(Bukkit.getWorld(world), x, y, z));
		}
	}
	
	public ArrayList<Location> getBombLocation(){
		
		return bomb;
	}
}

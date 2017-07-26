package com.github.Nols1000.SAD.config;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SpawnData {
	
	private ArrayList<Location> atSpawn = new ArrayList<Location>();
	private ArrayList<Location> deSpawn = new ArrayList<Location>();
	
	public SpawnData(ConfigurationSection atSection, ConfigurationSection deSection){
		
		atSpawn = getSpawns(atSection);
		deSpawn = getSpawns(deSection);
	}
	
	private ArrayList<Location> getSpawns(ConfigurationSection section) {
		
		ArrayList<Location> res = new ArrayList<Location>(); 
		
		if(section != null){
			
			for(int i = 0; section.isSet(""+i); i++){
				
				int x = section.getInt(i+".x");
				int y = section.getInt(i+".y");
				int z = section.getInt(i+".z");
				
				String world = section.getString(i+".w");
				
				res.add(new Location(Bukkit.getWorld(world), x, y, z));
			}
		}
		
		return res;
	}

	public ArrayList<Location> getAtSpawns(){
		
		return atSpawn;
	}
	
	public ArrayList<Location> getDeSpawns(){
		
		return deSpawn;
	}
}

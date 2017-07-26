package com.github.Nols1000.SAD.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	
	private static Plugin plugin;
	private static FileConfiguration config;
	
	private static boolean isInit = false;
	
	public static void init(){
		
	   	plugin = Bukkit.getPluginManager().getPlugin("Search and Destroy");
	   	config = plugin.getConfig();
	   	
	   	isInit = true;
	}
	
	public static GameData getGameData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new GameData(config.getInt("game.time.pregame"),
								config.getInt("game.time.round"),
								config.getInt("game.time.plant"),
								config.getInt("game.time.defuse"),
								config.getInt("game.time.detonation"),
								config.getInt("game.player.speed.default"),
								config.getInt("game.player.speed.hited"),
								config.getInt("game.player.health.default"),
								config.getInt("game.player.health.attacker"),
								config.getInt("game.player.health.defender"),
								config.getBoolean("game.player.health.regenerate"),
								config.getInt("game.rounds"),
								config.getString("game.world.name"),
								config.getString("game.lobby.name"));
		}
		
		return null;
	}
	
	public static TeamData getDefenderData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new TeamData(config.getString("game.team.de.name"), 
								config.getString("game.team.de.prefix"), 
								config.getInt("game.team.de.maxplayer"));
		}
		
		return null;
	}
	
	public static TeamData getAttackerData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new TeamData(config.getString("team.at.name"), 
								config.getString("team.at.prefix"), 
								config.getInt("team.at.maxplayer"));
		}
		
		return null;
	}
	
	public static SpawnData getSpawnData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new SpawnData(config.getConfigurationSection("spawn.0"),
								 config.getConfigurationSection("spawn.1"));
		}
		
		return null;
	}
	
	public static StationData getStationData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new StationData(config.getConfigurationSection("station"),
								   config.getInt("game.bomb.power"),
								   config.getBoolean("game.bomb.fire"),
								   config.getBoolean("game.bomb.destroy"));
		}
		
		return null;
	}
	
	public static BombData getBombData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new BombData(config.getConfigurationSection("bomb"));
		}
		
		return null;
	}
	
	public static SignDataPack getSignData(){
		
		List<SignData> arg0 = new ArrayList<SignData>();
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			for(int i = 0; config.isSet("sign."+i); i++){
			
				arg0.add(new SignData(config.getInt("sign."+i+".x"),config.getInt("sign."+i+".y"),config.getInt("sign."+i+".z"), Bukkit.getWorld(config.getString("sign."+i+".w"))));
			}
			
			return new SignDataPack(arg0);
		}
		
		return null;
	}
	
	public static LoggerData getLoggerData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new LoggerData(config.getBoolean("debug.error"),
								  config.getBoolean("debug.warning"),
								  config.getBoolean("debug.debug"),
								  config.getBoolean("debug.info"));
		}
		
		return null;
	}
	
	public static MinData getMinData(){
		
		if(!isInit)
			init();
		
		if(plugin != null)
			plugin.reloadConfig();
		
		if(config != null){
			
			return new MinData(config.getInt("game.min.player"));
		}
		
		return null;
	}
}

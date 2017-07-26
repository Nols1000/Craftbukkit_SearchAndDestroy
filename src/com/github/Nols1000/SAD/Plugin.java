package com.github.Nols1000.SAD;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Nols1000.SAD.config.Config;

public class Plugin extends JavaPlugin {
	
	private Game game2;
	
	private ArrayList<Block>       block = new ArrayList<Block>();
	//private ArrayList<BombStation> station = new ArrayList<BombStation>();
	@SuppressWarnings("unchecked")
	private ArrayList<Location>[]  spawns = new ArrayList[2];
	//private ArrayList<Location> bombs;
	
	private FileConfiguration config;

	public String configWorld;

	private List<Location> bombs = new ArrayList<Location>();

	private List<BombStation> station = new ArrayList<BombStation>();
	
	
	public void onEnable(){
		
		new Metrics(this);
		
		initConfig();
		initVars();
		initBombs();
		initStation();
		
		if(config.getBoolean("enabled"))
			initGame();
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("sad")){
			
			if(args.length == 0){
				
				return false;
			}else if(args.length == 1){
				
				return false;
			}else if(args.length == 2){
				
				if(args[0].equalsIgnoreCase("add")){
					
					if(args[1].equalsIgnoreCase("station")){
						
						if(sender.hasPermission("sad.add.station") && (sender instanceof org.bukkit.entity.Player)){
							
							sender.sendMessage(ChatColor.YELLOW+"Please Select a Furnace-Block.");
							
							if(game2 != null)
								game2.unregisterAll();
							
							getServer().getPluginManager().registerEvents(new Listener(){
								
								@EventHandler
								public void onClick(PlayerInteractEvent e){
									
									Block b = e.getClickedBlock();
									
									System.out.println(e.toString());
									
									if(b.getType() == Material.FURNACE){
										
										if(addFurnace(b)){
											sender.sendMessage(ChatColor.YELLOW+"Station selected");
											HandlerList.unregisterAll(this);
										}else
											sender.sendMessage(ChatColor.YELLOW+"Please Select a Furnace-Block.");
									}else{
										
										sender.sendMessage(ChatColor.RED+"Please Select a Furnace-Block. (Selected: "+b.getType().name()+")");
									}
								}
							}, this);
							
							return true;
						}
					}else if(args[1].equalsIgnoreCase("bomb")){
						
						if(sender.hasPermission("sad.add.bomb") && (sender instanceof org.bukkit.entity.Player)){
							
							org.bukkit.entity.Player pl = (org.bukkit.entity.Player) sender;
							
							addBomb(pl.getLocation());
							
							sender.sendMessage(ChatColor.YELLOW+"Bomb spawn added.");
							
							return true;
						}
					}else if(args[1].equalsIgnoreCase("sign")){
						
						if(sender.hasPermission("sad.add.sign")){
						
							sender.sendMessage(ChatColor.YELLOW+"Select a sign.");
							
							if(game2 != null)
								game2.unregisterAll();
						
							getServer().getPluginManager().registerEvents(new Listener(){
							
								@EventHandler
								public void onClick(PlayerInteractEvent e){
								
									Block b = e.getClickedBlock();
								
									System.out.println(e.toString());
								
									if(b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST){
									
										addSign(b.getLocation());
									
										sender.sendMessage(ChatColor.YELLOW+"Sign added.");
										HandlerList.unregisterAll(this);
									}else{
									
										sender.sendMessage(ChatColor.RED+"Select a sign. (Selected: "+b.getType().name()+")");
									}
								}
							}, this);
							
							return true;
						}
					}
				}
			}else if(args.length == 3){
				
				if(args[0].equalsIgnoreCase("add")){
					
					if(args[1].equalsIgnoreCase("spawn")){
						
						if(sender.hasPermission("sad.add.spawn") && (sender instanceof org.bukkit.entity.Player)){
							
							org.bukkit.entity.Player pl = (org.bukkit.entity.Player) sender;
							
							addSpawn(pl.getLocation(), Integer.valueOf(args[2]));
							
							sender.sendMessage(ChatColor.YELLOW+"Spawn added. Team:" + Integer.valueOf(args[2]));
							
							return true;
						}else if(!(sender instanceof org.bukkit.entity.Player)){
							
							sender.sendMessage("You aren\'t a Player");
							
							return true;
						}else{
							
							sender.sendMessage("You havn\'t the permissions to do that.");
							
							return true;
						}
					}
				}
				
			}else{
				
				return false;
			}
		}
		
		return false;
	}

	private void initConfig(){
		
		config = getConfig();
	}
	
	private void initVars(){
		
		reloadConfig();
		initConfig();
		
		configWorld = config.getString("game.world.name");
	}
	
	private void initSpawn(){
		
		reloadConfig();
		initConfig();
		
		boolean isRunning = true;
		
		int i = 0;
		int j = 0;
		
		spawns[0] = Config.getSpawnData().getAtSpawns();
		spawns[1] = Config.getSpawnData().getDeSpawns();
		
		while(isRunning){
			
			if(config.isSet("spawn."+i+"."+j)){
				
				int x 	 = config.getInt("spawn."+i+"."+j+".x");
				int y 	 = config.getInt("spawn."+i+"."+j+".y");
				int z 	 = config.getInt("spawn."+i+"."+j+".z");
				String w = config.getString("spawn."+i+"."+j+".w");
				
				spawns[i].add(new Location(getServer().getWorld(w), x, y, z));
				System.out.println(i+"|"+j);				
				
				j++;
			}else{
				
				i++;
				j = 0;
				
				if(i > 1){
					
					isRunning = false;
				}
			}
		}
	}
	
	private void initBombs() {
		
		reloadConfig();
		initConfig();
		
		boolean isRunning = true;
		
		int i = 0;
		
		bombs = new ArrayList<Location>();
		
		while(isRunning){
			
			if(config.isSet("bomb."+i)){
				
				int x 	 = config.getInt("bomb."+i+".x");
				int y 	 = config.getInt("bomb."+i+".y");
				int z 	 = config.getInt("bomb."+i+".z");
				String w = config.getString("bomb."+i+".w");
				
				bombs.add(new Location(getServer().getWorld(w), x, y, z));			
				
				i++;
			}else{
				
				isRunning = false;
			}
		}
	}
	
	private void initStation(){
		
		reloadConfig();
		initConfig();
		
		if(config.isSet("station.0.one") && config.isSet("station.0.two")){
			
			Block[] b = new Block[2];
			
			if(config.getString("station.0.one.w") != null){
				
				String w1 = config.getString("station.0.one.w");
				int    x1 = config.getInt("station.0.one.x");
				int    y1 = config.getInt("station.0.one.y");
				int    z1 = config.getInt("station.0.one.z");
				
				String w2 = config.getString("station.0.two.w");
				int    x2 = config.getInt("station.0.two.x");
				int    y2 = config.getInt("station.0.two.y");
				int    z2 = config.getInt("station.0.tow.z");
				
				b[0] = getServer().getWorld(w1).getBlockAt(new Location(getServer().getWorld(w1), x1, y1, z1));
				b[1] = getServer().getWorld(w2).getBlockAt(new Location(getServer().getWorld(w2), x2, y2, z2));
			
				b[0].setType(Material.FURNACE);
				b[1].setType(Material.FURNACE);
			}
			
			station.add(new BombStation(b));
		}
		
		if(config.isSet("station.1.one") && config.isSet("station.1.two")){
			
			Block[] b = new Block[2];
			
			if(config.getString("station.1.one.w") != null){
			
				String w1 = config.getString("station.1.one.w");
				int    x1 = config.getInt("station.1.one.x");
				int    y1 = config.getInt("station.1.one.y");
				int    z1 = config.getInt("station.1.one.z");
				
				String w2 = config.getString("station.1.two.w");
				int    x2 = config.getInt("station.1.two.x");
				int    y2 = config.getInt("station.1.two.y");
				int    z2 = config.getInt("station.1.two.z");
				
				b[0] = getServer().getWorld(w1).getBlockAt(new Location(getServer().getWorld(w1), x1, y1, z1));
				b[1] = getServer().getWorld(w2).getBlockAt(new Location(getServer().getWorld(w2), x2, y2, z2));
				
				b[0].setType(Material.FURNACE);
				b[1].setType(Material.FURNACE);
			}
				
			station.add(new BombStation(b));
		}
	}
	
	private void initGame() {
		
		game2 = new Game(this);
	}
	
	private void addSpawn(Location location, int team) {
		
		initConfig();
		
		if(config == null)
			System.out.println("Config: null");
		
		config.set("spawn."+team+"."+spawns[team].size()+".x", location.getBlockX());
		config.set("spawn."+team+"."+spawns[team].size()+".y", location.getBlockY());
		config.set("spawn."+team+"."+spawns[team].size()+".z", location.getBlockZ());
		config.set("spawn."+team+"."+spawns[team].size()+".w", location.getWorld().getName());
		
		saveConfig();
		
		initSpawn();
	}
	
	private void addSign(Location location){
		
		initConfig();
		
		if(config == null)
			System.out.println("Config: null");
		
		int size = Config.getSignData().getSignsList().size();
		
		config.set("sign."+size+".x", location.getBlockX());
		config.set("sign."+size+".y", location.getBlockY());
		config.set("sign."+size+".z", location.getBlockZ());
		config.set("sign."+size+".w", location.getWorld());
		
		saveConfig();
		reloadConfig();
		initConfig();
	}
	
	private void addBomb(Location location) {
		
		initConfig();
		
		if(config == null)
			System.out.println("Config: null");
		
		config.set("bomb."+bombs.size()+".x", location.getBlockX());
		config.set("bomb."+bombs.size()+".y", location.getBlockY());
		config.set("bomb."+bombs.size()+".z", location.getBlockZ());
		config.set("bomb."+bombs.size()+".w", location.getWorld().getName());
		
		saveConfig();
		
		initBombs();
	}

	private boolean addFurnace(Block b){
		
		if(block.size() < 1){
			
			block.add(b);
		}else{
			
			block.add(b);
			
			addStation(block);
			
			HandlerList.unregisterAll(this);
			
			if(game2 != null)
				game2.registerAll();
			
			block.remove(1);
			block.remove(0);
			
			return true;
		}
		
		return false;
	}

	private void addStation(ArrayList<Block> b) {
		
		reloadConfig();
		initConfig();
		
		System.out.println("addStation");
		
		config.set("station."+station.size()+".one.x", b.get(0).getLocation().getBlockX());
		config.set("station."+station.size()+".one.y", b.get(0).getLocation().getBlockY());
		config.set("station."+station.size()+".one.z", b.get(0).getLocation().getBlockZ());
		config.set("station."+station.size()+".one.w", b.get(0).getLocation().getWorld().getName());
		config.set("station."+station.size()+".two.x", b.get(1).getLocation().getBlockX());
		config.set("station."+station.size()+".two.y", b.get(1).getLocation().getBlockY());
		config.set("station."+station.size()+".two.z", b.get(1).getLocation().getBlockZ());
		config.set("station."+station.size()+".two.w", b.get(1).getLocation().getWorld().getName());
		
		saveConfig();
		
		initStation();
	}

	public void onDisable(){
		
		if(game2 != null)
			game2.unregisterAll();
	}
	
	public Game getGame2() {
		
		return game2;
	}
}

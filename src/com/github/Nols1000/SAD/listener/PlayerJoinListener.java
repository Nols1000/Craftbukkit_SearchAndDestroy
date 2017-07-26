package com.github.Nols1000.SAD.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.Nols1000.SAD.Counter;
import com.github.Nols1000.SAD.Player;
import com.github.Nols1000.SAD.Plugin;
import com.github.Nols1000.SAD.events.OnCounterCompleteEvent;
import com.github.Nols1000.SAD.events.OnCounterTickEvent;

public class PlayerJoinListener implements Listener {
	
	private Plugin plugin;
	
	private Counter leaveCounter;
	
	public PlayerJoinListener(Plugin p){
		
		plugin = p;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		Player p = new Player(e.getPlayer());
		
		if(p.bukkitPlayer.getWorld().getName().equalsIgnoreCase(plugin.getGame2().getWorldName())){
		
			if(plugin.getGame2().isRunning()){
			
				p.bukkitPlayer.kickPlayer("[Search and Destroy] GAME IS RUNNING");
			}else if(plugin.getGame2().isFull()){
				
				if(p.bukkitPlayer.hasPermission("sad.join.full")){
				
					plugin.getGame2().kickLastPlayer();
					plugin.getGame2().join(p);
				}else{
				
					p.bukkitPlayer.kickPlayer("[Search and Destroy] SERVER IS FULL");
				}
			}else{
			
				plugin.getGame2().join(p);
			}
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e){
		
		final Player p = new Player(e.getPlayer());
		
		if(p.bukkitPlayer.getWorld().getName().equalsIgnoreCase(plugin.getGame2().getWorldName())){
			
			if(plugin.getGame2().hasPlayer(p)){
				
				if(leaveCounter != null)
					leaveCounter.interrupt();
				
				p.bukkitPlayer.sendMessage("Thanks for coming back");
			}else if(plugin.getGame2().isFull()){
				
				if(p.bukkitPlayer.hasPermission("sad.join.full") && !plugin.getGame2().isRunning()){
					
					plugin.getGame2().kickLastPlayer();
					plugin.getGame2().join(p);
				}
			}else{
				
				if(!plugin.getGame2().isRunning()){
					
					plugin.getGame2().join(p);
				}else{
					
					p.bukkitPlayer.kickPlayer("[Search and Destroy] GAME IS RUNNING");
				}
			}
		}else{
			
			if(plugin.getGame2().hasPlayer(p)){
				
				p.bukkitPlayer.sendMessage("You have left the game please come back.");
				
				leaveCounter = new Counter(20, 1000, new OnCounterCompleteEvent() {
					
					@Override
					public void onCounterComplete(Counter counter) {
						
						plugin.getGame2().leave(p);
					}
				}, new OnCounterTickEvent() {
					
					@Override
					public void tick(int s) {
						
						p.bukkitPlayer.sendMessage("You have "+s+" seconds to come back.");
					}
				});
				
				leaveCounter.start();				
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		
		Player p = new Player(e.getPlayer());
		
		plugin.getGame2().leave(p);
	}
	
}

package com.github.Nols1000.SAD.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.Nols1000.SAD.Game;

public class PlayerKillListener implements Listener {
	
	private Game game;
	
	public PlayerKillListener(Game g){
		
		game = g;
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent e){
		
		if(e.getEntityType() == EntityType.PLAYER){
			
			if(game != null)
				game.onPlayerKilled((Player) e.getEntity());
		}
	}
}

package com.github.Nols1000.SAD.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

	public boolean canMove = true;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		
		if(!canMove && !e.getPlayer().hasPermission("sad.canMove")){
			
			double x = e.getFrom().getX(),
				   y = e.getFrom().getY(),
				   z = e.getFrom().getZ();
			
			if(x != e.getPlayer().getLocation().getX() && y != e.getPlayer().getLocation().getY() && z != e.getPlayer().getLocation().getZ())
				e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), x, y, z));
		}
	}
}

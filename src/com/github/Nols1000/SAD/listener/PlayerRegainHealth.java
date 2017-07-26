package com.github.Nols1000.SAD.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class PlayerRegainHealth {
	
	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent event){
		
		if(event.getEntity().getType() == EntityType.PLAYER){
			
			if(!(event.getRegainReason() == RegainReason.EATING || event.getRegainReason() == RegainReason.MAGIC || event.getRegainReason() == RegainReason.MAGIC_REGEN)){
				
				event.setCancelled(true);
			}
		}
	}
	
}

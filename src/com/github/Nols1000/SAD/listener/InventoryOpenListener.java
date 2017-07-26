package com.github.Nols1000.SAD.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.Nols1000.SAD.BombStation;
import com.github.Nols1000.SAD.Player;
import com.github.Nols1000.SAD.Plugin;
import com.github.Nols1000.SAD.config.SADTeamType;

public class InventoryOpenListener implements Listener {

	private Block b;
	
	private Plugin plugin;
	
	public InventoryOpenListener(Plugin p){
		
		plugin = p;
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e){
		
		if(e.getInventory().getType() == InventoryType.FURNACE){
			
			Player player  = plugin.getGame2().getPlayer(e.getPlayer().getName());
		
			Inventory plIn = player.bukkitPlayer.getInventory();
			FurnaceInventory fcIn = (FurnaceInventory) e.getInventory();
		
			if(b.getType() == Material.FURNACE){
			
				BombStation bs = plugin.getGame2().getStation(b);
			
				if(bs != null && plIn.contains(Material.TNT) && player.getTeam().getType() == SADTeamType.ATTACKER){
				
					plIn.remove(Material.TNT);
					fcIn.setFuel(new ItemStack(Material.TNT, 1));
				
					bs.plantBomb(player, plIn, fcIn);
				}else if(bs != null && player.getTeam().getType() == SADTeamType.DEFENDER && bs.canDefuse(fcIn)){
					
					System.out.println("Defusing");
					
					bs.defuseBomb(player, plIn, fcIn);
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		
		if(e.getInventory().getType() == InventoryType.FURNACE){
			
			Player player  = plugin.getGame2().getPlayer(e.getPlayer().getName());
		
			Inventory plIn = player.bukkitPlayer.getInventory();
			FurnaceInventory fcIn = (FurnaceInventory) e.getInventory();
		
			if(b.getType() == Material.FURNACE){
			
				BombStation bs = plugin.getGame2().getStation(b);
			
				if(bs != null && player.getTeam().getType() == SADTeamType.ATTACKER){
				
					if(bs.interruptPlantBomb()){
						
						fcIn.remove(Material.TNT);
						plIn.addItem(new ItemStack(Material.TNT, 1));
					}
				}else if(bs != null && player.getTeam().getType() == SADTeamType.DEFENDER){
				
					if(bs.interruptDefuseBomb()){
						
						// Some code
					}
				}else{
				
					System.out.println("BS NULL");
				}
			}
		}
	}
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e){
		
		Player player  = plugin.getGame2().getPlayer(e.getPlayer().getName());
		
		if(e.getItem().getItemStack().getType() == Material.TNT && player.getTeam().getType() == SADTeamType.DEFENDER){
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		b = e.getClickedBlock();
	}
}

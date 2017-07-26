package com.github.Nols1000.SAD;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.Nols1000.SAD.config.Config;
import com.github.Nols1000.SAD.config.GameData;
import com.github.Nols1000.SAD.config.SADTeamType;
import com.github.Nols1000.SAD.events.OnBombDefusedEvent;
import com.github.Nols1000.SAD.events.OnBombExplodeEvent;
import com.github.Nols1000.SAD.events.OnBombPlantedEvent;
import com.github.Nols1000.SAD.events.OnBombPlantingEvent;
import com.github.Nols1000.SAD.events.OnCounterCompleteEvent;
import com.github.Nols1000.SAD.events.OnCounterTickEvent;

public class BombStation {
	
	private Block[] station = new Block[2];
	private java.util.logging.Logger logger = Bukkit.getLogger();
	private boolean isStationSet = false;
	
	private Player plantingPlayer;
	private Player defusingPlayer;
	private Counter plantingCounter;
	private Counter defusingCounter;
	
	private OnBombPlantingEvent e1;
	private OnBombPlantedEvent  e2;
	private OnBombExplodeEvent  e3;
	private OnBombDefusedEvent  e4;
	
	private GameData data;
	
	public BombStation(Block[] station){
		
		setBlocks(station);
		
		data = Config.getGameData();
	}
	
	public void registerOnBombPlantingEvent(OnBombPlantingEvent e){
		
		e1 = e;
	}
	
	public void registerOnBombPlantedEvent(OnBombPlantedEvent e){
		
		e2 = e;
	}
	
	public void registerOnBombExplodeEvent(OnBombExplodeEvent e){
		
		e3 = e;
	}
	
	public void registerOnBombDefusedEvent(OnBombDefusedEvent e){
		
		e4 = e;
	}
	
	public boolean setBlocks(Block[] station){
		
		if(station.length == 2){
			
			if(station[0].getType() == Material.FURNACE){
				
				if(station[1].getType() == Material.FURNACE){
					
					this.station = station;
					
					isStationSet = true;
					return true;
				}else{
					
					logger.log(Level.WARNING, "BOMBSTATION init faild. (ERROR: BLOCK 2 IS NOT TYPE FURNACE)");
					
					isStationSet = false;
					return false;
				}
			}else{
				
				logger.log(Level.WARNING, "BOMBSTATION init faild. (ERROR: BLOCK 1 IS NOT TYPE FURNACE)");
				
				isStationSet = false;
				return false;
			}
			
		}else{
			
			logger.log(Level.WARNING, "BOMBSTATION init faild. (ERROR: MORE/LESS THAN 2 OBJECTS)");
			
			isStationSet = false;
			return false;
		}
	}
	
	public void plantBomb(Player p, final Inventory plIn, final FurnaceInventory fcIn){
		
		if(isStationSet){
			
			if(e1 != null)
				e1.onBombPlanting(p, this);
			
			plantingPlayer = p;
			
			plantingCounter = new Counter(data.getPlantTime(), 1000, new OnCounterCompleteEvent() {
				
				@Override
				public void onCounterComplete(Counter counter) {
					
					bombPlanted(plIn, fcIn);
				}
			}, new OnCounterTickEvent() {
				
				@Override
				public void tick(int s) {
					
					if(plantingPlayer != null)
						plantingPlayer.setPlantTimerScore(s);
				}
			});
			
			plantingPlayer.attachPlantScoreboard();
			plantingCounter.start();
			
		}
	}
	
	public boolean interruptPlantBomb() {
		
		if(plantingPlayer != null){
			
			plantingCounter.interrupt();
			
			plantingPlayer.reloadTeamScoreboard();
			plantingPlayer = null;
			
			return true;
		}
		
		return false;
	}
	
	private void bombPlanted(Inventory plIn, final FurnaceInventory fcIn){
		
		if(e2 != null)
			e2.onBombPlanted(plantingPlayer, this);
		
		plIn.remove(Material.TNT);
		
		fcIn.remove(Material.TNT);
		fcIn.setSmelting(new ItemStack(Material.TNT, 1));
		
		plantingPlayer.reloadTeamScoreboard();
		plantingPlayer = null;
		
		new Counter(45, 1000, new OnCounterCompleteEvent(){

			@Override
			public void onCounterComplete(Counter counter) {
				
				bombExplode(fcIn);
			}			
		}, new OnCounterTickEvent() {
			
			@Override
			public void tick(int s) {
				
				MScoreBoard.updateDetonationTimer(s);
			}
		}).start();
	}
	
	private void bombExplode(FurnaceInventory fcIn){
		
		if(e3 != null)
			e3.onBombExplode(this, true);
		
		fcIn.remove(Material.TNT);
		
		station[0].getWorld().createExplosion(station[0].getLocation().getX(), station[0].getLocation().getY(), station[0].getLocation().getZ(), 10, false, false);
		station[1].getWorld().createExplosion(station[0].getLocation().getX(), station[0].getLocation().getY(), station[0].getLocation().getZ(), 10, false, false);
	}

	public void defuseBomb(Player player, Inventory plIn, final FurnaceInventory fcIn) {
		
		defusingPlayer = player;
		
		defusingPlayer.attachDefuseScoreboard();
		
		if(player.team.getType() == SADTeamType.DEFENDER){
			
			defusingCounter = new Counter(data.getDefuseTime(), 1000, new OnCounterCompleteEvent() {
				
				@Override
				public void onCounterComplete(Counter counter) {
					
					bombDefused(fcIn);
				}
			}, new OnCounterTickEvent() {
				
				@Override
				public void tick(int s) {
					
					defusingPlayer.setDefuseTimerScore(s);
				}
			});
			
			defusingCounter.start();
		}
	}
	
	public boolean canDefuse(FurnaceInventory fcIn) {
		
		return fcIn.contains(Material.TNT);
	}

	private void bombDefused(FurnaceInventory fcIn) {
		
		e4.onBombDefused(defusingPlayer, this);
		
		defusingPlayer.reloadTeamScoreboard();
		
		plantingCounter.interrupt();
		
		fcIn.remove(Material.TNT);
	}

	public boolean interruptDefuseBomb(){
		
		if(defusingPlayer != null){
			
			defusingCounter.interrupt();
			
			defusingPlayer.reloadTeamScoreboard();
			defusingPlayer = null;
			
			return true;
		}
		
		return false;
	}
	
	public Block[] getBlocks() {
		
		return station;
	}
}

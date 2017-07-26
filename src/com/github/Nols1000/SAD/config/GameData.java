package com.github.Nols1000.SAD.config;

public class GameData {
	
	private int preGameTime;
	private int roundeTime;
	private int plantTime;
	private int defuseTime;
	private int detonationTime;

	private int defaultSpeed;
	private int hitedSpeed;
	
	private int defaultHealth;
	private int attackerHealth;
	private int defenderHealth;
	private boolean regenerate;
	
	private int rounds;
	
	private String worldName;
	private String lobbyName;
	
	public GameData(int pg,
					int rt,
					int pt,
					int dt,
					int detT,
					int ds,
					int hs,
					int dh,
					int ah,
					int defH,
					boolean re,
					int ro,
					String wn,
					String ln){
		
		preGameTime    = pg;
		roundeTime     = rt;
		plantTime      = pt;
		defuseTime     = dt;
		detonationTime = detT;
		
		defaultSpeed   = ds;
		hitedSpeed     = hs;
		
		defaultHealth  = dh;
		attackerHealth = ah;
		defenderHealth = defH;
		regenerate     = re;
		
		rounds         = ro;
		
		worldName      = wn;
		lobbyName      = ln;
 	}
	
	public int getPreGameTime() {
		
		return preGameTime;
	}
	
	public int getRoundTime(){
		
		return roundeTime;
	}
	
	public int getPlantTime(){
		
		return plantTime;
	}
	
	public int getDefuseTime(){
		
		return defuseTime;
	}
	
	public int getDetonationTime(){
		
		return detonationTime;
	}
	
	public int getDefaultSpeed(){
		
		return defaultSpeed;
	}
	
	public int getHitedSpeed(){
		
		return hitedSpeed;
	}
	
	public int getDefaultHealth(){
		
		return defaultHealth;
	}
	
	public int getAttackerHealth(){
		
		return attackerHealth;
	}
	
	public int getDefenderHealth(){
		
		return defenderHealth;
	}
	
	public boolean isRegenerating(){
		
		return regenerate;
	}
	
	public int getRounds() {
		
		return rounds;
	}

	public String getWorldName(){
		
		return worldName;
	}

	public String getLobbyName() {
		
		return lobbyName;
	}
}

package com.github.Nols1000.SAD;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Player {

	public org.bukkit.entity.Player bukkitPlayer;
	
	public Team2 team;
	
	protected int points = 0;
	protected int kills  = 0;
	protected int deaths = 0;

	private Scoreboard sbpt;
	private Objective pt;
	private Score ptt;
	
	private Scoreboard sbdt;
	private Objective dt;
	private Score dtt;

	
	public Player(){
		
		bukkitPlayer = Bukkit.getPlayer("Steve");
		
		init();
	}
	
	public Player(String name){
		
		bukkitPlayer = Bukkit.getPlayer(name);
		
		init();
	}
	
	public Player(org.bukkit.entity.Player p){
		
		bukkitPlayer = p;
		
		init();
	}
	
	protected void init(){
		
		sbpt = Bukkit.getScoreboardManager().getNewScoreboard();
		
		pt = sbpt.registerNewObjective("plantTimer", "dummy");
		
		pt.setDisplayName("Bomb planting ...");
		pt.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		ptt = pt.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GRAY+"Planting: "));
		
		sbdt = Bukkit.getScoreboardManager().getNewScoreboard();
		
		dt = sbdt.registerNewObjective("defuseTimer", "dummy");
		
		dt.setDisplayName("Bomb defusing ...");
		dt.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		dtt = dt.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GRAY+"Defusing: "));
	}
	
	public void setTeam(Team2 team){
		
		System.out.println("team set to player"+team.getTeam());
		
		this.team = team;
	}

	public void setScoreboard(Scoreboard sb) {
		
		if(bukkitPlayer != null)
			bukkitPlayer.setScoreboard(sb);
	}

	public void attachPlantScoreboard(){
		
		bukkitPlayer.setScoreboard(sbpt);
	}
	
	public void attachDefuseScoreboard() {
		
		bukkitPlayer.setScoreboard(sbdt);
	}
	
	public void setPlantTimerScore(int s){
		
		ptt.setScore(s);
	}
	
	public Score getPlantTimerScore() {
		
		return ptt;
	}
	
	public void reloadTeamScoreboard() {
		
		if(team == null)
			System.out.println("team null");
		
		team.reloadScoreboard();
	}

	public void setDefuseTimerScore(int s) {
		
		dtt.setScore(s);
	}
	
	public Score getDefuseTimerScore() {
		
		return dtt;
	}

	public Team2 getTeam() {
		
		return team;
	}
}

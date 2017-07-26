package com.github.Nols1000.SAD;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.github.Nols1000.SAD.config.SADTeamType;
import com.github.Nols1000.SAD.dev.Logger;

public class Team2 {
	
	private Team team;
	private Scoreboard sb;
	
	protected int maxPlayer = 5;
	protected int minPlayer = 1;
	protected int points    = 0;
	protected int wins      = 0;
	protected int loses     = 0;
	public int living       = 1;
	
	public SADTeamType type = SADTeamType.DEFENDER;
	
	private List<String> player       = new ArrayList<String>();
	private List<String> livingPlayer = new ArrayList<String>();
	
	public Team2(Team t, Scoreboard sb){
		
		team       = t;
		this.sb    = sb;
	}
	
	public void addPlayer(Player p){
		
		p.setTeam(this);
		
		org.bukkit.entity.Player pl = p.bukkitPlayer;
		
		player.add(pl.getName());
		livingPlayer.add(pl.getName());
		living++;
		
		if(sb != null)	
			pl.setScoreboard(sb);
		else
			Logger.log(Level.WARNING, "SB = NULL (43)");
		
		team.addPlayer(pl);
	}
	
	public void removePlayer(Player p){
		
		org.bukkit.entity.Player pl = p.bukkitPlayer;
		
		player.remove(pl.getName());
		livingPlayer.remove(pl.getName());
		living--;
		
		if(sb != null)	
			pl.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		else
			Logger.log(Level.WARNING, "SB = NULL (56)");
		
		team.removePlayer(pl);
	}
	
	public void reloadScoreboard(){
		
		for(int i = 0; i < player.size(); i++){
			
			Bukkit.getPlayer(player.get(i)).setScoreboard(sb);
		}
	}
	
	public int getSize(){
		
		return player.size();
	}
	
	public boolean isFull() {
		
		return (player.size() > maxPlayer);
	}
	
	public Team getTeam(){
		
		return team;
	}

	public boolean isPlayersTeam(Player p) {
		
		return team.hasPlayer(p.bukkitPlayer);
	}
	
	public void setSB(Scoreboard sb){
		
		this.sb = sb;
		reloadScoreboard();
	}
	
	public Scoreboard getSB() {
		
		return sb;
	}
	
	public void setType(SADTeamType t){
		
		type = t;
	}

	public SADTeamType getType() {
		
		return type;
	}

	public void onPlayerKilled(String name) {
		
		livingPlayer.remove(name);
		
		living = livingPlayer.size();
	}
}

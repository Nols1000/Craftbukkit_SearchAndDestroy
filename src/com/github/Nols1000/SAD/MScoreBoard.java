package com.github.Nols1000.SAD;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.github.Nols1000.SAD.Team;
import com.github.Nols1000.SAD.config.SADTeamType;

public class MScoreBoard {
	
	private Scoreboard sbat;
	private Scoreboard sbde;
	private Objective  at;
	private Objective  de;
	private Score[][]  teamScore = new Score[2][2];
	private static Score[]    deTimer   = new Score[2];
	
	private Score      at_timer;
	private Score      de_timer;
	
	private Team2[]    team = new Team2[2];
	
	private String title = "Search and Destroy";
	
	private String str_attacker = "Attacker";
	private String str_defender = "Defender";
	private Score[] gameStart = new Score[2];
	
	public MScoreBoard() {
		
		init();
	}

	public void init(){
		
		initDE();
		initAT();
		
		team[0] = new Team2(new Team(sbde.registerNewTeam(str_attacker), sbat.registerNewTeam(str_attacker)), sbat);
		team[1] = new Team2(new Team(sbde.registerNewTeam(str_defender), sbat.registerNewTeam(str_defender)), sbde);
		
		team[0].getTeam().setPrefix("[AT]");
		team[1].getTeam().setPrefix("[DE]");
		
		team[0].setType(SADTeamType.ATTACKER);
		team[1].setType(SADTeamType.DEFENDER);
		
		setScore(0, 0);
	}
	
	public void initAT(){
		
		sbat = Bukkit.getScoreboardManager().getNewScoreboard();
		
		at = sbat.registerNewObjective("attacker", "dummy");
		
		at.setDisplayName("["+str_attacker+"] "+title);
		at.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		teamScore[0][0] = at.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA+"Attacker"));
		teamScore[0][1] = at.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD+"Defender"));
		deTimer[0]      = at.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Detonation in:"));
		
		at_timer = at.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Time:"));
		gameStart[0]  = at.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Game starts in"));
	}
	
	public void initDE(){
		
		sbde = Bukkit.getScoreboardManager().getNewScoreboard();
		
		de = sbde.registerNewObjective("defender", "dummy");
		
		de.setDisplayName("["+str_defender+"] "+title);
		de.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		teamScore[1][0] = de.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD+"Attacker"));
		teamScore[1][1] = de.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA+"Defender"));
		deTimer[1]      = de.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Detonation in:"));
		
		de_timer = de.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Time:"));
		gameStart[1]  = de.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Game starts in"));
	}
	
	public boolean onPlayerJoin(Player p) {
		
		int n = 0;
		
		if(team[0].getSize() > team[1].getSize())
			n = 1;
		
		if(!team[n].isFull()){
			
			team[n].addPlayer(p);
			
			return true;
		}
		
		return false;
	}
	
	public boolean onPlayerLeave(Player p) {
		
		if(team[0].isPlayersTeam(p))
			team[0].removePlayer(p);
		else if(team[1].isPlayersTeam(p))
			team[1].removePlayer(p);
		else
			System.out.println("Player isnt in a team (99)");
		
		return false;
	}
	
	public void setScore(int scoreAt, int scoreDe){
		
		// Attacker
		teamScore[0][0].setScore(scoreAt);
		teamScore[1][0].setScore(scoreAt);
		
		// Defender
		teamScore[0][1].setScore(scoreDe);
		teamScore[1][1].setScore(scoreDe);
	}
	
	public void switchSB(){
		
		Scoreboard sb1 = team[0].getSB();
		Scoreboard sb2 = team[1].getSB();		
		
		team[0].setSB(sb2);
		team[1].setSB(sb1);
	}
	
	public Scoreboard getAttackerScoreboard(){
		
		return sbat;
	}
	
	public Scoreboard getDefenderScoreboard(){
		
		return sbde;
	}
	
	public void updateTimer(int s) {
		
		at_timer.setScore(s);
		de_timer.setScore(s);
		
		if(s == 0){
			
			sbat.resetScores(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Time:"));
			sbde.resetScores(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Time:"));
			
			team[0].reloadScoreboard();
			team[1].reloadScoreboard();
		}
	}
	
	static public void updateDetonationTimer(int s) {
		
		deTimer[0].setScore(s);
		deTimer[1].setScore(s);
	}
	
	public void reloadSB(){
		
		team[0].reloadScoreboard();
		team[1].reloadScoreboard();
	}
	
	public Team2[] getTeams(){
		
		return team;
	}

	public void updatePreGameTimer(int s) {
		
		gameStart[0].setScore(s);
		gameStart[1].setScore(s);
		
		if(s == 0){
			
			sbat.resetScores(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Game starts in"));
			sbde.resetScores(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Game starts in"));
		
			team[0].reloadScoreboard();
			team[1].reloadScoreboard();
		}
	}
}

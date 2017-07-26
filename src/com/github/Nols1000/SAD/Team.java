package com.github.Nols1000.SAD;

import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;

public class Team implements org.bukkit.scoreboard.Team {

	private org.bukkit.scoreboard.Team[] team = new org.bukkit.scoreboard.Team[2];
	
	public Team(org.bukkit.scoreboard.Team t1, org.bukkit.scoreboard.Team t2){
		
		team[0] = t1;
		team[1] = t2;
		
		setAllowFriendlyFire(        t1.allowFriendlyFire()        );
		setCanSeeFriendlyInvisibles( t1.canSeeFriendlyInvisibles() );
		setDisplayName(              t1.getDisplayName()           );
		setPrefix(                   t1.getPrefix()                );
		setSuffix(                   t1.getSuffix()                );
	}
	
	@Override
	public void addPlayer(OfflinePlayer arg0) throws IllegalStateException,
			IllegalArgumentException {
		
		team[0].addPlayer(arg0);
		team[1].addPlayer(arg0);
	}

	@Override
	public boolean allowFriendlyFire() throws IllegalStateException {
		
		
		return team[0].allowFriendlyFire();
	}

	@Override
	public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
		
		return team[0].canSeeFriendlyInvisibles();
	}

	@Override
	public String getDisplayName() throws IllegalStateException {
		
		return team[0].getDisplayName();
	}

	@Override
	public String getName() throws IllegalStateException {
		
		return team[0].getName();
	}

	@Override
	public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
		
		return team[0].getPlayers();
	}

	@Override
	public String getPrefix() throws IllegalStateException {
		
		return team[0].getPrefix();
	}

	@Override
	public Scoreboard getScoreboard() {
		// TODO RETURNS AT SB
		return team[0].getScoreboard();
	}

	@Override
	public int getSize() throws IllegalStateException {
		
		return team[0].getSize();
	}

	@Override
	public String getSuffix() throws IllegalStateException {
		
		return team[0].getSuffix();
	}

	@Override
	public boolean hasPlayer(OfflinePlayer arg0)
			throws IllegalArgumentException, IllegalStateException {
	
		return team[0].hasPlayer(arg0);
	}

	@Override
	public boolean removePlayer(OfflinePlayer arg0)
			throws IllegalStateException, IllegalArgumentException {
		
		return (team[0].removePlayer(arg0) &&
				team[1].removePlayer(arg0));
	}

	@Override
	public void setAllowFriendlyFire(boolean arg0) throws IllegalStateException {
		
		team[0].setAllowFriendlyFire(arg0);
		team[1].setAllowFriendlyFire(arg0);
	}

	@Override
	public void setCanSeeFriendlyInvisibles(boolean arg0)
			throws IllegalStateException {
		
		team[0].setCanSeeFriendlyInvisibles(arg0);
		team[1].setCanSeeFriendlyInvisibles(arg0);
	}

	@Override
	public void setDisplayName(String arg0) throws IllegalStateException,
			IllegalArgumentException {
		
		team[0].setDisplayName(arg0);
		team[1].setDisplayName(arg0);
	}

	@Override
	public void setPrefix(String arg0) throws IllegalStateException,
			IllegalArgumentException {
		
		team[0].setPrefix(arg0);
		team[1].setPrefix(arg0);
	}

	@Override
	public void setSuffix(String arg0) throws IllegalStateException,
			IllegalArgumentException {
		
		team[0].setSuffix(arg0);
		team[1].setSuffix(arg0);
	}

	@Override
	public void unregister() throws IllegalStateException {
		
		team[0].unregister();
		team[1].unregister();
	}
	

}

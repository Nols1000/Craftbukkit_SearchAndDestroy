package com.github.Nols1000.SAD.events;

import com.github.Nols1000.SAD.Player;
import com.github.Nols1000.SAD.Team;

public interface OnTeamJoinEvent {
	
	public void onTeamJoin(Player p, Team t);
	
}

package com.github.Nols1000.SAD.events;

import com.github.Nols1000.SAD.Player;
import com.github.Nols1000.SAD.Team;

public interface OnTeamLeaveEvent {
	
	public void onTeamLeave(Player p, Team t);
}

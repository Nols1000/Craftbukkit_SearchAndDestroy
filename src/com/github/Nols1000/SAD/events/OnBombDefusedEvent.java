package com.github.Nols1000.SAD.events;

import com.github.Nols1000.SAD.BombStation;
import com.github.Nols1000.SAD.Player;

public interface OnBombDefusedEvent {
	
	public void onBombDefused(Player p, BombStation bs);	
}

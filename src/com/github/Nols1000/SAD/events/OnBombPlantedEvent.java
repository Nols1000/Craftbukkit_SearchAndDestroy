package com.github.Nols1000.SAD.events;

import com.github.Nols1000.SAD.BombStation;
import com.github.Nols1000.SAD.Player;

public interface OnBombPlantedEvent {
	
	public void onBombPlanted(Player p, BombStation bs);
	
}

package com.github.Nols1000.SAD.events;

import com.github.Nols1000.SAD.BombStation;

public interface OnBombExplodeEvent {
	
	public void onBombExplode(BombStation bs, boolean explode);
	public void onBombExplode(BombStation bs, boolean explode, int radius);
	public void onBombExplode(BombStation bs, boolean explode, int radius, boolean killAll);
	
}

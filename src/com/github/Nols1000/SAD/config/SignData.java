package com.github.Nols1000.SAD.config;

import org.bukkit.World;

public class SignData {
	
	private int x;
	private int y;
	private int z;
	
	private World world;
	
	public SignData(int x,
					 int y,
					 int z,
					 World w){
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		world  = w;
	}

	public int getX() {
		
		return x;
	}

	public int getY() {
		
		return y;
	}

	public int getZ() {
		
		return z;
	}

	public World getWorld() {
		return world;
	}
}

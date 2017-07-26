package com.github.Nols1000.SAD;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Sign {
	
	private org.bukkit.block.Sign sign;
	
	private Game game;
	
	public Sign(Location arg0, Game arg1){
		
		game = arg1;
		Block b = arg0.getBlock();
		b.getState().setType(Material.SIGN);
		sign = (org.bukkit.block.Sign) b.getState();
	}
	
	public void refresh(){
		
		sign.setLine(2, game.getOnline()+" / "+game.getMax());
		
		if(game.isRunning())
			sign.setLine(3, ChatColor.RED+"Running");
		else
			sign.setLine(3, ChatColor.GREEN+"Open");
		
		sign.update();
	}
	
}

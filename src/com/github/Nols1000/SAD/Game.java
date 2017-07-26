package com.github.Nols1000.SAD;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.github.Nols1000.SAD.config.BombData;
import com.github.Nols1000.SAD.config.Config;
import com.github.Nols1000.SAD.config.GameData;
import com.github.Nols1000.SAD.config.SADTeamType;
import com.github.Nols1000.SAD.config.SignData;
import com.github.Nols1000.SAD.config.SignDataPack;
import com.github.Nols1000.SAD.config.SpawnData;
import com.github.Nols1000.SAD.config.StationData;
import com.github.Nols1000.SAD.events.OnBombDefusedEvent;
import com.github.Nols1000.SAD.events.OnBombExplodeEvent;
import com.github.Nols1000.SAD.events.OnBombPlantedEvent;
import com.github.Nols1000.SAD.events.OnCounterCompleteEvent;
import com.github.Nols1000.SAD.events.OnCounterTickEvent;
import com.github.Nols1000.SAD.listener.InventoryOpenListener;
import com.github.Nols1000.SAD.listener.PlayerJoinListener;
import com.github.Nols1000.SAD.listener.PlayerKillListener;
import com.github.Nols1000.SAD.listener.PlayerMoveListener;

public class Game{
	
	private Plugin plugin;
	
 	private GameData     gameData;
 	private SpawnData    spawnData;
 	private StationData  stationData;
 	private BombData     bombData;
 	private SignDataPack signData;
 	
 	private ArrayList<BombStation> station = new ArrayList<BombStation>();
 	
 	private MScoreBoard scoreboard;
 	private List<Sign> signs = new ArrayList<Sign>();
 	
 	private Counter mCounter;
 	
 	private boolean isRunning = false;
 	private boolean isBombSet = false;
 	private boolean isBombEx  = false;
 	
 	private int spawnIndexAt = 0;
 	private int spawnIndexDe = 0;
 	
 	private Team2 team1;
 	private Team2 team2;
 	
 	private ArrayList<Player> playerList = new ArrayList<Player>();

	private PlayerJoinListener pjl;
	private PlayerMoveListener pml;
	private InventoryOpenListener iol;
	private PlayerKillListener pkl;

	private boolean isStarted = false;
 	
 	public Game(Plugin p){
 		
 		plugin = p;
 		
 		scoreboard = new MScoreBoard();
 		
 		team1 = scoreboard.getTeams()[0];
 		team2 = scoreboard.getTeams()[1];
 		
 		gameData    = Config.getGameData();
 		spawnData   = Config.getSpawnData();
 		stationData = Config.getStationData();
 		bombData    = Config.getBombData();
 		signData    = Config.getSignData();
 		
 		initSign();
 	}
 	
 	private void initSign(){
 		
 		for(SignData sign : signData.getSignsList()){
 			
 			Location loc = new Location(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
 			
 			signs.add(new Sign(loc, this));
 		}
 	}
 	
 	public void initRound(){
 		
 		unregisterAll();
 		
 		registerJoin();
 		registerMove();
 		
 		isStarted = true;
 		
 		pml.canMove = false;
 		
 		isBombEx = false;
 		isBombSet = false;
 		
 		spawnIndexAt = 0;
 		spawnIndexDe = 0;
 		
 		signsRefresh();
 		
 		new Counter(gameData.getPreGameTime(), 1000, new OnCounterCompleteEvent() {
			
			@Override
			public void onCounterComplete(Counter counter) {
				
				startRound();
			}
		}, new OnCounterTickEvent() {
			
			@Override
			public void tick(int s) {
				
				scoreboard.updatePreGameTimer(s);
			}
		}).start();
 		
 		for(Location[] loc : stationData.getStationLocation()){
 			
 			if(loc.length == 2){
 				
 				Block b1 = loc[0].getBlock();
 				Block b2 = loc[1].getBlock();
 				
 				b1.setType(Material.FURNACE);
 				b2.setType(Material.FURNACE);
 				
 				station.add(new BombStation(new Block[]{b1, b2}));
 			}
 		}
 	}
 	
 	private void signsRefresh() {
		
		for(Sign sign : signs){
			
			sign.refresh();
		}
	}

	public void startRound() {
		
 		registerInventory();
 		registerKill();
 		
		isRunning = true;
		pml.canMove = true;
		
		signsRefresh();
		
		for(Player p : playerList){
			
			spawnPlayer(p);
		}
		
		spawnBombs();
		registerStationEvents();
		
		mCounter = new Counter(gameData.getRoundTime(), 1000, new OnCounterCompleteEvent() {
			
			@Override
			public void onCounterComplete(Counter counter) {
				
				stopRound();
			}
		}, new OnCounterTickEvent() {
			
			@Override
			public void tick(int s) {
				
				scoreboard.updateTimer(s);
			}
		});
		
		mCounter.start();
	}
 	
 	public void stopRound() {
		
 		mCounter.interrupt();
 		
 		if(!isBombSet){
 			
 			Team2 winner = null;
 			Team2 loser = null;
 			
 			if(team1.getType() == SADTeamType.ATTACKER && team2.getType() == SADTeamType.DEFENDER){
 			
 				if(isBombEx){
 				
 					winner = team1;
 					loser  = team2;
 				}else{
 				
 					winner = team2;
 					loser  = team1;
 				}
 			}else if(team2.getType() == SADTeamType.ATTACKER && team1.getType() == SADTeamType.DEFENDER){
 				
 				if(isBombEx){
 	 				
 					winner = team2;
 					loser  = team1;
 				}else{
 				
 					winner = team1;
 					loser  = team2;
 				}
 			}
 					
 			winner.wins++;
 			loser.loses++;
 			
 			if(winner.wins == (gameData.getRounds() / 2))
 				endGame(winner);
 			else{
 				
 				switchTeams();
 				initRound();
 			}
 		}
	}

	private void stopRound(Team2 winner) {
		
		mCounter.interrupt();
		
		Team2 loser = null;
		
		if(winner == team1)
			loser = team2;
		else if(winner == team2)
			loser = team1;
		
		winner.wins++;
		loser.loses++;
			
		if(winner.wins >= (gameData.getRounds() / 2))
			endGame(winner);
		else{
			switchTeams();
			initRound();
		}
	}
	
	private void endGame(Team2 winner) {
		
		isStarted = false;
		isRunning = false;
		
		signsRefresh();
		
		for(Player p : playerList){
			
			if(p.team == winner){
				
				p.bukkitPlayer.sendMessage("You won.");
			}else{
				
				p.bukkitPlayer.sendMessage("You lost.");
			}
			
			p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			p.bukkitPlayer.teleport(new Location(Bukkit.getWorld(gameData.getLobbyName()), 0, 0, 0), TeleportCause.PLUGIN);
		}
		
		initRound();
	}

	private void registerStationEvents(){
		
		OnBombExplodeEvent ev1 = new OnBombExplodeEvent() {
			
			@Override
			public void onBombExplode(BombStation bs, boolean explode, int radius,
					boolean killAll) {
				
				isBombSet = false;
				isBombEx = true;
				stopRound();
			}
			
			@Override
			public void onBombExplode(BombStation bs, boolean explode, int radius) {
				
				isBombSet = false;
				isBombEx = true;
				stopRound();
			}
			
			@Override
			public void onBombExplode(BombStation bs, boolean explode) {
				
				isBombSet = false;
				isBombEx = true;
				stopRound();
			}
		};
		
		OnBombPlantedEvent ev2 = new OnBombPlantedEvent() {
			
			@Override
			public void onBombPlanted(Player p, BombStation bs) {
				
				isBombSet = true;
			}
		};
		
		OnBombDefusedEvent ev3 = new OnBombDefusedEvent() {

			@Override
			public void onBombDefused(Player p, BombStation bs) {
				
				isBombSet = false;
				stopRound();
			}
		};
		
		for(BombStation bs : station){
			
			bs.registerOnBombExplodeEvent(ev1);
			bs.registerOnBombPlantedEvent(ev2);
			bs.registerOnBombDefusedEvent(ev3);
		}
	}
	
	public void registerAll(){
		
		unregisterAll();
		
		registerJoin();
		registerMove();
		registerInventory();
	 	registerKill();	
	}

	public void registerJoin(){
		
		System.out.println("register playerjoin");
		
		if(pjl == null){
			
			pjl = new PlayerJoinListener(plugin);
			plugin.getServer().getPluginManager().registerEvents(pjl, plugin);
		}
	}
	
	public void registerMove(){
		
		System.out.println("register playermove");
		
		if(pml == null){
		
			pml = new PlayerMoveListener();
			plugin.getServer().getPluginManager().registerEvents(pml, plugin);
		}
	}
	
	public void registerInventory(){
		
		System.out.println("register inventory");
		
		if(iol == null){
			
			iol = new InventoryOpenListener(plugin);
			plugin.getServer().getPluginManager().registerEvents(iol, plugin);
		}
	}
	
	private void registerKill() {
		
		System.out.println("register kill");
		
		if(pkl == null){
		
			pkl = new PlayerKillListener(this);
			plugin.getServer().getPluginManager().registerEvents(pkl, plugin);
		}
	}

	public void unregisterAll() {
		
		pjl = null;
		pml = null;
		iol = null;
		pkl = null;
		
		HandlerList.unregisterAll(plugin);
	}
 	
 	private void spawnPlayer(Player p) {
 		
 		if(p.getTeam().getType() == SADTeamType.ATTACKER){
 			
 			p.bukkitPlayer.teleport(spawnData.getAtSpawns().get(spawnIndexAt));
 			spawnIndexAt++;
 		}else if(p.getTeam().getType() == SADTeamType.DEFENDER){
 			
 			p.bukkitPlayer.teleport(spawnData.getDeSpawns().get(spawnIndexDe));
 			spawnIndexDe++;
 		}
 	}
 	
 	private void spawnBombs(){
 		
 		for(Location loc : bombData.getBombLocation()){
 			
 			loc.getWorld().dropItem(loc, new ItemStack(Material.TNT, 1));
 		}
 	}
 	
 	private void switchTeams() {
		
		String pre1    = team1.getTeam().getPrefix();
		String pre2    = team2.getTeam().getPrefix();
		
		String name1   = team1.getTeam().getDisplayName();
		String name2   = team2.getTeam().getDisplayName();
		
		SADTeamType t1 = team1.getType();
		SADTeamType t2 = team2.getType();
		
		team1.getTeam().setPrefix(pre2);
		team1.getTeam().setDisplayName(name2);
		team1.setType(t2);		
		
		team2.getTeam().setPrefix(pre1);
		team2.getTeam().setDisplayName(name1);
		team2.setType(t1);	
		
		initScore();
		
		scoreboard.switchSB();
		
		scoreboard.reloadSB();
	}
 	
 	private void initScore(){
		
		if(team1.getType() == SADTeamType.ATTACKER){
			
			scoreboard.setScore(team1.wins, team2.wins);
		}else if(team2.getType() == SADTeamType.ATTACKER){
			
			scoreboard.setScore(team2.wins, team1.wins);
		}
		
		scoreboard.reloadSB();
	}
 	
 	public boolean isFull(){
 		
 		return team1.isFull() && team2.isFull();
 	}

	public boolean isRunning() {
		
		return isRunning;
	}

	public void join(Player p) {
		
		playerList.add(p);
		
		if(playerList.size() >= Config.getMinData().getPlayer() && !isStarted()){
			
			
		}
		
		if(team1.getSize() > team2.getSize()){
			
			team2.addPlayer(p);
		}else{
			
			team1.addPlayer(p);
		}
		
		spawnPlayer(p);
		
		signsRefresh();
	}
	
	private boolean isStarted() {
		
		return isStarted ;
	}

	public void leave(Player p){
		
		for(int i = 0; i < playerList.size(); i++){
			
			if(playerList.get(i).bukkitPlayer.getName().equalsIgnoreCase(p.bukkitPlayer.getName()))
				playerList.remove(i);
		}
		
		if(team1.isPlayersTeam(p)){
			
			team1.removePlayer(p);
		}else if(team2.isPlayersTeam(p)){
			
			team2.removePlayer(p);
		}
		
		signsRefresh();
	}
	
	public void onPlayerKilled(org.bukkit.entity.Player entity) {
		
		Player player = new Player(entity);
		
		if(team1.isPlayersTeam(player))
			team1.onPlayerKilled(player.bukkitPlayer.getName());
		else if(team2.isPlayersTeam(player))
			team2.onPlayerKilled(player.bukkitPlayer.getName());
		
		if(team1.living == 0)
			stopRound(team1);
		else if(team2.living == 0)
			stopRound(team2);
	}
	
	public BombStation getStation(Block b){
		
		for(BombStation station : this.station){
			
			ArrayList<Block> block = new ArrayList<Block>();
			block.add(station.getBlocks()[0]);
			block.add(station.getBlocks()[1]);
			
			if(block.contains(b)){
				
				return station;
			}
		}
		
		return null;
	}
	
	public Player getPlayer(String name){
		
		for(int i = 0; playerList.size() > i; i++){
			
			if(playerList.get(i).bukkitPlayer.getName().equals(name)){
				
				return playerList.get(i);
			}
		}
		
		return null;
	}

	public String getWorldName() {
		
		return gameData.getWorldName();
	}
	
	public boolean hasPlayer(Player p){
		
		for(int i = 0; i < playerList.size(); i++){
			
			if(playerList.get(i).bukkitPlayer.getName().equalsIgnoreCase(p.bukkitPlayer.getName()))
				return true;
		}
		
		return false;
	}

	public void kickLastPlayer() {
		
		Player p = playerList.get(playerList.size()-1);
		
		playerList.remove(p);
		p.bukkitPlayer.kickPlayer("You were kick for an vip.");
	}

	public int getMax() {
		
		return team1.maxPlayer+team2.maxPlayer;
	}

	public int getOnline() {
		
		return playerList.size();
	}
}

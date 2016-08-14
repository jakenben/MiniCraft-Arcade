package me.jake861.BenServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Spleef implements Listener {

	private Main plugin;
	
	////////////////////////////////
	////////////////////////////////
	//      STORED MAP SPAWNS     //
	////////////////////////////////
	////////////////////////////////
	
	public Location lobbySpawn;
	public Location[] map1SpawnLocations = new Location[12];
	public Location map1SpectatorSpawn;
	
	public Location[] map2SpawnLocations = new Location[12];
	public Location map2SpectatorSpawn;
	
	////////////////////////////////
	////////////////////////////////
	//     CURRENT MAP SPAWNS     //
	////////////////////////////////
	////////////////////////////////
	
	public World spleef = Bukkit.getWorld("Spleef");
	public World spleefLobby = Bukkit.getWorld("SpleefLobby");
	
	public Location spectatorSpawn;
	public Location[] spawnLocations = new Location[12]; 
	
	
	public Spleef(Main plugin) {
		this.plugin = plugin;
		
		if(spleef == null) {
			WorldCreator wc = new WorldCreator("Spleef");
			spleef = Bukkit.createWorld(wc);
		}
		
		if(spleefLobby == null) {
			WorldCreator wc = new WorldCreator("SpleefLobby");
			spleefLobby = Bukkit.createWorld(wc);
		}
		
		lobbySpawn = new Location(spleefLobby, -17.5, 67.5, 13.5, 0, 0);
		
		map1SpectatorSpawn = new Location(spleef, 0, 70, 0);
		
		map1SpawnLocations[0] = new Location(spleef, 0, 64, 9);
		map1SpawnLocations[1] = new Location(spleef, 0, 64, 7);
		map1SpawnLocations[2] =	new Location(spleef, -4, 64, 7);
		map1SpawnLocations[3] = new Location(spleef, -5, 64, 3);
		map1SpawnLocations[4] = new Location(spleef, -4, 64, 0);
		map1SpawnLocations[5] = new Location(spleef, -2, 64, -2);
		map1SpawnLocations[6] = new Location(spleef, 0, 64, -3);
		map1SpawnLocations[7] = new Location(spleef, 2, 64, 0);
		map1SpawnLocations[8] = new Location(spleef, 5, 64, -3);
		map1SpawnLocations[9] = new Location(spleef, 0, 64, 0);
		map1SpawnLocations[10] = new Location(spleef, 0, 64, 0);
		map1SpawnLocations[11] = new Location(spleef, 0, 64, 0);
		
		
		map2SpectatorSpawn = new Location(spleef, 105, 80, -55);
		
		map2SpawnLocations[0] = new Location(spleef, 105, 68, -55);
		map2SpawnLocations[1] = new Location(spleef, 105, 68, -56);
		map2SpawnLocations[2] =	new Location(spleef, 105, 68, -57);
		map2SpawnLocations[3] = new Location(spleef, 105, 68, -58);
		map2SpawnLocations[4] = new Location(spleef, 105, 68, -59);
		map2SpawnLocations[5] = new Location(spleef, 105, 68, -60);
		map2SpawnLocations[6] = new Location(spleef, 105, 68, -61);
		map2SpawnLocations[7] = new Location(spleef,  105, 68, -62);
		map2SpawnLocations[8] = new Location(spleef, 105, 68, -63);
		map2SpawnLocations[9] = new Location(spleef, 105, 68, -64);
		map2SpawnLocations[10] = new Location(spleef, 105, 68, -65);
		map2SpawnLocations[11] = new Location(spleef, 105, 68, -66);	
	}
	
	public List<UUID> players = new ArrayList<>();
	public List<UUID> spectators = new ArrayList<>();
	public List<UUID> deadPlayers = new ArrayList<>();
	
	public Map<Location, Material> blocks = new HashMap<>();
	public Map<Location, Byte> data = new HashMap<>();
	
	public Map<UUID, Integer> breaks = new HashMap<>();
	Player winner;
	
	public int MAX_PLAYERS = 16;
	
	
	
	public int spawnNum = 0;
	
	public String status = "WAITING";
	
	public String currentMap = "Stumps";
	
	
	
	public void addPlayer(UUID playerUuid) {
		

		
		
		players.add(playerUuid);
		breaks.put(playerUuid, 0);
		plugin.scoreboards.updateScoreboard();
		Player player = Bukkit.getPlayer(playerUuid);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(player);
		}
		
		for(UUID plUuid : players) {
			Player pl = Bukkit.getPlayer(plUuid);
			pl.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + player.getName() + " joined the game!");
		}
		
	
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setFlying(false);
		player.setAllowFlight(false);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		
		updateScoreboard();
		
		ItemStack clock = new ItemStack(Material.WATCH);
		ItemMeta clockMeta = clock.getItemMeta();
		clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
		clock.setItemMeta(clockMeta);
		player.getInventory().setItem(8, clock);
		
		if(status == "WAITING") {
			if(players.size() == 2) {
				startLobbyCountdown();
			} else if (players.size() < 2) {
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Waiting for players to start.");
			}
			player.teleport(lobbySpawn);
		} else {
			addSpectator(playerUuid);
		}
		
	
		
		
		
	}
	
	
	public void removePlayer(UUID playerUuid) {
		players.remove(playerUuid);
		Player player = Bukkit.getPlayer(playerUuid);
		plugin.scoreboards.updateScoreboard();
		for(UUID plUuid : players) {
			Player pl = Bukkit.getPlayer(plUuid);
			pl.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + player.getName() + " left the game.");
		}
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			pl.showPlayer(player);
		}
		
		player.setFlying(false);
		player.setAllowFlight(false);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setGameMode(GameMode.SURVIVAL);
		
		if(spectators.contains(playerUuid)) {
			spectators.remove(playerUuid);
		}
		
		if(deadPlayers.contains(playerUuid)) {
			deadPlayers.remove(playerUuid);
		}
		
		if(players.size() - spectators.size() - deadPlayers.size() <= 1) {
		
			if(status == "WAITING") {
				Bukkit.getScheduler().cancelTask(lobbyId);
			}
			
			if(status == "COUNTING") {
				Bukkit.getScheduler().cancelTask(lobbyId);
				status = "WAITING";
				updateScoreboard();
			}
			
			if(status == "STARTING") {
				Bukkit.getScheduler().cancelTask(startId);
				endGame();
			}
			
			if(status == "STARTED") {
				endGame();
			}
			
		}
		
		updateScoreboard();
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			removePlayer(event.getPlayer().getUniqueId());
		}
	}
	
	int lobbyId;
	int lobbyCount;
	
	public void startLobbyCountdown() {
		lobbyCount = 21;
		
		chooseMap();
		
		status = "COUNTING";

		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + currentMap + ChatColor.GRAY + " was selected as the map!");
			breaks.put(playerUuid, 0);
		}
		
		lobbyId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				lobbyCount--;
				updateScoreboard();
				if(lobbyCount == 20) {
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "Game starting in 20 seconds!");
					}
				}
				
				if(lobbyCount <= 10 && lobbyCount > 0) {
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						
						player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 0);
					}
				}
				
				if(lobbyCount <= 0) {
					
					startGame();
					Bukkit.getScheduler().cancelTask(lobbyId);
					
				}
				
			}
		}, 0, 20);
	}
	
	public void chooseMap() {
		
		Random rand = new Random();
		
		if(rand.nextInt() >= 1) {
			setMap1();
			currentMap = "Stumps";
		} else {
			setMap2();
			currentMap = "Hills";
		}
		
		
	}
	
	public void setMap1() {
		for(int i = 0; i < 12; i++) {
			spawnLocations[i] = map1SpawnLocations[i];
		}
		spectatorSpawn = map1SpectatorSpawn;
	}
	
	public void setMap2() {
		for(int i = 0; i < 12; i++) {
			spawnLocations[i] = map2SpawnLocations[i];
		}
		spectatorSpawn = map2SpectatorSpawn;
	}
	
	public void addSpectator(UUID playerUuid) {
		Player player = Bukkit.getPlayer(playerUuid);
		spectators.add(playerUuid);
		
		for(Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
		}
		player.teleport(spectatorSpawn);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		ItemStack clock = new ItemStack(Material.WATCH);
		ItemMeta clockMeta = clock.getItemMeta();
		clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
		clock.setItemMeta(clockMeta);
		player.getInventory().setItem(8, clock);
		
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setHealth(20);
		player.setFoodLevel(20);
	}
	
	
	public void addDead(UUID playerUuid) {
		Player player = Bukkit.getPlayer(playerUuid);
		deadPlayers.add(playerUuid);
		
		for(Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
		}
		player.teleport(spectatorSpawn);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		ItemStack clock = new ItemStack(Material.WATCH);
		ItemMeta clockMeta = clock.getItemMeta();
		clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
		clock.setItemMeta(clockMeta);
		player.getInventory().setItem(8, clock);
		
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setHealth(20);
		player.setFoodLevel(20);
		
		updateScoreboard();
	}
	
	
	int startId;
	int startCount;
	
	public void startGame() {
		
		for(UUID playerUuid : players) {
			
			Player player = Bukkit.getPlayer(playerUuid);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(player);
			}
			
			player.setFlying(false);
			player.setAllowFlight(false);
			player.teleport(spawnLocations[spawnNum]);
			spawnNum++;
			if(spawnNum >= MAX_PLAYERS) {
				spawnNum = 0;
			}
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.sendMessage(ChatColor.AQUA + "=====================================");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.YELLOW + " " + ChatColor.BOLD + "Spleef");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + "  Dig out the ground in order to make ");
			player.sendMessage(ChatColor.GRAY + "  your opponents fall!");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.AQUA + "=====================================");
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			
			
		}
			
			
		status = "STARTING";
		startCount = 6;
		
		startId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				startCount--;
				
				updateScoreboard();
				for(UUID playerUuid : players) {
					Player player = Bukkit.getPlayer(playerUuid);
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 0);
					player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + Integer.toString(startCount));
				}
				
				if(startCount <= 0) {
					start();
					status = "STARTED";
					plugin.getServer().getScheduler().cancelTask(startId);
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 0);
					}
				}
			}
		}, 0, 20);
		
	}
	
	public void start() {
		status = "STARTED";
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 0);
		}
		
		updateScoreboard();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(players.contains(player.getUniqueId())) {
			if(status == "STARTED") {
				if(player.getLocation().getY() <= 30 && !spectators.contains(player.getUniqueId()) && !deadPlayers.contains(player.getUniqueId())) {
				
					player.setHealth(20);
					player.setFoodLevel(20);
					addDead(player.getUniqueId());
					updateScoreboard();
					for(UUID playerUuid : players) {
						Player p = Bukkit.getPlayer(playerUuid);
						p.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + player.getName() + " died.");
					}
					
					if(players.size() - spectators.size() - deadPlayers.size() <= 1) {
						endGame();
					}
					
				}
			}
		}
		
	}
	
	int endId;
	int endCount;
	
	public void endGame() {
		status = "ENDED";
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setHealth(20);
			player.setFoodLevel(20);
			if(!spectators.contains(player.getUniqueId()) && !deadPlayers.contains(player.getUniqueId())) {
				winner = player;
			}
			
		}
		
		
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			if(winner != null) {
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.YELLOW + "  " + ChatColor.BOLD + "Game Over!");
				player.sendMessage(" ");
				player.sendMessage("  " + winner.getName() + " won the game!" );
				player.sendMessage(" ");
				player.sendMessage(ChatColor.AQUA + "=====================================");
			} else {
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.YELLOW + "  " + ChatColor.BOLD + "Game Over!");
				player.sendMessage(" ");
				player.sendMessage("  Nobody won the game!" );
				player.sendMessage(" ");
				player.sendMessage(ChatColor.AQUA + "=====================================");
			}
		}
		
		giveCoins();
		
		endCount = 9;
		
		status = "ENDED";
		endId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				
				endCount--;
				updateScoreboard();
				
				if(endCount <= 0) {
					spawnNum = 0;
					status = "WAITING";
	
					removeAll();
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							resetMap();
						}
					}, 20);
						
					
					   List<Entity> entList = spleef.getEntities();
					   
					   for(Entity ent : entList) {
						   if(ent instanceof Item) {
							   ent.remove();
						   }
					   }
					 
					  
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						player.setFlying(false);
						player.setAllowFlight(false);
						for(Player p : Bukkit.getOnlinePlayers()) {
	
							p.showPlayer(player);
							
						}
					}
					
					
					plugin.getServer().getScheduler().cancelTask(endId);
				}
			}
			
			
		}, 0, 20);
	}
	
	
	public void giveCoins() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				for(UUID playerUuid : players) {
					Player player = Bukkit.getPlayer(playerUuid);
					
					player.sendMessage(ChatColor.AQUA + "=====================================");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  Coins");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.WHITE + "  Participation: " + ChatColor.YELLOW + "10");
					plugin.accounts.addCoins(player, 10);
					if(winner == player) {
						player.sendMessage(ChatColor.WHITE + "  Winner: " + ChatColor.YELLOW + "20");
						plugin.accounts.addCoins(player, 20);
					}
					if(breaks.get(playerUuid) != 0) {
						player.sendMessage(ChatColor.WHITE + "  Breaks: " + ChatColor.YELLOW + Integer.toString((int) (breaks.get(playerUuid) * .4)));
						plugin.accounts.addCoins(player, (int) (breaks.get(playerUuid) * .4));
					}
					player.sendMessage(" ");
					player.sendMessage(ChatColor.YELLOW + "  You currently have " + Integer.toString(plugin.accounts.getCoins(player)) + " coins!");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.AQUA + "=====================================");
					
				}
			}
		}, 80);
	}
	
	
	public void removeAll() {
		
		@SuppressWarnings("rawtypes")
		Iterator spec = spectators.iterator();
		
		while(spec.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) spec.next();
			spec.remove();
		}
		
		@SuppressWarnings("rawtypes")
		Iterator dead = deadPlayers.iterator();
		
		while(dead.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) dead.next();
			dead.remove();
		}
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.teleport(lobbySpawn);
			breaks.put(playerUuid, 0);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(player);
			}
			
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			ItemStack clock = new ItemStack(Material.WATCH);
			ItemMeta clockMeta = clock.getItemMeta();
			clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
			clock.setItemMeta(clockMeta);
			player.getInventory().setItem(8, clock);
			
			player.setFlying(false);
			player.setAllowFlight(false);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setGameMode(GameMode.SURVIVAL);
		}
		
		if(players.size() >= 2) {
			startLobbyCountdown();
		} else {
			for(UUID playerUuid : players) {
				Player player = Bukkit.getPlayer(playerUuid);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Waiting for players to start.");
			}
		}
		
		updateScoreboard();
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player.getUniqueId()) && !spectators.contains(player.getUniqueId()) && !deadPlayers.contains(player.getUniqueId())) {
			if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(status != "STARTED") {
					event.setCancelled(true);
				} else {
					
					Block block = event.getClickedBlock();
					blocks.put(block.getLocation(), block.getType());
					data.put(block.getLocation(), block.getData());
					event.getClickedBlock().setType(Material.AIR);
					player.playSound(player.getLocation(), Sound.DIG_GRASS, 1, 0);
					breaks.put(player.getUniqueId(), breaks.get(player.getUniqueId()) + 1);
						
					
				}
			}
		}
		
		if(players.contains(player.getUniqueId())) {
			if(player.getItemInHand().getType() != Material.AIR) {
				if(player.getItemInHand().getItemMeta().hasDisplayName()) {
					if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Hub")) {
						plugin.getServer().dispatchCommand(player, "hub");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryInteractEvent(InventoryInteractEvent event) {
		if(players.contains(((Player) event.getWhoClicked()).getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if(players.contains((event.getPlayer()).getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			if(players.contains(((Player) event.getEntity()).getUniqueId())) {
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
			if(players.contains(damager.getUniqueId()) || players.contains(player.getUniqueId())) {
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(players.contains(((Player) event.getEntity()).getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	public void resetMap() {
		
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.getWorld().equals(spleef)) {
						player.teleport(plugin.hub.spawn);
						player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "The Spleef world is reloading. You can't be in there!");
					}
				}
		
				unloadWorld();
				loadWorld();
				saveWorld();
	


	}
	
		
		public void unloadWorld() {
			if(Bukkit.getServer().unloadWorld("Spleef", false)) {
				plugin.getLogger().info("Successfully unloaded Spleef.");
			} else {
				plugin.getLogger().severe("COULD NOT UNLOAD SPLEEF");
			}
		}
		
		public void loadWorld() {
			World world = Bukkit.getServer().createWorld(new WorldCreator("Spleef"));
			world.setAutoSave(false);
			world.setMonsterSpawnLimit(0);
			world.setAnimalSpawnLimit(0);
			world.setDifficulty(Difficulty.PEACEFUL);
		}
		
		public void saveWorld() {
			Bukkit.getWorld("Spleef").save();
			Bukkit.getWorld("Spleef").setAutoSave(false);
		}
		
		
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent event) {
			if(players.contains(event.getPlayer().getUniqueId())) {
		
				event.setCancelled(true);
				
			}
		}
		
		
		
		
		
	
		
		
		
		
		ScoreboardManager manager;
		Scoreboard scoreboard;
		Objective obj;
		
		public void updateScoreboard() {
			
			manager = null;
			scoreboard = null;
			obj = null;
			
			manager = Bukkit.getScoreboardManager();
			scoreboard = manager.getNewScoreboard();
			obj = scoreboard.registerNewObjective("Cactice", "dummy");
			
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			if(status == "STARTING" || status == "STARTED" || status == "ENDED") {
			
				obj.setDisplayName(ChatColor.AQUA + "   " + ChatColor.BOLD + "Mini" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Craft" + ChatColor.RESET + "   ");
			
				
				obj.getScore(ChatColor.WHITE.toString()).setScore(15);
				
				if(players.size() < 14) {
				
					int counter = 14;
					
					for(UUID uuid : players) {
						Player player = Bukkit.getPlayer(uuid);
						
						if(!deadPlayers.contains(player.getUniqueId()) && !spectators.contains(player.getUniqueId())) {
							obj.getScore(ChatColor.GREEN + player.getName()).setScore(counter);
						} else if(deadPlayers.contains(player.getUniqueId())) {
							obj.getScore(ChatColor.DARK_GRAY + player.getName()).setScore(counter);
						}
						
						
						counter--;
					}
				
				} else {
					obj.getScore(ChatColor.GREEN + "Alive Players: " + ChatColor.WHITE + Integer.toString(players.size() - spectators.size() - deadPlayers.size())).setScore(14);
					obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
					obj.getScore(ChatColor.RED + "Dead Players: " + ChatColor.WHITE + Integer.toString(deadPlayers.size())).setScore(12);
				}
				
			
			} else if(status == "WAITING") {
				obj.setDisplayName(ChatColor.BLUE + "WAITING FOR PLAYERS");
				obj.getScore(ChatColor.WHITE.toString()).setScore(15);
				obj.getScore(ChatColor.AQUA + "Players: " + ChatColor.WHITE + Integer.toString(players.size())).setScore(14);;
				obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
			} else if(status == "COUNTING") {
				obj.setDisplayName(ChatColor.BLUE + "" + "STARTING IN " + ChatColor.AQUA + "" + Integer.toString(lobbyCount) + ChatColor.BLUE + " SECONDS");
				obj.getScore(ChatColor.WHITE.toString()).setScore(15);
				obj.getScore(ChatColor.AQUA + "Players: " + ChatColor.WHITE + Integer.toString(players.size())).setScore(14);;
				obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
			}
			
			for(UUID uuid : players) {
				Player player = Bukkit.getPlayer(uuid);
				player.setScoreboard(scoreboard);
			}
		}
		
		
}

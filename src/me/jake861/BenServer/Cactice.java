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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Cactice implements Listener {

	private Main plugin;
	
	
	////////////////////////////////
	////////////////////////////////
	//      STORED MAP SPAWNS     //
	////////////////////////////////
	////////////////////////////////
	
	public World cactice = Bukkit.getWorld("Cactice");
	
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
	
	public Location[] spawnLocations = new Location[12];
	public Location spectatorSpawn;
	
	
	public Cactice(Main plugin) {
		this.plugin = plugin;
		
		if(cactice == null) {
			WorldCreator wc = new WorldCreator("Cactice");
			cactice = Bukkit.createWorld(wc);
		}
		lobbySpawn = new Location(cactice, -96.5, 5.5, -14.5);
		
		map1SpectatorSpawn = new Location(cactice, 0, 15, 0);
		map1SpawnLocations[0] = new Location(cactice, 0, 5, 9);
		map1SpawnLocations[1] = new Location(cactice, 0, 5, 7);
		map1SpawnLocations[2] =	new Location(cactice, -4, 5, 7);
		map1SpawnLocations[3] = new Location(cactice, -5, 5, 3);
		map1SpawnLocations[4] = new Location(cactice, -4, 5, 0);
		map1SpawnLocations[5] = new Location(cactice, -2, 5, -2);
		map1SpawnLocations[6] = new Location(cactice, 0, 5, -3);
		map1SpawnLocations[7] = new Location(cactice, 2, 5, 0);
		map1SpawnLocations[8] = new Location(cactice, 5, 5, -3);
		map1SpawnLocations[9] = new Location(cactice, 0, 5, 0);
		map1SpawnLocations[10] = new Location(cactice, 0, 5, 0);
		map1SpawnLocations[11] = new Location(cactice, 0, 5, 0);
		
		map2SpectatorSpawn = new Location(cactice, -189, 64, 38);
		map2SpawnLocations[0] = new Location(cactice, -186, 60, 54);
		map2SpawnLocations[1] = new Location(cactice, -189, 60, 38);
		map2SpawnLocations[2] =	new Location(cactice, -193, 60, 64);
		map2SpawnLocations[3] = new Location(cactice, -200, 60, 61);
		map2SpawnLocations[4] = new Location(cactice, -196, 60, 76);
		map2SpawnLocations[5] = new Location(cactice, -227, 60, 59);
		map2SpawnLocations[6] = new Location(cactice, -229, 60, 44);
		map2SpawnLocations[7] = new Location(cactice, -212, 60, 23);
		map2SpawnLocations[8] = new Location(cactice, -193, 60, 23);
		map2SpawnLocations[9] = new Location(cactice, -175, 60, 40);
		map2SpawnLocations[10] = new Location(cactice, -193, 60, 50);
		map2SpawnLocations[11] = new Location(cactice, -202, 60, 58);
	}
	
	public List<UUID> players = new ArrayList<>();
	public List<UUID> spectators = new ArrayList<>();
	public List<UUID> deadPlayers = new ArrayList<>();
	
	public Map<UUID, Integer> hits = new HashMap<>();
	public Map<UUID, String> kit = new HashMap<>();
	public Map<UUID, Boolean> wallerCooldownComplete = new HashMap<>();
	
	Player winner;
	
	public int spawnNum = 0;
	public int MAX_PLAYERS = 16;
	
	public String status = "WAITING";
	
	
	public String currentMap = "Surrounded";

	
	
	public void addPlayer(UUID playerUuid) {
	
		players.add(playerUuid);
		hits.put(playerUuid, 0);
		kit.put(playerUuid, "DEFAULT");
		wallerCooldownComplete.put(playerUuid, false);
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
		
		if(status == "WAITING" || status == "COUNTING") {
			if(players.size() == 2) {
				startLobbyCountdown();
			} else if (players.size() < 2) {
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Waiting for players to start.");
			}
			player.teleport(lobbySpawn);
			player.sendMessage(ChatColor.BLUE + "[Game] "+ ChatColor.YELLOW + "This game has a kit called Waller! Type /kit waller to choose or purchase the kit for 1000 coins!");
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
		
		updateScoreboard();
		
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
		
		
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			removePlayer(event.getPlayer().getUniqueId());
		}
	}
	
	public void addSpectator(UUID playerUuid) {
		Player player = Bukkit.getPlayer(playerUuid);
		spectators.add(playerUuid);
		
		updateScoreboard();
		
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
		   List<Entity> entList = cactice.getEntities();
		   
		   for(Entity ent : entList) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
	}
	
	
	public void addDead(UUID playerUuid) {
		Player player = Bukkit.getPlayer(playerUuid);
		deadPlayers.add(playerUuid);
		
		updateScoreboard();
		
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
		   List<Entity> entList = cactice.getEntities();
		   
		   for(Entity ent : entList) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
	}
	
	
	int lobbyId;
	int lobbyCount;
	
	public void startLobbyCountdown() {
		lobbyCount = 21;
		
		status = "COUNTING";
		
		chooseMap();

		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + currentMap + ChatColor.GRAY + " was selected as the map!");
			hits.put(playerUuid, 0);
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
	
	
	int startId;
	int startCount;
	
	public void startGame() {
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			hits.put(playerUuid, 0);
			player.teleport(spawnLocations[spawnNum]);
			spawnNum++;
			if(spawnNum >= MAX_PLAYERS) {
				spawnNum = 0;
			}
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setFlying(false);
			player.setAllowFlight(false);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(ChatColor.AQUA + "=====================================");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.YELLOW + " " + ChatColor.BOLD + "Cactice");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + "  See that knockback stick? Knock your opponent ");
			player.sendMessage(ChatColor.GRAY + "  into the cactus with it!");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.AQUA + "=====================================");
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
		}
			
		status = "STARTING";
		startCount = 6;
		
		updateScoreboard();
		
		
		startId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				startCount--;
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
	
	public void chooseMap() {
		Random rand = new Random();
		int randomNumber = rand.nextInt(2);
		if(randomNumber == 0) {
			currentMap = "Surrounded";
			setMap1();
		} else {
			currentMap = "Town Square";
			setMap2();
		}
	}
	
	public void start() {
		status = "STARTED";
		for(UUID uuid : players) {
			if(!spectators.contains(uuid)) {
				Player player = Bukkit.getPlayer(uuid);
				ItemStack stick = new ItemStack(Material.STICK, 1);
				stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
				player.getInventory().addItem(stick);
			}
		}
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 0);
		}
		
		updateScoreboard();
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		if(players.contains(player.getUniqueId())) {
			event.getDrops().clear();
			player.setHealth(20);
			player.setFoodLevel(20);
			addDead(player.getUniqueId());
			updateScoreboard();
			event.setDeathMessage("");
			for(UUID plUuid : players) {
				Player pl = Bukkit.getPlayer(plUuid);
				pl.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + player.getName() + " died.");
			}
			if(players.size() - spectators.size() - deadPlayers.size() <= 1) {
				endGame();
			}
			   List<Entity> entList = cactice.getEntities();
			   
			   for(Entity ent : entList) {
				   if(ent instanceof Item) {
					   ent.remove();
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
		
		endCount = 9;
		
		giveCoins();
		status = "ENDING";
		endId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				endCount--;
				
				if(endCount <= 0) {
					
					spawnNum = 0;
					status = "WAITING";

					removeAll();
					   List<Entity> entList = cactice.getEntities();
					   
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
					
					@SuppressWarnings("rawtypes")
					Iterator iter = hits.entrySet().iterator();
					
					while(iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<UUID, Integer> playerHits = (Map.Entry<UUID, Integer>) iter.next();
						playerHits.setValue(0);
						iter.remove();
					}
					
					plugin.getServer().getScheduler().cancelTask(endId);
					
				}
			}
			
		}, 0, 20);
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
					if(hits.get(playerUuid) != 0) {
						player.sendMessage(ChatColor.WHITE + "  Hits: " + ChatColor.YELLOW + hits.get(playerUuid).toString());
						plugin.accounts.addCoins(player, hits.get(playerUuid));
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
		
		Bukkit.getScheduler().cancelTask(wallerId);
		
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
			hits.put(playerUuid, 0);
			kit.put(playerUuid, "DEFAULT");
			
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
		
	}
	
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(players.contains(player.getUniqueId())) {
				event.setCancelled(true);
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
				if(status != "STARTED") {
					event.setCancelled(true);
				}
				if(spectators.contains(((Player) event.getEntity()).getUniqueId()) || deadPlayers.contains(((Player) event.getEntity()).getUniqueId())) {
					event.setCancelled(true);
				}
				if(event.getCause().equals(DamageCause.CONTACT)) {
					event.setDamage(3);
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
			if(players.contains(damager.getUniqueId()) || players.contains(player.getUniqueId())) {
				if(status != "STARTED" || spectators.contains(damager.getUniqueId()) || deadPlayers.contains(damager.getUniqueId())) {
					event.setCancelled(true);
				}
			}
			if(players.contains(damager.getUniqueId()) && players.contains(player.getUniqueId())) {
				if(status == "STARTED") {
					event.setDamage(0);
					hits.put(damager.getUniqueId(), hits.get(damager.getUniqueId()) + 1);
				}
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
	
	public static String getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player.getUniqueId())) {
			if(status == "STARTED") {
				if(kit.get(player.getUniqueId()).equalsIgnoreCase("WALLER")) {
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if(player.getItemInHand().getType() == Material.STICK) {
							if(wallerCooldownComplete.get(player.getUniqueId())) {
								Block block = event.getClickedBlock();
								if(block.getType() != Material.ICE && block.getType() != Material.SAND && block.getType() != Material.CACTUS){
									player.sendMessage(ChatColor.BLUE + "[Game] "+ ChatColor.GRAY + "You can use Waller in 10 seconds.");
									startWallerCooldown(player);
									
									
									if(getDirection(player).equalsIgnoreCase("N") || getDirection(player).equalsIgnoreCase("S")) {
										Block block1 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block1Type = block1.getType();
										if(block1Type == Material.SMOOTH_BRICK) {
											block1Type = Material.AIR;
										}
										if(block1Type != Material.CACTUS) {
											block1.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block1, block1Type);
										
										Block block2 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ() - 1));
										Material block2Type = block2.getType();
										if(block2Type == Material.SMOOTH_BRICK) {
											block2Type = Material.AIR;
										}
										if(block2Type != Material.CACTUS) {
											block2.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block2, block2Type);
										
										Block block3 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ() + 1));
										Material block3Type = block3.getType();
										if(block3Type == Material.SMOOTH_BRICK) {
											block3Type = Material.AIR;
										}
										if(block3Type != Material.CACTUS) {
											block3.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block3, block3Type);
										
										Block block4 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block4Type = block4.getType();
										if(block4Type == Material.SMOOTH_BRICK) {
											block4Type = Material.AIR;
										}
										if(block4Type != Material.CACTUS) {
											block4.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block4, block4Type);
										
										Block block5 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ() - 1));
										Material block5Type = block5.getType();
										if(block5Type == Material.SMOOTH_BRICK) {
											block5Type = Material.AIR;
										}
										if(block5Type != Material.CACTUS) {
											block5.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block5, block5Type);
										
										Block block6 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ() + 1));
										Material block6Type = block6.getType();
										if(block6Type == Material.SMOOTH_BRICK) {
											block6Type = Material.AIR;
											
										}
										if(block6Type != Material.CACTUS) {
											block6.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block6, block6Type);
										
										Block block7 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block7Type = block7.getType();
										if(block7Type == Material.SMOOTH_BRICK) {
											block7Type = Material.AIR;
										}
										if(block7Type != Material.CACTUS) {
											block7.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block7, block7Type);
										
										Block block8 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ() - 1));
										Material block8Type = block8.getType();
										if(block8Type == Material.SMOOTH_BRICK) {
											block8Type = Material.AIR;
										}
										if(block8Type != Material.CACTUS) {
											block8.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block8, block8Type);
										
										Block block9 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ() + 1));
										Material block9Type = block9.getType();
										if(block9Type == Material.SMOOTH_BRICK) {
											block9Type = Material.AIR;
										}
										if(block9Type != Material.CACTUS) {
											block9.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block9, block9Type);
									}
									
									if(getDirection(player).equalsIgnoreCase("NE") || getDirection(player).equalsIgnoreCase("SW")) {
										Block block1 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block1Type = block1.getType();
										if(block1Type == Material.SMOOTH_BRICK) {
											block1Type = Material.AIR;
										}
										if(block1Type != Material.CACTUS) {
											block1.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block1, block1Type);
										
										Block block2 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()-1, block.getLocation().getY() + 1, block.getLocation().getZ() + 1));
										Material block2Type = block2.getType();
										if(block2Type == Material.SMOOTH_BRICK) {
											block2Type = Material.AIR;
										}
										if(block2Type != Material.CACTUS) {
											block2.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block2, block2Type);
										
										Block block3 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()+1, block.getLocation().getY() + 1, block.getLocation().getZ() - 1));
										Material block3Type = block3.getType();
										if(block3Type == Material.SMOOTH_BRICK) {
											block3Type = Material.AIR;
										}
										if(block3Type != Material.CACTUS) {
											block3.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block3, block3Type);
										
										Block block4 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block4Type = block4.getType();
										if(block4Type == Material.SMOOTH_BRICK) {
											block4Type = Material.AIR;
										}
										if(block4Type != Material.CACTUS) {
											block4.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block4, block4Type);
										
										Block block5 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()-1, block.getLocation().getY() + 2, block.getLocation().getZ() + 1));
										Material block5Type = block5.getType();
										if(block5Type == Material.SMOOTH_BRICK) {
											block5Type = Material.AIR;
										}
										if(block5Type != Material.CACTUS) {
											block5.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block5, block5Type);
										
										Block block6 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() + 1, block.getLocation().getY() + 2, block.getLocation().getZ() - 1));
										Material block6Type = block6.getType();
										if(block6Type == Material.SMOOTH_BRICK) {
											block6Type = Material.AIR;
										}
										
										if(block6Type != Material.CACTUS) {
											block6.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block6, block6Type);
										
										Block block7 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block7Type = block7.getType();
										if(block7Type == Material.SMOOTH_BRICK) {
											block7Type = Material.AIR;
										}
										if(block7Type != Material.CACTUS) {
											block7.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block7, block7Type);
										
										Block block8 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() - 1, block.getLocation().getY() + 3, block.getLocation().getZ() + 1));
										Material block8Type = block8.getType();
										if(block8Type == Material.SMOOTH_BRICK) {
											block8Type = Material.AIR;
										}
										if(block8Type != Material.CACTUS) {
											block8.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block8, block8Type);
										
										Block block9 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() + 1, block.getLocation().getY() + 3, block.getLocation().getZ() - 1));
										Material block9Type = block9.getType();
										if(block9Type == Material.SMOOTH_BRICK) {
											block9Type = Material.AIR;
										}
										if(block9Type != Material.CACTUS) {
											block9.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block9, block9Type);
									}
									
									if(getDirection(player).equalsIgnoreCase("E") || getDirection(player).equalsIgnoreCase("W")) {
										Block block1 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block1Type = block1.getType();
										if(block1Type == Material.SMOOTH_BRICK) {
											block1Type = Material.AIR;
										}
										if(block1Type != Material.CACTUS) {
											block1.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block1, block1Type);
										
										Block block2 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()-1, block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block2Type = block2.getType();
										if(block2Type == Material.SMOOTH_BRICK) {
											block2Type = Material.AIR;
										}
										if(block2Type != Material.CACTUS) {
											block2.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block2, block2Type);
										
										Block block3 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()+1, block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block3Type = block3.getType();
										if(block3Type == Material.SMOOTH_BRICK) {
											block3Type = Material.AIR;
										}
										if(block3Type != Material.CACTUS) {
											block3.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block3, block3Type);
										
										Block block4 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block4Type = block4.getType();
										if(block4Type == Material.SMOOTH_BRICK) {
											block4Type = Material.AIR;
										}
										if(block4Type != Material.CACTUS) {
											block4.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block4, block4Type);
										
										Block block5 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()-1, block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block5Type = block5.getType();
										if(block5Type == Material.SMOOTH_BRICK) {
											block5Type = Material.AIR;
										}
										if(block5Type != Material.CACTUS) {
											block5.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block5, block5Type);
										
										Block block6 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() + 1, block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block6Type = block6.getType();
										if(block6Type == Material.SMOOTH_BRICK) {
											block6Type = Material.AIR;
										}
										if(block6Type != Material.CACTUS) {
											block6.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block6, block6Type);
										
										Block block7 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block7Type = block7.getType();
										if(block7Type == Material.SMOOTH_BRICK) {
											block7Type = Material.AIR;
										}
										if(block7Type != Material.CACTUS) {
											block7.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block7, block7Type);
										
										Block block8 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() - 1, block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block8Type = block8.getType();
										if(block8Type == Material.SMOOTH_BRICK) {
											block8Type = Material.AIR;
										}
										if(block8Type != Material.CACTUS) {
											block8.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block8, block8Type);
										
										Block block9 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() + 1, block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block9Type = block9.getType();
										if(block9Type == Material.SMOOTH_BRICK) {
											block9Type = Material.AIR;
										}
										if(block9Type != Material.CACTUS) {
											block9.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block9, block9Type);
									}
									
									if(getDirection(player).equalsIgnoreCase("NW") || getDirection(player).equalsIgnoreCase("SE")) {
										Block block1 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()));
										Material block1Type = block1.getType();
										if(block1Type == Material.SMOOTH_BRICK) {
											block1Type = Material.AIR;
										}
										if(block1Type != Material.CACTUS) {
											block1.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block1, block1Type);
										
										Block block2 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()+1, block.getLocation().getY() + 1, block.getLocation().getZ() + 1));
										Material block2Type = block2.getType();
										if(block2Type == Material.SMOOTH_BRICK) {
											block2Type = Material.AIR;
										}
										if(block2Type != Material.CACTUS) {
											block2.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block2, block2Type);
										
										Block block3 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()-1, block.getLocation().getY() + 1, block.getLocation().getZ()-1));
										Material block3Type = block3.getType();
										if(block3Type == Material.SMOOTH_BRICK) {
											block3Type = Material.AIR;
										}
										if(block3Type != Material.CACTUS) {
											block3.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block3, block3Type);
										
										Block block4 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ()));
										Material block4Type = block4.getType();
										if(block4Type == Material.SMOOTH_BRICK) {
											block4Type = Material.AIR;
										}
										if(block4Type != Material.CACTUS) {
											block4.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block4, block4Type);
										
										Block block5 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX()+1, block.getLocation().getY() + 2, block.getLocation().getZ() + 1));
										Material block5Type = block5.getType();
										if(block5Type == Material.SMOOTH_BRICK) {
											block5Type = Material.AIR;
										}
										if(block5Type != Material.CACTUS) {
											block5.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block5, block5Type);
										
										Block block6 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() - 1, block.getLocation().getY() + 2, block.getLocation().getZ()-1));
										Material block6Type = block6.getType();
										if(block6Type == Material.SMOOTH_BRICK) {
											block6Type = Material.AIR;
										}
										if(block6Type != Material.CACTUS) {
											block6.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block6, block6Type);
										
										Block block7 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX(), block.getLocation().getY() + 3, block.getLocation().getZ()));
										Material block7Type = block7.getType();
										if(block7Type == Material.SMOOTH_BRICK) {
											block7Type = Material.AIR;
										}
										if(block7Type != Material.CACTUS) {
											block7.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block7, block7Type);
										
										Block block8 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() + 1, block.getLocation().getY() + 3, block.getLocation().getZ() +1));
										Material block8Type = block8.getType();
										if(block8Type == Material.SMOOTH_BRICK) {
											block8Type = Material.AIR;
										}
										if(block8Type != Material.CACTUS) {
											block8.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block8, block8Type);
										
										Block block9 = cactice.getBlockAt(new Location(cactice, block.getLocation().getX() - 1, block.getLocation().getY() + 3, block.getLocation().getZ() - 1));
										Material block9Type = block9.getType();
										if(block9Type == Material.SMOOTH_BRICK) {
											block9Type = Material.AIR;
										}
										if(block9Type != Material.CACTUS) {
											block9.setType(Material.SMOOTH_BRICK);
										}
										resetBlock(block9, block9Type);
									}
								} else {
									player.sendMessage(ChatColor.RED + "You can't use Waller here!");
								}
								
							}
						}
					}
				}
			}
			if(player.getItemInHand().getType() != Material.AIR) {
				if(player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasDisplayName()) {
					if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Hub")) {
						plugin.getServer().dispatchCommand(player, "hub");
					}
				}
			}
		}
	}
	
	public void resetBlock(final Block block, final Material material) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				block.setType(material);
			}
		}, 60);
	}
	
	int wallerId;
	
	public void startWallerCooldown(final Player player) {
		wallerCooldownComplete.put(player.getUniqueId(), false);
		wallerId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				wallerCooldownComplete.put(player.getUniqueId(), true);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY +  "Waller cooldown complete!");
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			}
		}, 20*10);
	}
	
	public void chooseWaller(Player player) {
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".kits.cactice.waller.has")) {
			kit.put(player.getUniqueId(), "WALLER");
			player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Equipped Waller kit.");
			wallerCooldownComplete.put(player.getUniqueId(), true);
		} else {
			if(plugin.accounts.getCoins(player) >= 1000) {
				plugin.accounts.removeCoins(player, 1000);
				plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".kits.cactice.waller.has", true);
				try {
					plugin.accounts.getConfig().save(plugin.accounts.file);
				} catch(Exception exception) {
					exception.printStackTrace();
				}
				kit.put(player.getUniqueId(), "WALLER");
				player.sendMessage(ChatColor.YELLOW + "You purchased Waller for 1000 coins. You now have " + Integer.toString(plugin.accounts.getCoins(player)) + " coins left.");
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Equipped Waller kit.");
			} else {
				player.sendMessage(ChatColor.RED + "You don't have enough coins to purchase Waller!");
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
	
			event.setCancelled(true);
			
		}
	}
	
	
	
	
	/// SCOREBOARD
	/// SCOREBOARD
	/// SCOREBOARD
	
	
	
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

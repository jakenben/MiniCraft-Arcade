package me.jake861.BenServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class TowerDefense implements Listener {

	
	////////////////////////////////
	////////////////////////////////
	//      STORED MAP SPAWNS     //
	////////////////////////////////
	////////////////////////////////	
	
	public World towerDefense = Bukkit.getWorld("TowerDefense");
	public World towerDefense2 = Bukkit.getWorld("TowerDefense2");
	
	public Location lobbySpawn;
	
	public Location[] map1InvaderSpawns = new Location[12];
	public Location[] map1DefenderSpawns = new Location[12];
	public Location map1WoolBlock;
	public Location map1SpectatorSpawn;
	
	public Location[] map2InvaderSpawns = new Location[12];
	public Location[] map2DefenderSpawns = new Location[12];
	public Location map2WoolBlock;
	public Location map2SpectatorSpawn;
	
	////////////////////////////////
	////////////////////////////////
	//     CURRENT MAP SPAWNS     //
	////////////////////////////////
	////////////////////////////////
	

	public Location spectatorSpawn;
	public Location woolBlock;
	public Location[] invaderSpawns = new Location[6]; 
	public Location[] defenderSpawns = new Location[6];
	
	
	private Main plugin;
	
	public TowerDefense(Main plugin) {
		this.plugin = plugin;
		
		if(towerDefense == null) {
			WorldCreator wc = new WorldCreator("TowerDefense");
			towerDefense = Bukkit.createWorld(wc);
		}
		
		if(towerDefense2 == null) {
			WorldCreator wc = new WorldCreator("TowerDefense2");
			towerDefense2 = Bukkit.createWorld(wc);
		}
		
		lobbySpawn = new Location(towerDefense, 194.5, 90.5, 131.5);
		
		map1SpectatorSpawn = new Location(towerDefense, -14, 101, 10);
		map1WoolBlock = new Location(towerDefense, -7.5, 93, 27.5);
		
		map1InvaderSpawns[0] = new Location(towerDefense, -3, 71, -25);
		map1InvaderSpawns[1] = new Location(towerDefense, -4, 70, -25);
		map1InvaderSpawns[2] = new Location(towerDefense, -5, 70, -25);
		map1InvaderSpawns[3] = new Location(towerDefense, -6, 70, -25);
		map1InvaderSpawns[4] = new Location(towerDefense, -7, 70, -25);
		map1InvaderSpawns[5] = new Location(towerDefense, -8, 70, -25);
		
		map1DefenderSpawns[0] = new Location(towerDefense, -10, 84, 27);
		map1DefenderSpawns[1] = new Location(towerDefense, -9, 84, 27);
		map1DefenderSpawns[2] = new Location(towerDefense, -8, 84, 27);
		map1DefenderSpawns[3] = new Location(towerDefense, -7, 84, 27);
		map1DefenderSpawns[4] = new Location(towerDefense, -6, 84, 27);
		map1DefenderSpawns[5] = new Location(towerDefense, -5, 84, 27);
		
		
		
		map2SpectatorSpawn = new Location(towerDefense2, -25, 85, -9);
		map2WoolBlock = new Location(towerDefense2, -115, 75, -11);
		
		map2InvaderSpawns[0] = new Location(towerDefense2, -1, 69, -5, 90, 0);
		map2InvaderSpawns[1] = new Location(towerDefense2, -1, 69, -6, 90, 0);
		map2InvaderSpawns[2] = new Location(towerDefense2, -1, 69, -7, 90, 0);
		map2InvaderSpawns[3] = new Location(towerDefense2, -1, 69, -8, 90, 0);
		map2InvaderSpawns[4] = new Location(towerDefense2, -1, 69, -9, 90, 0);
		map2InvaderSpawns[5] = new Location(towerDefense2, -1, 69, -10, 90, 0);
		
		map2DefenderSpawns[0] = new Location(towerDefense2, -120, 78, -14, -90, 0);
		map2DefenderSpawns[1] = new Location(towerDefense2, -120, 78, -13, -90, 0);
		map2DefenderSpawns[2] = new Location(towerDefense2, -120, 78, -12, -90, 0);
		map2DefenderSpawns[3] = new Location(towerDefense2, -120, 78, -11, -90, 0);
		map2DefenderSpawns[4] = new Location(towerDefense2, -120, 78, -10, -90, 0);
		map2DefenderSpawns[5] = new Location(towerDefense2, -120, 78, -9, -90, 0);
	}

	public boolean respawn = true;
	
	public List<UUID> players = new ArrayList<>();
	public List<UUID> tempDeads = new ArrayList<>();
	public List<UUID> spectators = new ArrayList<>();
	public List<UUID> invaders = new ArrayList<>();
	public List<UUID> defenders = new ArrayList<>();
	public List<UUID> deadDefenders = new ArrayList<>();
	
	public int MAX_PLAYERS = 40;
	
	public Map<UUID, Integer> kills = new HashMap<>();
	
	Player blockBreaker;
	
	
	int invaderNum = 0;
	int defenderNum = 0;
	public String lastSelection = "DEFENDERS";
	
	public String status = "WAITING";
	
	public String currentMap = "Jungle";
	
	
	
	public void addPlayer(UUID playerUuid) {
		Player player = Bukkit.getPlayer(playerUuid);
		kills.put(player.getUniqueId(), 0);	
		players.add(playerUuid);
		plugin.scoreboards.updateScoreboard();
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
		
		if(status == "COUNTING") {
			player.teleport(lobbySpawn);
			
			if(lastSelection == "INVADERS") {
				defenders.add(playerUuid);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "You joined the " + ChatColor.AQUA + "Defenders " + ChatColor.YELLOW + "team!");
				lastSelection = "DEFENDERS";
			} else {
				invaders.add(playerUuid);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "You joined the " + ChatColor.RED + "Invaders " + ChatColor.YELLOW + "team!");
				
				lastSelection = "INVADERS";
			}
		}
		
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
		}
		
		if(status != "WAITING" && status != "COUNTING") {
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
		
		if(invaders.contains(playerUuid)) {
			invaders.remove(playerUuid);
		}
		
		if(defenders.contains(playerUuid)) {
			defenders.remove(playerUuid);
		}
		
		if(tempDeads.contains(playerUuid)) {
			tempDeads.remove(playerUuid);
		}
		
		if(deadDefenders.contains(playerUuid)) {
			deadDefenders.remove(playerUuid);
		}
		
		
		if(players.size() - spectators.size() <= 1) {
			
			
			
				if(status == "WAITING" || status == "COUNTING") {
					Bukkit.getScheduler().cancelTask(lobbyId);
					status = "WAITING";
					updateScoreboard();
				}
				
				if(status == "STARTING") {
					if(defenders.size() <= 0) {
						Bukkit.getScheduler().cancelTask(startId);
						endGame("Invaders");
					} else if(invaders.size() <= 0) {
						Bukkit.getScheduler().cancelTask(startId);
						endGame("Defenders");
					} else {
						Bukkit.getScheduler().cancelTask(startId);
						endGame("Nobody");
					}
				}
				
				if(status == "STARTED") {
					if(defenders.size() <= 0) {
						endGame("Invaders");
					} else if(invaders.size() <= 0) {
						endGame("Defenders");
					} else {
						endGame("Nobody");
					}
				}
			
		} else {
			if(status == "STARTED" || status == "STARTING") {
				
				if(defenders.size() <= 0) {
					endGame("Invaders");
				} else if(invaders.size() <= 0) {
					endGame("Defenders");
				}
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
		   List<Entity> entList = towerDefense.getEntities();
		   
		   for(Entity ent : entList) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
		   
		   List<Entity> entList2 = towerDefense2.getEntities();
		   
		   for(Entity ent : entList2) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
		   
		   updateScoreboard();
	}

	

	
	public void equipInvader(Player player) {
		ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.spigot().setUnbreakable(true);
		helmet.setItemMeta(helmetMeta);
		player.getInventory().setHelmet(helmet);
		
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemMeta chestplateMeta = chestplate.getItemMeta();
		chestplateMeta.spigot().setUnbreakable(true);
		chestplate.setItemMeta(chestplateMeta);
		player.getInventory().setChestplate(chestplate);
		
		ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta leggingsMeta = leggings.getItemMeta();
		leggingsMeta.spigot().setUnbreakable(true);
		leggings.setItemMeta(leggingsMeta);
		player.getInventory().setLeggings(leggings);
		
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
		ItemMeta bootsMeta = boots.getItemMeta();
		bootsMeta.spigot().setUnbreakable(true);
		boots.setItemMeta(bootsMeta);
		player.getInventory().setBoots(boots);
		
		
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.spigot().setUnbreakable(true);
		sword.setItemMeta(swordMeta);
		player.getInventory().addItem(sword);
		
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemMeta rodMeta = rod.getItemMeta();
		rodMeta.spigot().setUnbreakable(true);
		rod.setItemMeta(rodMeta);
		player.getInventory().addItem(rod);
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.spigot().setUnbreakable(true);
		bow.setItemMeta(bowMeta);
		player.getInventory().addItem(bow);
		
		
		player.getInventory().addItem(new ItemStack(Material.ARROW, 5));
	}
	
	public void equipDefender(Player player) {
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.spigot().setUnbreakable(true);
		helmet.setItemMeta(helmetMeta);
		player.getInventory().setHelmet(helmet);
		
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta chestplateMeta = chestplate.getItemMeta();
		chestplateMeta.spigot().setUnbreakable(true);
		chestplate.setItemMeta(chestplateMeta);
		player.getInventory().setChestplate(chestplate);
		
		ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		ItemMeta leggingsMeta = leggings.getItemMeta();
		leggingsMeta.spigot().setUnbreakable(true);
		leggings.setItemMeta(leggingsMeta);
		player.getInventory().setLeggings(leggings);
		
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		ItemMeta bootsMeta = boots.getItemMeta();
		bootsMeta.spigot().setUnbreakable(true);
		boots.setItemMeta(bootsMeta);
		player.getInventory().setBoots(boots);
		
		
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.spigot().setUnbreakable(true);
		sword.setItemMeta(swordMeta);
		player.getInventory().addItem(sword);
		
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemMeta rodMeta = rod.getItemMeta();
		rodMeta.spigot().setUnbreakable(true);
		rod.setItemMeta(rodMeta);
		player.getInventory().addItem(rod);
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.spigot().setUnbreakable(true);
		bow.setItemMeta(bowMeta);
		player.getInventory().addItem(bow);
		
		player.getInventory().addItem(new ItemStack(Material.ARROW, 10));
	}
	
	
	
	int lobbyId;
	int lobbyCount;
	
	public void startLobbyCountdown() {
		
		status = "COUNTING";
		
		chooseMap();
		
		
		
		Collections.shuffle(players);
		
		for(UUID playerUuid : players) {
		
			Player player = Bukkit.getPlayer(playerUuid);
		
			if(lastSelection == "INVADERS") {
				defenders.add(playerUuid);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "You joined the " + ChatColor.AQUA + "Defenders " + ChatColor.YELLOW + "team!");
				lastSelection = "DEFENDERS";
			} else {
				invaders.add(playerUuid);
				player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "You joined the " + ChatColor.RED + "Invaders " + ChatColor.YELLOW + "team!");
				
				lastSelection = "INVADERS";
			}
		}
		
		
		lobbyCount = 21;
		
		chooseMap();
		
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + currentMap + ChatColor.GRAY + " was selected as the map!");
			kills.put(playerUuid, 0);
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
					plugin.getServer().getScheduler().cancelTask(lobbyId);
					
				}
				
			}
		}, 0, 20);
	}
	
	
	int startId;
	int startCount;
	
	@SuppressWarnings("deprecation")
	public void startGame() {
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			
			if(invaders.contains(playerUuid)) {
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.YELLOW + "  " + ChatColor.BOLD + "Tower Defense");
				player.sendMessage("  " + ChatColor.RED + "Invaders Team");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.GRAY + "  Destroy the wool to prevent the defenders ");
				player.sendMessage(ChatColor.GRAY + "  from respawning and then kill them off!");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.teleport(invaderSpawns[invaderNum]);
				invaderNum++;
				if(invaderNum >= (MAX_PLAYERS / 2)) {
					invaderNum = 0;
				}
			} else if(defenders.contains(playerUuid)) {
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.YELLOW + "  " + ChatColor.BOLD + "Tower Defense");
				player.sendMessage("  " + ChatColor.AQUA + "Defenders Team");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.GRAY + "  Prevent the invaders from breaking the wool ");
				player.sendMessage(ChatColor.GRAY + "  block and stay alive!");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.AQUA + "=====================================");
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
				player.teleport(defenderSpawns[defenderNum]);
				defenderNum++;
				if(defenderNum >= (MAX_PLAYERS / 2)) {
					defenderNum = 0;
				}
			}
		}
		
		
		status = "STARTING";
		startCount = 6;
		
		updateScoreboard();
		
		gameCount = 60*5;
		woolBlock.getBlock().setType(Material.WOOL);
		woolBlock.getBlock().setData(DyeColor.LIGHT_BLUE.getData());
		
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
			currentMap = "Jungle";
			setMap1();
		} else {
			currentMap = "Graveyard";
			setMap2();
		}
	}
	
	public void start() {
		gameTimer();
		status = "STARTED";
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1, 0);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			if(defenders.contains(playerUuid)) {
				equipDefender(player);
				startArrowTimer(player);
			} else if (invaders.contains(playerUuid)) {
				equipInvader(player);
			}
			
		}
		
		updateScoreboard();
	}
	
	int gameId;
	int gameCount = 60*5;
	
	public void gameTimer() {
		gameCount = 60 * 5 + 1;
		if(status != "STARTED") {
		gameId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				
				gameCount--;
				updateScoreboard();
				if(gameCount <= 0) {
					endGame("Defenders");
				} else {
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						if(gameCount % 60 == 0) {
							player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "Invaders have " + Integer.toString(gameCount / 60) + " minutes!");
						}
					}
				}
			}
		}, 0, 20);
		}
	}


	
	@EventHandler 
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(spectators.contains(event.getPlayer().getUniqueId()) || tempDeads.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
		
		Player player = event.getPlayer();
		if(players.contains(player.getUniqueId())) {
			if(player.getItemInHand().getType() != Material.AIR) {
				if(player.getItemInHand().hasItemMeta()) {
					if(player.getItemInHand().getItemMeta().hasDisplayName()) {
						if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Hub")) {
							plugin.getServer().dispatchCommand(player, "hub");
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player.getUniqueId())) {
			if(!spectators.contains(player.getUniqueId()) && !tempDeads.contains(player.getUniqueId())) {
				if(invaders.contains(player.getUniqueId())) {
					if(event.getBlock().getType() == Material.WOOL) {
						blockBreaker = player;
						for(UUID playerUuid : players) {
							Player pl = Bukkit.getPlayer(playerUuid);
							pl.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "The wool has been broken! Defenders no longer respawn.");
							respawn = false;
							List<Entity> entList = towerDefense.getEntities();
							   
							   for(Entity ent : entList) {
								   if(ent instanceof Item) {
									   ent.remove();
								   }
							   }
							   
							   List<Entity> entList2 = towerDefense2.getEntities();
							   
							   for(Entity ent : entList2) {
								   if(ent instanceof Item) {
									   ent.remove();
								   }
							   }
						}
					} else {
						event.setCancelled(true);
					}
				} else {
					event.setCancelled(true);
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		if(players.contains(player.getUniqueId())) {
			event.getDrops().clear();
			if(defenders.contains(player.getUniqueId())) {
				if(respawn) {
					for(Player pl : Bukkit.getOnlinePlayers()) {
						pl.hidePlayer(player);
					}
					tempDeads.add(player.getUniqueId());
					player.setHealth(20);
					player.setFoodLevel(20);
					player.setAllowFlight(true);
					player.setFlying(true);
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.teleport(spectatorSpawn);
					if(player.getKiller() != null) {
						player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "You were killed by " + ChatColor.YELLOW + player.getKiller().getName() + ChatColor.GRAY + ".");
						player.getKiller().sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "You killed " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
						
					}
					player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "RESPAWNING IN 3 SECONDS");
					startRespawnTimer(player);
				} else {
					
					for(UUID playerUuid : players) {
						Player pl = Bukkit.getPlayer(playerUuid);
						pl.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + player.getName() + " died.");
	
					}
					
					deadDefenders.add(player.getUniqueId());
					addSpectator(player.getUniqueId());
					
					if(deadDefenders.size() >= defenders.size()) {
						endGame("Invaders");
					}
				}
			} else {
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.hidePlayer(player);
				}
				player.setHealth(20);
				player.setFoodLevel(20);
				tempDeads.add(player.getUniqueId());
				player.setAllowFlight(true);
				player.setFlying(true);
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.teleport(spectatorSpawn);
				if(player.getKiller() != null) {
					player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "You were killed by " + ChatColor.YELLOW + player.getKiller().getName() + ChatColor.GRAY + ".");
					player.getKiller().sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "You killed " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
				}
				player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "RESPAWNING IN 3 SECONDS");
				startRespawnTimer(player);
				
			}
			event.setDeathMessage("");
			   List<Entity> entList = towerDefense.getEntities();
			   
			   for(Entity ent : entList) {
				   if(ent instanceof Item) {
					   ent.remove();
				   }
			   }
			   
			   List<Entity> entList2 = towerDefense2.getEntities();
			   
			   for(Entity ent : entList2) {
				   if(ent instanceof Item) {
					   ent.remove();
				   }
			   }
			   player.playSound(player.getLocation(), Sound.VILLAGER_HIT, 1, 0);
			  
			   player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 0);
			   if(player.getKiller() != null) {
				   player.getKiller().playSound(player.getKiller().getLocation(), Sound.VILLAGER_HIT, 1, 0);
				   player.getKiller().playSound(player.getKiller().getLocation(), Sound.WITHER_DEATH, 1, 0);
				   player.getKiller().playSound(player.getKiller().getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			   }
			   
			   respawn(player);
			   
			   if(player.getKiller() != null) {
				   kills.put(player.getKiller().getUniqueId(), kills.get(player.getKiller().getUniqueId()) + 1);
			   }
		}
		
		updateScoreboard();
	}
	
	public void respawn(final Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				player.spigot().respawn();
			}
		}, 1);
	}
	
	
	public Map<Player, Integer> respawnId = new HashMap<>();
	public Map<Player, Integer> respawnCount = new HashMap<>();
	
	public void startRespawnTimer(final Player player) {
		
		respawnCount.put(player, 4);
		
		respawnId.put(player, plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				respawnCount.put(player, respawnCount.get(player)-1);
				if(respawnCount.get(player) <= 0) {
					if(defenders.contains(player.getUniqueId())) {
						equipDefender(player);
						player.setHealth(20);
						player.setFoodLevel(20);
						player.teleport(defenderSpawns[0]);
						player.setFlying(false);
						player.setAllowFlight(false);
						
						
						tempDeads.remove(player.getUniqueId());
						for(Player pl : Bukkit.getOnlinePlayers()) {
							pl.showPlayer(player);
						}
					} else {
						equipInvader(player);
						player.setHealth(20);
						player.setFoodLevel(20);
						player.teleport(invaderSpawns[0]);
						player.setFlying(false);
						player.setAllowFlight(false);
						
						tempDeads.remove(player.getUniqueId());
						for(Player pl : Bukkit.getOnlinePlayers()) {
							pl.showPlayer(player);
						}
					}
					
					plugin.getServer().getScheduler().cancelTask(respawnId.get(player));
				}
			}
			
		}, 0, 20));
		
	}
	
	
	public Map<Player, Integer> arrowId = new HashMap<>();
	public Map<Player, Integer> arrowCount = new HashMap<>();
	
	public void startArrowTimer(final Player player) {
		
		arrowCount.put(player, 8);
		
		arrowId.put(player, plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				arrowCount.put(player, arrowCount.get(player)-1);
				if(arrowCount.get(player) <= 0) {
					if(defenders.contains(player.getUniqueId())) {
						if(!spectators.contains(player.getUniqueId()) && !tempDeads.contains(player.getUniqueId())) {
							boolean found = false;
							for(ItemStack items : player.getInventory().getContents()) {
								if(items != null) {
									if(items.getType() != null) {
										if(items.getType() == Material.ARROW) {
											found = true;
											if(items.getAmount() < 10) {
												player.getInventory().addItem(new ItemStack(Material.ARROW));
												player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 3);
												
											}
										}
									}
								}
							}
							
							if(!found) {
								player.getInventory().addItem(new ItemStack(Material.ARROW));
								player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 3);
							}
							
						} 
						
					}
					plugin.getServer().getScheduler().cancelTask(arrowId.get(player));
					startArrowTimer(player);
					
				}
			}
			
		}, 0, 20));
		
	}
	
	int endId;
	int endCount;
	
	public void endGame(String winner) {
		status = "ENDED";
		   List<Entity> entList = towerDefense.getEntities();
		   
		   for(Entity ent : entList) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
		   List<Entity> entList2 = towerDefense2.getEntities();
		   
		   for(Entity ent : entList2) {
			   if(ent instanceof Item) {
				   ent.remove();
			   }
		   }
		plugin.getServer().getScheduler().cancelTask(gameId);
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setHealth(20);
			player.setFoodLevel(20);
			
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
				player.sendMessage("  " + winner + " won the game!" );
				player.sendMessage(" ");
				player.sendMessage(ChatColor.AQUA + "=====================================");
				
				if(defenders.contains(playerUuid)) {
					Player pl = Bukkit.getPlayer(playerUuid);
					if(arrowId.get(pl) != null) {
						plugin.getServer().getScheduler().cancelTask(arrowId.get(pl));
					}
				}
			}
		}
		
		giveCoins(winner);
		
		endCount = 9;
		
		updateScoreboard();
		
		endId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				endCount--;
				if(endCount <= 0) {
					
					respawn = true;
					invaderNum = 0;
					defenderNum = 0;
					
					status = "WAITING";
					for(UUID playerUuid : players) {
						Player player = Bukkit.getPlayer(playerUuid);
						player.setFlying(false);
						player.setAllowFlight(false);
						for(Player p : Bukkit.getOnlinePlayers()) {
						
							p.showPlayer(player);
						}
					}

					removeAll();
					
					
					plugin.getServer().getScheduler().cancelTask(endId);
					
				}
			}
			
		}, 0, 20);
	}
	
	public void giveCoins(final String winner) {
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
					if(winner == "Invaders") {
						if(invaders.contains(player.getUniqueId())) {
							player.sendMessage(ChatColor.WHITE + "  Winner: " + ChatColor.YELLOW + "20");
							plugin.accounts.addCoins(player, 20);
						}
					}
					if(winner == "Defenders") {
						if(defenders.contains(player.getUniqueId())) {
							player.sendMessage(ChatColor.WHITE + "  Winner: " + ChatColor.YELLOW + "20");
							plugin.accounts.addCoins(player, 20);
						}
					}
					if(kills.get(playerUuid) != 0) {
						player.sendMessage(ChatColor.WHITE + "  Kills: " + ChatColor.YELLOW + Integer.toString(kills.get(playerUuid) *5));
						plugin.accounts.addCoins(player, (kills.get(playerUuid) * 5));
					}
					if(player == blockBreaker) {
						player.sendMessage(ChatColor.WHITE + "  Wool Breaker: " + ChatColor.YELLOW + "10");
						plugin.accounts.addCoins(player, 10);
					}
					
					player.sendMessage(" ");
					player.sendMessage(ChatColor.YELLOW + "  You currently have " + Integer.toString(plugin.accounts.getCoins(player)) + " coins!");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.AQUA + "=====================================");
					
				}
			}
		}, 80);
	}
	
	public void setMap1() {
		for(int i = 0; i < 6; i++) {
			invaderSpawns[i] = map1InvaderSpawns[i];
			defenderSpawns[i] = map1DefenderSpawns[i];
		}
		spectatorSpawn = map1SpectatorSpawn;
		woolBlock = map1WoolBlock;
	}
	
	public void setMap2() {
		for(int i = 0; i < 6; i++) {
			invaderSpawns[i] = map2InvaderSpawns[i];
			defenderSpawns[i] = map2DefenderSpawns[i];
		}
		spectatorSpawn = map2SpectatorSpawn;
		woolBlock = map2WoolBlock;
	}
	
	
	public void removeAll() {
		
		blockBreaker = null;
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = kills.entrySet().iterator();
		while(iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<UUID, Integer> entry = (Map.Entry<UUID, Integer>) iterator.next();
			entry.setValue(0);
			iterator.remove();
		}
	
		

		
		@SuppressWarnings("rawtypes")
		Iterator i = spectators.iterator();
		
		while(i.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) i.next();
			i.remove();
		}

		@SuppressWarnings("rawtypes")
		Iterator ite = invaders.iterator();
		
		while(ite.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) ite.next();
			ite.remove();
		}
		

		@SuppressWarnings("rawtypes")
		Iterator iter = defenders.iterator();
		
		while(iter.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) iter.next();
			iter.remove();
		}
		
		@SuppressWarnings("rawtypes")
		Iterator itera = deadDefenders.iterator();
		
		while(itera.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) itera.next();
			itera.remove();
		}
		
		@SuppressWarnings("rawtypes")
		Iterator iterat = tempDeads.iterator();
		
		while(iterat.hasNext()) {
			@SuppressWarnings("unused")
			UUID pU = (UUID) iterat.next();
			iterat.remove();
		}
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.setFlying(false);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(player);
			}
			
			
			player.setAllowFlight(false);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			
			
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setGameMode(GameMode.SURVIVAL);
			((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
			
			player.teleport(lobbySpawn);
			ItemStack clock = new ItemStack(Material.WATCH);
			ItemMeta clockMeta = clock.getItemMeta();
			clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
			clock.setItemMeta(clockMeta);
			player.getInventory().setItem(8, clock);
		}
		
		status = "WAITING";
		
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
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(players.contains(player.getUniqueId())) {
				if(status != "STARTED") {
					event.setCancelled(true);
				} else {
					if(spectators.contains(player.getUniqueId()) || tempDeads.contains(player.getUniqueId())) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(players.contains(((Player) event.getWhoClicked()).getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if(players.contains(player.getUniqueId()) || players.contains(damager.getUniqueId())) {
				if(status != "STARTED") {
					event.setCancelled(true);
				} else {
					if(spectators.contains(player.getUniqueId()) || tempDeads.contains(player.getUniqueId()) || spectators.contains(damager.getUniqueId()) ||  tempDeads.contains(damager.getUniqueId())) {
						event.setCancelled(true);
					}
					if(invaders.contains(player.getUniqueId()) && invaders.contains(damager.getUniqueId())) {
						event.setCancelled(true);
					}
					if(defenders.contains(player.getUniqueId()) && defenders.contains(damager.getUniqueId())) {
						event.setCancelled(true);
					}
					
				}
			}
			
			
		} else if (event.getDamager() instanceof Projectile) {
			if(event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				Projectile proj = (Projectile) event.getDamager();
				if(proj instanceof Arrow) {
					Arrow arrow = (Arrow) proj;
					if(players.contains(player.getUniqueId())) {
						if(arrow.getShooter() instanceof Player) {
							Player pl = (Player) arrow.getShooter();
							if(defenders.contains(player.getUniqueId()) && defenders.contains(pl.getUniqueId())) {
								event.setCancelled(true);
							}
							if(invaders.contains(player.getUniqueId()) && invaders.contains(pl.getUniqueId())) {
								event.setCancelled(true);
							}
							((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
							removeArrows(player);
						}
					}
				}
			}
		}
	}
	
	public void removeArrows(final Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
			}
		}, 1);
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(status == "STARTING") {
			if(players.contains(event.getPlayer().getUniqueId())) {
				if (((event.getTo().getX() != event.getFrom().getX()) || (event.getTo().getZ() != event.getFrom().getZ()))) {
		            event.setTo(event.getFrom());
		            return;
		        }
			}
		}
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(status == "STARTED") {
				if(event.getPlayer().getLocation().getY() <= 35 && !spectators.contains(event.getPlayer().getUniqueId()) && !tempDeads.contains(event.getPlayer().getUniqueId())) {
					event.getPlayer().setHealth(0);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(players.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
	
			event.setCancelled(true);
			
		}
	}
	
	
	
	
	
	
	
	
	public String getTime() {
		int remainder = gameCount % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		String secs = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = Integer.toString(minutes) + ":" + secs;
		return formattedTime;
	}
	
	
	
	public String getWoolBroken() {
		if(!respawn) {
			return ChatColor.RED +  "Wool Broken!";
		} else {
			return ChatColor.AQUA + "Wool Alive";
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
		obj = scoreboard.registerNewObjective("Tower Defense", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(status == "STARTING" || status == "STARTED" || status == "ENDED") {
		
			obj.setDisplayName(ChatColor.AQUA + "   " + ChatColor.BOLD + "Mini" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Craft" + ChatColor.RESET + "   ");
		
			
			obj.getScore(ChatColor.WHITE.toString()).setScore(15);
			obj.getScore(ChatColor.BLUE + "Remaining Time: " + ChatColor.WHITE + getTime()).setScore(14);
			obj.getScore(getWoolBroken()).setScore(13);
			obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString() + ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(12);
			
			if(invaders.size() + defenders.size() < 11) {
			
				int counter = 11;
				
				for(UUID uuid : invaders) {
					Player player = Bukkit.getPlayer(uuid);
					
					obj.getScore(ChatColor.RED + player.getName()).setScore(counter);
					
					counter--;
				}
				
				obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(counter);
				
				counter--;
				
				for(UUID uuid : defenders) {
					Player player = Bukkit.getPlayer(uuid);
					if(!deadDefenders.contains(uuid)) {
						obj.getScore(ChatColor.AQUA + player.getName()).setScore(counter);
					} else {
						obj.getScore(ChatColor.DARK_GRAY + player.getName()).setScore(counter);
					}
					
					counter--;
				}
			
			} else {
				obj.getScore(ChatColor.RED + "Invaders: " + ChatColor.WHITE + Integer.toString(invaders.size())).setScore(14);
				obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
				obj.getScore(ChatColor.AQUA + "Alive Defenders: " + ChatColor.WHITE + Integer.toString(defenders.size() - deadDefenders.size())).setScore(12);
				obj.getScore(ChatColor.RED + "Dead Defenders: " + ChatColor.WHITE + Integer.toString(deadDefenders.size())).setScore(12);
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

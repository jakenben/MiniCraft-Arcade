package me.jake861.BenServer.PixelBattles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jake861.BenServer.Main;

public class PixelBattles implements Listener {

	private Main plugin;
	
	public Location lobbySpawn;
	
	public PixelBattles(Main plugin) {
		this.plugin = plugin;
		
		lobbySpawn = new Location(Util.getGameWorld(), 23.5, 3.5, -50.5, 0, 0);
		new Gameboard(plugin);
	}
	
	public List<UUID> players = new ArrayList<>();
	public List<Drawing> drawings = new ArrayList<>();
	
	public Map<Player, Drawing> currentDrawing = new HashMap<>();
	public Map<Player, Integer> number = new HashMap<>();
	
	public int MAX_PLAYERS = 12;
	
	
	public String status = "WAITING";
	
	public void addPlayer(UUID uuid) {
		
		if(status == "WAITING" || status == "COUNTING") {	
			Player player = Bukkit.getPlayer(uuid);
			players.add(uuid);
			plugin.scoreboards.updateScoreboard();
			plugin.pixelBattlesGameboard.updateScoreboard();
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
			
			number.put(player, 0);
			
			ItemStack clock = new ItemStack(Material.WATCH);
			ItemMeta clockMeta = clock.getItemMeta();
			clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
			clock.setItemMeta(clockMeta);
			player.getInventory().setItem(8, clock);
			
			
				if(players.size() == 2) {
					startLobbyCountdown();
				} else if (players.size() < 2) {
					player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.GRAY + "Waiting for players to start.");
				}
				player.teleport(lobbySpawn);
		} else {
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game already started!");
		}
		
		
	}
	
	public void removePlayer(UUID uuid) {
		players.remove(uuid);
		Player player = Bukkit.getPlayer(uuid);
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
		
		if(players.size() <= 1) {
		
			if(status == "WAITING") {
				Bukkit.getScheduler().cancelTask(lobbyId);
			}
			
			if(status == "COUNTING") {
				Bukkit.getScheduler().cancelTask(lobbyId);
				status = "WAITING";
				plugin.pixelBattlesGameboard.updateScoreboard();
			}
			
			if(status == "STARTING") {
				Bukkit.getScheduler().cancelTask(startId);
				endGame(null);
			}
			
			if(status == "STARTED") {
				endGame(null);
			}
		}
		
		plugin.pixelBattlesGameboard.updateScoreboard();
		
	}
	
	int lobbyId;
	public int lobbyCount;
	
	public void startLobbyCountdown() {
		lobbyCount = 21;
		status = "COUNTING";
		
		
		lobbyId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				lobbyCount--;
				plugin.pixelBattlesGameboard.updateScoreboard();
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
					
					startGameCountdown();
					plugin.getServer().getScheduler().cancelTask(lobbyId);
					
				}
				
			}
		}, 0, 20);
	}
	
	int startId;
	int startCount;
	
	public void startGameCountdown() {
		drawings.clear();
		Util.getDrawingManager().createDrawings();
		
		Collections.shuffle(Util.getDrawingManager().getDrawings());
		drawings.add(Util.getDrawingManager().getDrawings().get(0));
		drawings.add(Util.getDrawingManager().getDrawings().get(1));
		drawings.add(Util.getDrawingManager().getDrawings().get(2));
		
		
		
		for(UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
		
			Util.getBuildAreaManager().createBuildAreas();
			Util.getBuildAreaManager().assignBuildArea(players);
			player.teleport(Util.getBuildAreaManager().getBuildArea(player).getPlayerSpawn());
			
			for(Location loc : Util.getBuildAreaManager().getBuildArea(player).getViewBlocks()) {
				loc.getBlock().setType(Material.AIR);
			}
			
			for(Location loc : Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks()) {
				loc.getBlock().setType(Material.AIR);
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
			player.sendMessage(ChatColor.YELLOW + " " + ChatColor.BOLD + "Pixel Battles");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + "  Copy the image on the right and ");
			player.sendMessage(ChatColor.GRAY + "  build it on the left! First to 3 wins!");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.AQUA + "=====================================");
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
		}
		
		status = "STARTING";
		startCount = 6;
		
		plugin.pixelBattlesGameboard.updateScoreboard();
		
		startId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				startCount--;
				for(UUID playerUuid : players) {
					Player player = Bukkit.getPlayer(playerUuid);
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 0);
					player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + Integer.toString(startCount));
				}
				
				if(startCount <= 0) {
					startGame();
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
	
	@SuppressWarnings("deprecation")
	public void startGame() {
		for(UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			player.setAllowFlight(true);
			player.setFlying(true);
			for(Location loc : Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks()) {
				loc.getBlock().setType(Material.AIR);
			}
			number.put(player, 0);
			currentDrawing.put(player, drawings.get(0));
			player.sendMessage(ChatColor.BLUE + "[Drawing] " + ChatColor.GRAY + "Drawing #1: " + ChatColor.YELLOW + currentDrawing.get(player).getName());
			BuildArea ba = Util.getBuildAreaManager().getBuildArea(player);
			int count = 0;
			for(Location loc : ba.getViewBlocks()) {
				loc.getBlock().setType(currentDrawing.get(player).getBlocks().get(count).getBlock().getType());
				loc.getBlock().setData(currentDrawing.get(player).getBlocks().get(count).getBlock().getData());
				player.getInventory().addItem(new ItemStack(loc.getBlock().getType(), 1, (short) loc.getBlock().getData()));
				count++;
			}
			
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.pixelBattlesGameboard.updateScoreboard();
			}
		}, 5);
		
	}
	
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		if(players.contains(player.getUniqueId())) {
			if(status == "STARTED") {
				Location loc = event.getBlock().getLocation();
				if(Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks().contains(loc)) {
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							checkDrawing(player);
						}
					});
					
				} else {
					event.setCancelled(true);
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	
	@SuppressWarnings("deprecation")
	public void checkDrawing(Player player) {
		int count = 0;
		int blocks = 0;
		for(Location loc : Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks()) {
			if((loc.getBlock().getType() == currentDrawing.get(player).getBlocks().get(count).getBlock().getType()) &&
			 {
			 	if(!(loc.getBlock().getData() == currentDrawing.get(player).getBlocks().get(count).getBlock().getData())))
			 	return;
			 	
				else blocks++;
			} 
			count++;
			if(blocks >= Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks().size()) {
				nextBuild(player);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void nextBuild(final Player player) {
		
		player.sendMessage(ChatColor.BLUE + "[Game] " + ChatColor.YELLOW + "You completed " + currentDrawing.get(player).getName() + "!");
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
		
		number.put(player, number.get(player) + 1);
		
		if(number.get(player) < 3) {
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
						
						currentDrawing.put(player,  drawings.get(number.get(player)));
						player.sendMessage(ChatColor.BLUE + "[Drawing] " + ChatColor.GRAY + "Drawing #" + Integer.toString(number.get(player) + 1) + ": " + ChatColor.YELLOW + currentDrawing.get(player).getName());
						BuildArea ba = Util.getBuildAreaManager().getBuildArea(player);
						
						int count = 0;
						
						player.getInventory().clear();
						
						for(Location loc : ba.getBuildBlocks()) {
							loc.getBlock().setType(Material.AIR);
						}
						
						for(Location loc : ba.getViewBlocks()) {
							loc.getBlock().setType(currentDrawing.get(player).getBlocks().get(count).getBlock().getType());
							loc.getBlock().setData(currentDrawing.get(player).getBlocks().get(count).getBlock().getData());
							player.getInventory().addItem(new ItemStack(loc.getBlock().getType(), 1, (short) loc.getBlock().getData()));
							count++;
						
						}
						
				}
			}, 20);
		} else {
			endGame(player);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.pixelBattlesGameboard.updateScoreboard();
			}
		}, 5);
	}
	
	int endCount;
	int endId;
	
	public void endGame(Player winner) {
		
		
		status = "ENDED";
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setHealth(20);
			player.setFoodLevel(20);
		}
		
		
		plugin.pixelBattlesGameboard.updateScoreboard();
		
		
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
		
		giveCoins(winner);
		endId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				endCount--;
				
				if(endCount <= 0) {
					
					status = "WAITING";

					removeAll();
					   List<Entity> entList = Util.getGameWorld().getEntities();
					   
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
	
	
	public void giveCoins(final Player winner) {
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
					if(number.get(player) > 0) {
						player.sendMessage(ChatColor.WHITE + "  " + Integer.toString(number.get(player)) + " Drawings: " + ChatColor.YELLOW + number.get(player) * 5);
						plugin.accounts.addCoins(player, number.get(player) * 5);
					}
					if(winner == player) {
						player.sendMessage(ChatColor.WHITE + "  Winner: " + ChatColor.YELLOW + "20");
						plugin.accounts.addCoins(player, 20);
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
		
		Util.getBuildAreaManager().clearBuildAreas();
		
		for(UUID playerUuid : players) {
			Player player = Bukkit.getPlayer(playerUuid);
			player.teleport(lobbySpawn);
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(player);
			}
			
			
			drawings.clear();
			
			number.put(player, 0);
			
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
		
		plugin.pixelBattlesGameboard.updateScoreboard();
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			removePlayer(event.getPlayer().getUniqueId());
		}
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(players.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(players.contains(player.getUniqueId())) {
				event.setCancelled(true);
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
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(status != "STARTED") {
				event.setCancelled(true);
			} else {
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					if(Util.getBuildAreaManager().getBuildArea(player).getBuildBlocks().contains(event.getClickedBlock().getLocation())) {
						player.getInventory().addItem(new ItemStack(event.getClickedBlock().getType(), 1, event.getClickedBlock().getData()));
						event.getClickedBlock().setType(Material.AIR);
						player.playSound(player.getLocation(), Sound.DIG_WOOL, 1, 0);
						
					}
				}
			}
			
			if(player.getItemInHand().getType() == Material.WATCH) {
				Bukkit.dispatchCommand(player, "hub");
			}
			
		}
	}
	
}

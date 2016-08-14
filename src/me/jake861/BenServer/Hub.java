package me.jake861.BenServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import de.slikey.*;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.ParticleEffect;
import me.jake861.BenServer.Cosmetics.GUI.Cosmetics;

public class Hub implements Listener {
	
	private Main plugin;
	public Location spawn;
	public World hub = Bukkit.getWorld("NewHub");
	
	public Location spleefSignLoc;
	public Location cacticeSignLoc;
	public Location towerDefenseSignLoc;
	
	public Sign spleefSign;
	public Sign cacticeSign;
	public Sign towerDefenseSign;
	
	public Map<UUID, Boolean> touchedGround = new HashMap<>();
	
	
	public Hub(Main plugin) {
		this.plugin = plugin;
		
		if(hub == null) {
			WorldCreator wc = new WorldCreator("NewHub");
			hub = Bukkit.createWorld(wc);
		}
		
		spawn = new Location(hub, -45.5, 173.5, -85.5, -180, 0);
		spleefSignLoc = new Location(hub, -41, 5, -39);
		cacticeSignLoc = new Location(hub, -41, 5, -38);
		towerDefenseSignLoc = new Location(hub, -41, 5, -40);

		
		
	}
	
	public List<UUID> players = new ArrayList<>();
	public List<UUID> builders = new ArrayList<>();
	World world = hub;
	public Map<UUID, String> game = new HashMap<>();

	
	public void init() {
		
		setTime();
		
	}

	
	public void addPlayer(UUID uuid) {
		
		
		
		players.add(uuid);
		
		
		plugin.scoreboards.updateScoreboard();
		
		Player player = Bukkit.getPlayer(uuid);
		
		plugin.particles.particlesEnabled.put(player.getUniqueId(), true);
		
		for(Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
		}

		player.teleport(spawn);
		
		for(Player other : Bukkit.getOnlinePlayers()) {
			other.showPlayer(player);
		}
		
		
		player.setHealth(20);
		player.setMaxHealth(20);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setFoodLevel(20);
		player.setFlying(false);
		player.setAllowFlight(true);
		this.game.put(uuid, "HUB");
		this.touchedGround.put(uuid, false);
	
		
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta compassIm = compass.getItemMeta();
		compassIm.setDisplayName(ChatColor.AQUA + "Game Menu");
		compass.setItemMeta(compassIm);
		
		ItemStack chest = new ItemStack(Material.CHEST, 1);
		ItemMeta chestIm = chest.getItemMeta();
		chestIm.setDisplayName(ChatColor.BLUE + "Cosmetics");
		chest.setItemMeta(chestIm);
		
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "title " + player.getName() + " title {text:\"" + ChatColor.WHITE + "Welcome to " + ChatColor.AQUA + "" + ChatColor.BOLD + "Mini" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Craft" + ChatColor.WHITE + "!\"}");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "title " + player.getName() + " subtitle {text:\"" + ChatColor.WHITE + "Click with the compass to choose a game! \"}");
		
		player.getInventory().addItem(compass);
		player.getInventory().addItem(chest);
		
		plugin.cosmeticManager.removeCosmetics(player);
		
		
		
	}
	
	public void removePlayer(UUID uuid) {
		this.game.put(uuid, null);
		if(players.contains(uuid)) {
			players.remove(uuid);
		}
		
		
		
		if(builders.contains(uuid)) {
			builders.remove(uuid);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.scoreboards.updateScoreboard();
			}
		}, 1);
		
		plugin.cosmeticManager.removeCosmetics(Bukkit.getPlayer(uuid));
			
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		UUID playerUuid = player.getUniqueId();
		plugin.hub.addPlayer(playerUuid);
		event.setJoinMessage(null);
		
		
		
		for(Player play : Bukkit.getOnlinePlayers()) {
			play.sendMessage(ChatColor.GRAY + "[+] " + ChatColor.YELLOW + Bukkit.getPlayer(playerUuid).getName());
		}
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		UUID playerUuid = event.getPlayer().getUniqueId();
		if(plugin.hub.players.contains(playerUuid)) {
			plugin.hub.removePlayer(playerUuid);
		}
		
		event.setQuitMessage(null);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.GRAY + "[-] " + ChatColor.YELLOW + Bukkit.getPlayer(playerUuid).getName());
		}
		
		plugin.message.lastSent.put(playerUuid, null);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.scoreboards.updateScoreboard();
			}
		}, 5);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) entity;
			UUID playerUuid = player.getUniqueId();
			if(players.contains(playerUuid)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if(damager instanceof Player) {
			Player player = (Player) damager;
			UUID playerUuid = player.getUniqueId();
			if(players.contains(playerUuid)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(!builders.contains(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(!builders.contains(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setFormat(plugin.accounts.getRankPrefix(plugin.accounts.getRank(event.getPlayer())) + ChatColor.YELLOW + event.getPlayer().getName() + " " + ChatColor.WHITE + event.getMessage());
	}
	
	public void addBuilder(UUID playerUuid) {
		builders.add(playerUuid);
		Bukkit.getPlayer(playerUuid).setGameMode(GameMode.CREATIVE);
		Bukkit.getPlayer(playerUuid).setAllowFlight(true);
		Bukkit.getPlayer(playerUuid).setFlying(true);
		Bukkit.getPlayer(playerUuid).setFlySpeed(5);
	}
	
	public void removeBuilder(UUID playerUuid) {
		builders.remove(playerUuid);
		Bukkit.getPlayer(playerUuid).setGameMode(GameMode.SURVIVAL);
		Bukkit.getPlayer(playerUuid).setFlying(false);
		Bukkit.getPlayer(playerUuid).setAllowFlight(false);
		Bukkit.getPlayer(playerUuid).teleport(spawn);
		Bukkit.getPlayer(playerUuid).getInventory().clear();
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		event.setMaxPlayers(100);
		
		event.setMotd(ChatColor.WHITE + "            ∞∞∞∞∞" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + " Mini" + ChatColor.AQUA + "" + ChatColor.BOLD + "Craft " + ChatColor.WHITE + "∞∞∞∞∞\n" 
		+ ChatColor.AQUA +"" + ChatColor.BOLD +  "    NEW GAME! " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PIXEL BATTLES!" + ChatColor.AQUA + "" + ChatColor.BOLD + " TRY NOW!");
		
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
	
	public void resetBlock(final Block block, final Material material) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				block.setType(material);
			}
		}, 60);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player.getUniqueId())) {
			
			if(event.getAction() == Action.RIGHT_CLICK_AIR ||
			   event.getAction() == Action.RIGHT_CLICK_BLOCK ||
			   event.getAction() == Action.LEFT_CLICK_AIR ||
			   event.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(player.getItemInHand().getType() == Material.COMPASS) {
					if(player.getItemInHand().getItemMeta().hasDisplayName()) {
						if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Game Menu")) {
							GameGUI guiClass = new GameGUI(player, plugin);
							player.openInventory(guiClass.displayGui());
						}
					}
				}
				
				if(player.getWorld() == plugin.commands.intro) {
					if(player.getItemInHand().getType() == Material.WATCH) {
						player.teleport(spawn);
						player.getInventory().clear();
						ItemStack compass = new ItemStack(Material.COMPASS, 1);
						ItemMeta compassIm = compass.getItemMeta();
						compassIm.setDisplayName(ChatColor.AQUA + "Game Menu");
						compass.setItemMeta(compassIm);
						player.getInventory().addItem(compass);
					}
				}
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR ||
				   event.getAction() == Action.RIGHT_CLICK_BLOCK ||
				   event.getAction() == Action.LEFT_CLICK_AIR ||
				   event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(player.getItemInHand().getType().equals(Material.CHEST)) {
				
				if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Cosmetics")) {
					
					Cosmetics cosmetics = new Cosmetics(player, plugin);
					player.openInventory(cosmetics.displayGui());
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent event) {
		if(players.contains(((Player) event.getWhoClicked()).getUniqueId())) {
			Player player = (Player) event.getWhoClicked();
			if(!builders.contains(player.getUniqueId())) {
				
				event.setCancelled(true);
				
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(players.contains(((Player) event.getWhoClicked()).getUniqueId())) {
			Player player = (Player) event.getWhoClicked();
			if(!builders.contains(player.getUniqueId())) {
				
				event.setCancelled(true);
				if(event.getCurrentItem() != null) {
					if(event.getCurrentItem().getType() != Material.AIR) {
						if(event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
							if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Cactice")) {
								plugin.getServer().dispatchCommand(player, "cactice");
							}
							
							if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Spleef")) {
								plugin.getServer().dispatchCommand(player, "spleef");
							}
							
							if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Tower Defense")) {
								plugin.getServer().dispatchCommand(player, "towerdefense");
							}
							
							if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Pixel Battles")) {
								plugin.getServer().dispatchCommand(player, "pixelbattles");
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(!builders.contains(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			if(!builders.contains(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	

	
	
	int timeId;
	
	public void setTime() {
		
		timeId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for(World world: plugin.getServer().getWorlds()) {
					world.setTime(1000L);
					world.setStorm(false);
				}
				
				
				
			}
		}, 0, 2);
		
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerFly(PlayerMoveEvent event) {
		if(players.contains(event.getPlayer().getUniqueId())) {
			Player player = event.getPlayer();
			
			if(hub.getBlockAt(new Location(hub, player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ())).getType() != Material.AIR) {
				
				player.setAllowFlight(true);
				
			}
			
			if(player.isFlying()) {
				if(!builders.contains(player.getUniqueId())) {
					player.setFlying(false);
					player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 1, player.getLocation().getDirection().getZ()));
					player.setAllowFlight(false);
					player.playEffect(player.getLocation(), org.bukkit.Effect.BLAZE_SHOOT, 1);
				}
			}
			
			
			if(player.getLocation().getY() <= 140 && player.getWorld() == hub) {
				player.teleport(spawn);
			}
		}
		
		
	}
	
	@EventHandler
	public void onProjectileThrow(ProjectileLaunchEvent event) {
		if(players.contains(((Player) event.getEntity().getShooter()).getUniqueId())) {
			Projectile proj = (Projectile) event.getEntity();
			proj.setVelocity(proj.getVelocity().multiply(10));
		}
	}
	
	
	
	
	
 
}

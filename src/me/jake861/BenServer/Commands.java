package me.jake861.BenServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Commands implements CommandExecutor {
	
	private Main plugin;
	
	public World intro = Bukkit.getWorld("TJ_CypressIntro");
	
	public Commands(Main plugin) {
		this.plugin = plugin;
		
		if(intro == null) {
			WorldCreator wc = new WorldCreator("TJ_CypressIntro");
			intro = Bukkit.createWorld(wc);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("hub")) {
			
			Player player = (Player) sender;
			
			if(plugin.hub.game.get(player.getUniqueId()) != "HUB") {
				if(plugin.hub.game.get(player.getUniqueId()) == "CACTICE") {
					plugin.cactice.removePlayer(player.getUniqueId());
				}
				if(plugin.hub.game.get(player.getUniqueId()) == "SPLEEF") {
					plugin.spleef.removePlayer(player.getUniqueId());
				}
				if(plugin.hub.game.get(player.getUniqueId()) == "TOWER") {
					plugin.towerDefense.removePlayer(player.getUniqueId());
				}
				
				if(plugin.hub.game.get(player.getUniqueId()) == "PIXEL") {
					plugin.pixelBattles.removePlayer(player.getUniqueId());
				}
				
				
				plugin.hub.addPlayer(player.getUniqueId());
			} else {
				player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You are already in the hub!");
			}
			
			return true;
			
		} 
		
		if(cmd.getName().equalsIgnoreCase("updaterank")) {
			if(sender instanceof Player) {	
				Player player = (Player) sender;
				if(plugin.accounts.getRank(player).equalsIgnoreCase("OWNER")) {
					if(args.length <= 1) {
						player.sendMessage(ChatColor.RED + "Not enough arguments.");
					} else {
						String playerName = args[0];
						OfflinePlayer rankSetter = null;
						boolean found = false;
						
						for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
							if(offlinePlayer.getName().equalsIgnoreCase(playerName)) {
								rankSetter = offlinePlayer;
								found = true;
							}
						}
						
						if(found) {
							if(args[1].equalsIgnoreCase("OWNER") || args[1].equalsIgnoreCase("MEMBER") || args[1].equalsIgnoreCase("YOUTUBE")) {
								plugin.accounts.setRank(rankSetter, args[1]);
								
								try {
									plugin.accounts.getConfig().save(plugin.accounts.file);
								} catch(Exception exception) {
									exception.printStackTrace();
								}
							} else {
								player.sendMessage(ChatColor.RED + "That rank doesn't exist.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "That player doesn't exist.");
						}
						
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.GRAY + "You must have rank " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "OWNER" + ChatColor.GRAY + " to set a rank!");
				}
			} else {
					if(args.length <= 1) {
						System.out.println(ChatColor.RED + "Not enough arguments.");
					} else {
						String playerName = args[0];
						OfflinePlayer rankSetter = null;
						boolean found = false;
						
						for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
							if(offlinePlayer.getName().equalsIgnoreCase(playerName)) {
								rankSetter = offlinePlayer;
								found = true;
							}
						}
						
						if(found) {
							if(args[1].equalsIgnoreCase("OWNER") || args[1].equalsIgnoreCase("MEMBER")) {
								plugin.accounts.setRank(rankSetter, args[1]);
								try {
									plugin.accounts.getConfig().save(plugin.accounts.file);
								} catch(Exception exception) {
									exception.printStackTrace();
								}
							} else {
								System.out.println(ChatColor.RED + "That rank doesn't exist.");
							}
						} else {
							System.out.println(ChatColor.RED + "That player doesn't exist.");
						}
					}

			}
				
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("buildmode")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(plugin.accounts.getRank(player).equalsIgnoreCase("OWNER")) {
					if(plugin.hub.players.contains(player.getUniqueId())) {
						if(plugin.hub.builders.contains(player.getUniqueId())) {
							plugin.hub.removeBuilder(player.getUniqueId());
						} else {
							plugin.hub.addBuilder(player.getUniqueId());
						}
					} else {
						player.sendMessage(ChatColor.RED + "You must be in the hub!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.GRAY + "You must have rank " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "OWNER" + ChatColor.GRAY + " to enter Build Mode!");
				}
			} else {
				System.out.println("You must be a player to use this!");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("coins")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.YELLOW + "You currently have " + Double.toString(plugin.accounts.getCoins(player)) + " coins!");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(plugin.cactice.players.contains(player.getUniqueId())) {
					if(args.length > 0) {
						if(plugin.cactice.status == "WAITING" || plugin.cactice.status == "COUNTING") {
							if(args[0].equalsIgnoreCase("waller")) {
								plugin.cactice.chooseWaller(player);
							}
						} else {
							player.sendMessage("Unknown command. Type \"/help\" for help.");
						}
					} else {
						if(plugin.cactice.status == "WAITING") {
							player.sendMessage(ChatColor.RED + "/kit <kit name>");
							player.sendMessage(ChatColor.RED + "Type /kits to see a list of all kits for your game. You can only choose a kit during the start countdown for each game.");
						} else {
							player.sendMessage("Unknown command. Type \"/help\" for help.");
						}
					}
				} else {
					player.sendMessage("Unknown command. Type \"/help\" for help.");
				}
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("cactice")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!plugin.cactice.players.contains(player.getUniqueId())) {
					if(plugin.hub.game.get(player.getUniqueId()) == "HUB") {
						if(plugin.cactice.players.size() < plugin.cactice.MAX_PLAYERS) {
							plugin.hub.players.remove(player.getUniqueId());
							plugin.cactice.addPlayer(player.getUniqueId());
							plugin.hub.game.put(player.getUniqueId(), "CACTICE");
							

						} else {
							player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game is full!");
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You aren't in the hub!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You are already in Cactice!");
				}
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("spleef")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!plugin.spleef.players.contains(player.getUniqueId())) {
					if(plugin.hub.game.get(player.getUniqueId()) == "HUB") {
						if(plugin.spleef.players.size() < plugin.spleef.MAX_PLAYERS) {
							plugin.hub.players.remove(player.getUniqueId());
							plugin.spleef.addPlayer(player.getUniqueId());
							plugin.hub.game.put(player.getUniqueId(), "SPLEEF");
							

						} else {
							player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game is full!");
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You aren't in the hub!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You are already in Spleef!");
				}
			}
			return true;
		}
		
		
		
		if(cmd.getName().equalsIgnoreCase("towerdefense")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!plugin.spleef.players.contains(player.getUniqueId())) {
					if(plugin.hub.game.get(player.getUniqueId()) == "HUB") {
						if(plugin.towerDefense.players.size() < plugin.towerDefense.MAX_PLAYERS) {
							plugin.hub.players.remove(player.getUniqueId());
							plugin.towerDefense.addPlayer(player.getUniqueId());
							plugin.hub.game.put(player.getUniqueId(), "TOWER");
							

						} else {
							player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game is full!");
						}
							
					} else {
						player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You aren't in the hub!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You are already in Tower Defense!");
				}
			}
			return true;
		}
		
		

		if(cmd.getName().equalsIgnoreCase("pixelbattles")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!plugin.pixelBattles.players.contains(player.getUniqueId())) {
					if(plugin.hub.game.get(player.getUniqueId()) == "HUB") {
						if(plugin.pixelBattles.players.size() < plugin.pixelBattles.MAX_PLAYERS) {
							if(plugin.pixelBattles.status == "WAITING" || plugin.pixelBattles.status == "COUNTING") {
								plugin.hub.game.put(player.getUniqueId(), "PIXEL");
								player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
								plugin.hub.players.remove(player.getUniqueId());
								plugin.pixelBattles.addPlayer(player.getUniqueId());
								
							} else {
								player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game already started!");
							}

						} else {
							player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "That game is full!");
						}
							
					} else {
						player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You aren't in the hub!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.YELLOW + "You are already in Pixel Battles!");
				}
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("kits")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(plugin.hub.game.get(player.getUniqueId()) != null) {
					if(plugin.hub.game.get(player.getUniqueId()) == "HUB") {
						player.sendMessage(ChatColor.AQUA + "=====================================");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + " Kits");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Cactice");
						player.sendMessage(ChatColor.AQUA + "   Knocker  " + ChatColor.WHITE + "A stick enchanted with Knockback 10.");
						player.sendMessage(ChatColor.AQUA + "   Waller  " + ChatColor.WHITE + "KB stick + create a 3x3 wall when you");
						player.sendMessage("   right click a block.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Spleef");
						player.sendMessage(ChatColor.AQUA + "   Spleefer  " + ChatColor.WHITE + "Immediately destroy blocks upon click.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Tower Defense");
						player.sendMessage(ChatColor.AQUA + "   Defender  " + ChatColor.WHITE + "Full iron armor + stone sword + bow.");
						player.sendMessage("   10 arrows + 1 arrow every 7 seconds.");
						player.sendMessage(ChatColor.RED + "   Invader  " + ChatColor.WHITE + "Full chain armor + stone sword + bow. 5 arrows.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "=====================================");
					} else if(plugin.hub.game.get(player.getUniqueId()) == "CACTICE") {
						player.sendMessage(ChatColor.AQUA + "=====================================");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + " Cactice Kits");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "  Knocker  " + ChatColor.WHITE + "A stick enchanted with Knockback 10.");
						player.sendMessage(ChatColor.AQUA + "  Waller  " + ChatColor.WHITE + "KB stick + create a 3x3 wall when you");
						player.sendMessage("  right click a block.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "=====================================");
					} else if(plugin.hub.game.get(player.getUniqueId()) == "SPLEEF") {
						player.sendMessage(ChatColor.AQUA + "=====================================");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + " Spleef Kits");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "  Spleefer  " + ChatColor.WHITE + "Immediately destroy blocks upon click.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "=====================================");
					} else if(plugin.hub.game.get(player.getUniqueId()) == "TOWER") {
						player.sendMessage(ChatColor.AQUA + "=====================================");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + " Tower Defense Kits");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "  Defender  " + ChatColor.WHITE + "Full iron armor + stone sword + bow.");
						player.sendMessage("  10 arrows + 1 arrow every 7 seconds.");
						player.sendMessage(ChatColor.RED + "  Invader  " + ChatColor.WHITE + "Full chain armor + stone sword + bow. 5 arrows.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "=====================================");
					} else {
						player.sendMessage(ChatColor.AQUA + "=====================================");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + " Kits");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Cactice");
						player.sendMessage(ChatColor.AQUA + "  Knocker  " + ChatColor.WHITE + "A stick enchanted with Knockback 10.");
						player.sendMessage(ChatColor.AQUA + "  Waller  " + ChatColor.WHITE + "KB stick + create a 3x3 wall when you right click a block.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Spleef");
						player.sendMessage(ChatColor.AQUA + "  Spleefer  " + ChatColor.WHITE + "Immediately destroy blocks upon click.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  Tower Defense");
						player.sendMessage(ChatColor.AQUA + "   Defender  " + ChatColor.WHITE + "Full iron armor + stone sword + bow.");
						player.sendMessage("   10 arrows + 1 arrow every 7 seconds.");
						player.sendMessage(ChatColor.RED + "  Invader  " + ChatColor.WHITE + "Full chain armor + stone sword + bow. 5 arrows.");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "=====================================");
					}
				}
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("savespleef")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(plugin.accounts.getRank(player).equalsIgnoreCase("OWNER")) {
					plugin.spleef.saveWorld();
					player.sendMessage(ChatColor.GREEN + "Successfully saved Spleef.");
				} else {
					player.sendMessage(ChatColor.BLUE + "[Server] " + ChatColor.GRAY + "You must have rank " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "OWNER" + ChatColor.GRAY + " to enter do this!");
				}
			}
			return true;
		}
		
		
		
		if(cmd.getName().equalsIgnoreCase("intro")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(plugin.accounts.getRank(player).equalsIgnoreCase("YOUTUBE") || plugin.accounts.getRank(player).equalsIgnoreCase("OWNER")) {
					if(plugin.hub.players.contains(player.getUniqueId())) {
						player.getInventory().clear();
						player.teleport(new Location(intro, -4, 65, -7));
						ItemStack clock = new ItemStack(Material.WATCH);
						ItemMeta clockMeta = clock.getItemMeta();
						clockMeta.setDisplayName(ChatColor.AQUA + "Hub");
						clock.setItemMeta(clockMeta);
						player.getInventory().setItem(8, clock);
						
					} else {
						player.sendMessage(ChatColor.RED + "You aren't in the hub.");
					}
				}
				return true;
			}
			
		}
		
		
		if(cmd.getName().equalsIgnoreCase("addcoins")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.getName().equalsIgnoreCase("Jake861")) {
					if(args.length == 2) {
						boolean found = false;
						OfflinePlayer coiner = null;
						for(OfflinePlayer pl : Bukkit.getOfflinePlayers()) {
							if(pl.getName().equalsIgnoreCase(args[0])) {
								found = true;
								coiner = pl;
							}
						}
						
						if(found) {
							try {
								plugin.accounts.addCoins(coiner, Integer.parseInt(args[1]));
							} catch(Exception ex) {
								player.sendMessage(ChatColor.RED + "That isn't a number!");
							}	
						} else {
							player.sendMessage(ChatColor.RED + "That player doesn't exist!");
						}
						
					} else {
						player.sendMessage(ChatColor.RED + "Improper arguments.");
					}
				}
			} else {
				if(args.length == 2) {
					boolean found = false;
					OfflinePlayer coiner = null;
					for(OfflinePlayer pl : Bukkit.getOfflinePlayers()) {
						if(pl.getName().equalsIgnoreCase(args[0])) {
							found = true;
							coiner = pl;
						}
					}
					
					if(found) {
						try {
							plugin.accounts.addCoins(coiner, Integer.parseInt(args[1]));
						} catch(Exception ex) {
							System.out.println(ChatColor.RED + "That isn't a number!");
						}	
					} else {
						System.out.println(ChatColor.RED + "That player doesn't exist!");
					}
					
				} else {
					System.out.println(ChatColor.RED + "Improper arguments.");
				}
			}
			return true;
		}
		
		
		if(cmd.getName().equalsIgnoreCase("removecoins")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.getName().equalsIgnoreCase("Jake861")) {
					if(args.length == 2) {
						boolean found = false;
						OfflinePlayer coiner = null;
						for(OfflinePlayer pl : Bukkit.getOfflinePlayers()) {
							if(pl.getName().equalsIgnoreCase(args[0])) {
								found = true;
								coiner = pl;
							}
						}
						
						if(found) {
							try {
								plugin.accounts.removeCoins(coiner, Integer.parseInt(args[1]));
							} catch(Exception ex) {
								player.sendMessage(ChatColor.RED + "That isn't a number!");
							}	
						} else {
							player.sendMessage(ChatColor.RED + "That player doesn't exist!");
						}
						
					} else {
						player.sendMessage(ChatColor.RED + "Improper arguments.");
					}
				}
			} else {
				if(args.length == 2) {
					boolean found = false;
					OfflinePlayer coiner = null;
					for(OfflinePlayer pl : Bukkit.getOfflinePlayers()) {
						if(pl.getName().equalsIgnoreCase(args[0])) {
							found = true;
							coiner = pl;
						}
					}
					
					if(found) {
						try {
							plugin.accounts.removeCoins(coiner, Integer.parseInt(args[1]));
						} catch(Exception ex) {
							System.out.println(ChatColor.RED + "That isn't a number!");
						}	
					} else {
						System.out.println(ChatColor.RED + "That player doesn't exist!");
					}
					
				} else {
					System.out.println(ChatColor.RED + "Improper arguments.");
				}
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("resetspleef")) {
			if(plugin.accounts.getRank((Player) sender).equals("OWNER")) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.getWorld().equals(Bukkit.getWorld("Spleef"))) {
						player.teleport(plugin.spleef.lobbySpawn);
					}
				}
				plugin.spleef.resetMap();
			} 
			return true;
		}
		

		return false;
	}
	
	

	
	
	
}

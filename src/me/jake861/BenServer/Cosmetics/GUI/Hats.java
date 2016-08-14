package me.jake861.BenServer.Cosmetics.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jake861.BenServer.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class Hats implements Listener {
	
	private Main plugin;
	private Player player;
	
	public Hats(Player player, Main plugin) {
		this.plugin = plugin;
		this.player = player;
		this.plugin.registerEvents(this);
	}
	
	
	private Inventory gui;
	
	
	
	private List<String> spaceLore = new ArrayList<>();
	private List<String> cheeseLore = new ArrayList<>();
	
	public ItemStack space;
	public ItemStack cheese;
	public ItemStack back;
	
	
	public Inventory displayGui() {
		
		addLores();
		
		gui = Bukkit.createInventory(player, 9, ChatColor.AQUA + "Hats");
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.has")) {
			if(plugin.cosmeticManager.currentHat.get(player.getUniqueId()) != "SPACE") {
				space = new ItemStack(Material.GLASS, 1);
				ItemMeta spaceMeta = space.getItemMeta();
				spaceMeta.setLore(spaceLore);
				spaceMeta.setDisplayName(ChatColor.WHITE + "Space Helmet");
				space.setItemMeta(spaceMeta);
			} else {
				space = new ItemStack(Material.EMERALD_BLOCK);
				ItemMeta spaceMeta = space.getItemMeta();
				spaceMeta.setDisplayName(ChatColor.AQUA + "Space Helmet");
				space.setItemMeta(spaceMeta);
			}
		} else {
			space = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta spaceMeta = space.getItemMeta();
			spaceMeta.setLore(spaceLore);
			spaceMeta.setDisplayName(ChatColor.WHITE + "Space Helmet");
			space.setItemMeta(spaceMeta);
		}
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.cheese.has")) {
			if(plugin.cosmeticManager.currentHat.get(player.getUniqueId()) != "CHEESE") {
				cheese = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData());
				ItemMeta cheeseMeta = cheese.getItemMeta();
				cheeseMeta.setLore(cheeseLore);
				cheeseMeta.setDisplayName(ChatColor.GOLD + "Cheese");
				cheese.setItemMeta(cheeseMeta);
			} else {
				cheese = new ItemStack(Material.EMERALD_BLOCK);
				ItemMeta cheeseMeta = cheese.getItemMeta();
				cheeseMeta.setDisplayName(ChatColor.GOLD + "Cheese");
				cheese.setItemMeta(cheeseMeta);
			}
		} else {
			cheese = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta cheeseMeta = cheese.getItemMeta();
			cheeseMeta.setLore(cheeseLore);
			cheeseMeta.setDisplayName(ChatColor.WHITE + "Cheese");
			cheese.setItemMeta(cheeseMeta);
		}
		
		back = new ItemStack(Material.SIGN, 1);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(ChatColor.WHITE + "<---- BACK");
		back.setItemMeta(backMeta);
		
		
		gui.setItem(0, space);
		gui.setItem(1, cheese);
		gui.setItem(8, back);
		
		
		return gui;
	}
	
	public void addLores() {
		
		spaceLore.clear();
		cheeseLore.clear();
		
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.has")) {
			spaceLore.add(" ");
			spaceLore.add(ChatColor.AQUA + "Don't let yourself suffocate");
			spaceLore.add(ChatColor.AQUA + "while exploring the universe!");
			spaceLore.add(" ");	
		} else {
			if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
			
				spaceLore.add(" ");
				spaceLore.add(ChatColor.RED + "You don't own this item!");
				spaceLore.add(ChatColor.YELLOW + "You can purchase this item");
				spaceLore.add(ChatColor.YELLOW + "for " + ChatColor.GREEN + "2000 " + ChatColor.YELLOW + "coins!");
				spaceLore.add(" ");
				
			} else {
				
				spaceLore.add(" ");
				spaceLore.add(ChatColor.RED + "You don't own this item!");
				spaceLore.add(ChatColor.YELLOW + "You don't have enough coins");
				spaceLore.add(ChatColor.YELLOW + "to purchase this item!");
				spaceLore.add(" ");
				
			}
		}
			
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.cheese.has")) {
			cheeseLore.add(" ");
			cheeseLore.add(ChatColor.AQUA + "Straight from");
			cheeseLore.add(ChatColor.AQUA + "Wisconsin!");
			cheeseLore.add(" ");	
		} else {
			if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
			
				cheeseLore.add(" ");
				cheeseLore.add(ChatColor.RED + "You don't own this item!");
				cheeseLore.add(ChatColor.YELLOW + "You can purchase this item");
				cheeseLore.add(ChatColor.YELLOW + "for " + ChatColor.GREEN + "2000 " + ChatColor.YELLOW + "coins!");
				cheeseLore.add(" ");
				
			} else {
				
				cheeseLore.add(" ");
				cheeseLore.add(ChatColor.RED + "You don't own this item!");
				cheeseLore.add(ChatColor.YELLOW + "You don't have enough coins");
				cheeseLore.add(ChatColor.YELLOW + "to purchase this item!");
				cheeseLore.add(" ");
				
			}
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().equals(gui)) {
			
			event.setCancelled(true);
			
			if(event.getCurrentItem().equals(space)) {
				if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.has")) {	
					if(plugin.cosmeticManager.currentHat.get(player.getUniqueId()) != "SPACE") {
						spaceLore.clear();
						plugin.cosmeticManager.removeHat(player);
						ItemMeta spaceMeta = space.getItemMeta();
						spaceMeta.setDisplayName(ChatColor.AQUA + "Space Helmet");
						space.setItemMeta(spaceMeta);
						plugin.cosmeticManager.currentHat.put(player.getUniqueId(), "SPACE");
						player.getInventory().setHelmet(space);
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Equipped " + ChatColor.YELLOW + "Space Helmet");
					} else {
						plugin.cosmeticManager.removeHat(player);
						player.getInventory().setHelmet(null);
						player.openInventory(displayGui());
					}
				} else {
					if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
						plugin.accounts.removeCoins(player, 2000);
						plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.has", true);
						plugin.scoreboards.updateScoreboard();
						plugin.accounts.save();
					} 
					player.openInventory(displayGui());
				}
			}
			
			if(event.getCurrentItem().equals(cheese)) {
				if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.cheese.has")) {	
					if(plugin.cosmeticManager.currentHat.get(player.getUniqueId()) != "CHEESE") {
						cheeseLore.clear();
						plugin.cosmeticManager.removeHat(player);
						ItemMeta cheeseMeta = cheese.getItemMeta();
						cheeseMeta.setDisplayName(ChatColor.GOLD + "Cheese");
						cheese.setItemMeta(cheeseMeta);
						plugin.cosmeticManager.currentHat.put(player.getUniqueId(), "CHEESE");
						player.getInventory().setHelmet(cheese);
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Equipped " + ChatColor.YELLOW + "Cheese");
					} else {
						plugin.cosmeticManager.removeHat(player);
						player.openInventory(displayGui());
					}
				} else {
					if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
						plugin.accounts.removeCoins(player, 2000);
						plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.cheese.has", true);
						plugin.scoreboards.updateScoreboard();
						plugin.accounts.save();
					} 
					player.openInventory(displayGui());
				}
			}
			
			if(event.getCurrentItem().equals(back)) {
				Cosmetics cosmetics = new Cosmetics(player, plugin);
				player.openInventory(cosmetics.displayGui());
			}
			
			
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.cosmeticManager.currentHat.put(event.getPlayer().getUniqueId(), "NONE");
	}
	
	
}

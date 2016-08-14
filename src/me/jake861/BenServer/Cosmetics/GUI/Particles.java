package me.jake861.BenServer.Cosmetics.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jake861.BenServer.*;
import org.bukkit.ChatColor;

public class Particles implements Listener {
	
	private Main plugin;
	private Player player;
	
	public Particles(Player player, Main plugin) {
		this.plugin = plugin;
		this.player = player;
		this.plugin.registerEvents(this);
	}
	
	
	private Inventory gui;
	
	
	
	private List<String> flameTrailLore = new ArrayList<>();
	private List<String> starredNameLore = new ArrayList<>();

	public ItemStack flameTrail;
	public ItemStack starredName;
	public ItemStack back;
	
	
	public Inventory displayGui() {
		
		addLores();
		
		gui = Bukkit.createInventory(player, 9, ChatColor.AQUA + "Particle Effects");
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.flame_trail.has")) {
			if(plugin.particles.currentParticle.get(player.getUniqueId()) != "FLAME_TRAIL") {
				flameTrail = new ItemStack(Material.BLAZE_POWDER, 1);
				ItemMeta flameTrailMeta = flameTrail.getItemMeta();
				flameTrailMeta.setLore(flameTrailLore);
				flameTrailMeta.setDisplayName(ChatColor.WHITE + "Flame Trail");
				flameTrail.setItemMeta(flameTrailMeta);
			} else {
				flameTrail = new ItemStack(Material.EMERALD_BLOCK);
				ItemMeta flameTrailMeta = flameTrail.getItemMeta();
				flameTrailMeta.setDisplayName(ChatColor.AQUA + "Flame Trail");
				flameTrail.setItemMeta(flameTrailMeta);
			}
		} else {
			flameTrail = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta flameTrailMeta = flameTrail.getItemMeta();
			flameTrailMeta.setLore(flameTrailLore);
			flameTrailMeta.setDisplayName(ChatColor.WHITE + "Flame Trail");
			flameTrail.setItemMeta(flameTrailMeta);
		}
		
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.starred_name.has")) {
			if(plugin.particles.currentParticle.get(player.getUniqueId()) != "STARRED_NAME") {
				starredName = new ItemStack(Material.NAME_TAG, 1);
				ItemMeta starredNameMeta = starredName.getItemMeta();
				starredNameMeta.setLore(starredNameLore);
				starredNameMeta.setDisplayName(ChatColor.WHITE + "Starred Name");
				starredName.setItemMeta(starredNameMeta);
			} else {
				starredName = new ItemStack(Material.EMERALD_BLOCK);
				ItemMeta starredNameMeta = starredName.getItemMeta();
				starredNameMeta.setDisplayName(ChatColor.AQUA + "Starred Name");
				starredName.setItemMeta(starredNameMeta);
			}
		} else {
			starredName = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta starredNameMeta = starredName.getItemMeta();
			starredNameMeta.setLore(starredNameLore);
			starredNameMeta.setDisplayName(ChatColor.WHITE + "Starred Name");
			starredName.setItemMeta(starredNameMeta);
		}
		
		
		back = new ItemStack(Material.SIGN, 1);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(ChatColor.WHITE + "<---- BACK");
		back.setItemMeta(backMeta);
		
		gui.setItem(0, flameTrail);
		gui.setItem(1, starredName);
		gui.setItem(8, back);
		
		
		return gui;
	}
	
	public void addLores() {
		
		flameTrailLore.clear();
		starredNameLore.clear();
		
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.flame_trail.has")) {
			flameTrailLore.add(" ");
			flameTrailLore.add(ChatColor.AQUA + "It's like walking on hot coals...");
			flameTrailLore.add(ChatColor.AQUA + "but BETTER!");
			flameTrailLore.add(" ");	
		} else {
			if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 1000) {
			
				flameTrailLore.add(" ");
				flameTrailLore.add(ChatColor.RED + "You don't own this item!");
				flameTrailLore.add(ChatColor.YELLOW + "You can purchase this item");
				flameTrailLore.add(ChatColor.YELLOW + "for " + ChatColor.GREEN + "1000 " + ChatColor.YELLOW + "coins!");
				flameTrailLore.add(" ");
				
			} else {
				
				flameTrailLore.add(" ");
				flameTrailLore.add(ChatColor.RED + "You don't own this item!");
				flameTrailLore.add(ChatColor.YELLOW + "You don't have enough coins");
				flameTrailLore.add(ChatColor.YELLOW + "to purchase this item!");
				flameTrailLore.add(" ");
				
			}
		}
		
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.starred_name.has")) {
			starredNameLore.add(" ");
			starredNameLore.add(ChatColor.AQUA + "You know you always wanted");
			starredNameLore.add(ChatColor.AQUA + "to see your name in big letters ;)");
			starredNameLore.add(" ");	
		} else {
			if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
			
				starredNameLore.add(" ");
				starredNameLore.add(ChatColor.RED + "You don't own this item!");
				starredNameLore.add(ChatColor.YELLOW + "You can purchase this item");
				starredNameLore.add(ChatColor.YELLOW + "for " + ChatColor.GREEN + "2000 " + ChatColor.YELLOW + "coins!");
				starredNameLore.add(" ");
				
			} else {
				
				starredNameLore.add(" ");
				starredNameLore.add(ChatColor.RED + "You don't own this item!");
				starredNameLore.add(ChatColor.YELLOW + "You don't have enough coins");
				starredNameLore.add(ChatColor.YELLOW + "to purchase this item!");
				starredNameLore.add(" ");
				
			}
		}
			
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().equals(gui)) {
			
			event.setCancelled(true);
			
			if(event.getCurrentItem().equals(flameTrail)) {
				if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.flame_trail.has")) {	
					if(plugin.particles.currentParticle.get(player.getUniqueId()) != "FLAME_TRAIL") {
						flameTrailLore.clear();
						plugin.particles.currentParticle.put(player.getUniqueId(), "FLAME_TRAIL");
						ItemMeta flameTrailMeta = flameTrail.getItemMeta();
						flameTrailMeta.setDisplayName(ChatColor.AQUA + "Flame Trail");
						flameTrail.setItemMeta(flameTrailMeta);
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Equipped " + ChatColor.YELLOW + "Flame Trail");
					} else {
						plugin.particles.currentParticle.put(player.getUniqueId(), "NONE");
						player.openInventory(displayGui());
					}
				} else {
					if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 1000) {
						plugin.accounts.removeCoins(player, 1000);
						plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.flame_trail.has", true);
						plugin.scoreboards.updateScoreboard();
						plugin.accounts.save();
					} 
					player.openInventory(displayGui());
				}
			}
			
			
			if(event.getCurrentItem().equals(starredName)) {
				if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.starred_name.has")) {	
					if(plugin.particles.currentParticle.get(player.getUniqueId()) != "STARRED_NAME") {
						starredNameLore.clear();
						plugin.particles.currentParticle.put(player.getUniqueId(), "STARRED_NAME");
						ItemMeta starredNameMeta = starredName.getItemMeta();
						starredNameMeta.setDisplayName(ChatColor.AQUA + "Starred Name");
						starredName.setItemMeta(starredNameMeta);
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Equipped " + ChatColor.YELLOW + "Starred Name");
					} else {
						plugin.particles.currentParticle.put(player.getUniqueId(), "NONE");
						player.openInventory(displayGui());
					}
				} else {
					if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 2000) {
						plugin.accounts.removeCoins(player, 2000);
						plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.particles.starred_name.has", true);
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
	
	
	
}

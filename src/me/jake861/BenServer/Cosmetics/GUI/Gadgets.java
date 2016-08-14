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

public class Gadgets implements Listener {
	
	private Main plugin;
	private Player player;
	
	public Gadgets(Player player, Main plugin) {
		this.plugin = plugin;
		this.player = player;
		this.plugin.registerEvents(this);
	}
	
	
	private Inventory gui;
	
	
	
	private List<String> jetpackLore = new ArrayList<>();

	public ItemStack jetpack;
	public ItemStack back;
	
	public Inventory displayGui() {
		
		addLores();
		
		gui = Bukkit.createInventory(player, 9, ChatColor.AQUA + "Gadgets");
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.gadgets.jetpack.has")) {
			if(plugin.gadgets.currentGadget.get(player.getUniqueId()) != "JETPACK") {
				jetpack = new ItemStack(Material.IRON_CHESTPLATE, 1);
				ItemMeta jetpackMeta = jetpack.getItemMeta();
				jetpackMeta.setLore(jetpackLore);
				jetpackMeta.setDisplayName(ChatColor.WHITE + "Jetpack");
				jetpack.setItemMeta(jetpackMeta);
			} else {
				jetpack = new ItemStack(Material.EMERALD_BLOCK);
				ItemMeta jetpackMeta = jetpack.getItemMeta();
				jetpackMeta.setDisplayName(ChatColor.WHITE + "Jetpack");
				jetpack.setItemMeta(jetpackMeta);
			}
		} else {
			jetpack = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta jetpackMeta = jetpack.getItemMeta();
			jetpackMeta.setLore(jetpackLore);
			jetpackMeta.setDisplayName(ChatColor.WHITE + "Jetpack");
			jetpack.setItemMeta(jetpackMeta);
		}
		
		
		back = new ItemStack(Material.SIGN, 1);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(ChatColor.WHITE + "<---- BACK");
		back.setItemMeta(backMeta);
		
		gui.setItem(0, jetpack);
		gui.setItem(8, back);
		
		
		return gui;
	}
	
	public void addLores() {
		
		jetpackLore.clear();
		
		
		if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.gadgets.jetpack.has")) {
			jetpackLore.add(" ");
			jetpackLore.add(ChatColor.AQUA + "Don't go too high or");
			jetpackLore.add(ChatColor.AQUA + "you'll run out of oxygen!");
			jetpackLore.add(" ");	
		} else {
			if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 3000) {
			
				jetpackLore.add(" ");
				jetpackLore.add(ChatColor.RED + "You don't own this item!");
				jetpackLore.add(ChatColor.YELLOW + "You can purchase this item");
				jetpackLore.add(ChatColor.YELLOW + "for " + ChatColor.GREEN + "3000 " + ChatColor.YELLOW + "coins!");
				jetpackLore.add(" ");
				
			} else {
				
				jetpackLore.add(" ");
				jetpackLore.add(ChatColor.RED + "You don't own this item!");
				jetpackLore.add(ChatColor.YELLOW + "You don't have enough coins");
				jetpackLore.add(ChatColor.YELLOW + "to purchase this item!");
				jetpackLore.add(" ");
				
			}
		}
			
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().equals(gui)) {
			
			event.setCancelled(true);
			
			if(event.getCurrentItem().equals(jetpack)) {
				if(plugin.accounts.getConfig().getBoolean("accounts." + player.getUniqueId().toString() + ".cosmetics.gadgets.jetpack.has")) {	
					if(plugin.gadgets.currentGadget.get(player.getUniqueId()) != "JETPACK") {
						jetpackLore.clear();
						plugin.gadgets.currentGadget.put(player.getUniqueId(), "JETPACK");
						ItemMeta jetpackMeta = jetpack.getItemMeta();
						jetpackMeta.setDisplayName(ChatColor.AQUA + "Jetpack");
						jetpack.setItemMeta(jetpackMeta);
						player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Equipped " + ChatColor.YELLOW + "Jetpack");
						player.sendMessage(ChatColor.BLUE + "[Cosmetics] " + ChatColor.GRAY + "Hold Sneak to fly!");
						
					} else {
						player.getInventory().setChestplate(null);
						plugin.gadgets.currentGadget.put(player.getUniqueId(), "NONE");
						player.openInventory(displayGui());
					}
				} else {
					if(plugin.accounts.getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins") >= 3000) {
						plugin.accounts.removeCoins(player, 3000);
						plugin.accounts.getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.gadgets.jetpack.has", true);
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

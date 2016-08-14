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
import net.md_5.bungee.api.ChatColor;

public class Cosmetics implements Listener {
	
	private Main plugin;
	private Player player;
	
	public Cosmetics(Player player, Main plugin) {
		this.plugin = plugin;
		this.player = player;
		this.plugin.registerEvents(this);
	}
	
	
	private Inventory gui;
	
	private List<String> hatLore = new ArrayList<>();
	private List<String> particlesLore = new ArrayList<>();
	private List<String> gadgetsLore = new ArrayList<>();
	
	public ItemStack hat;
	public ItemStack particles;
	public ItemStack gadgets;
	
	
	public Inventory displayGui() {
		
		addLores();
		
		gui = Bukkit.createInventory(player, 9, ChatColor.AQUA + "Cosmetics");
		
		hat = new ItemStack(Material.GOLD_HELMET, 1);
		ItemMeta hatMeta = hat.getItemMeta();
		hatMeta.setLore(hatLore);
		hatMeta.setDisplayName(ChatColor.BLUE + "Hats");
		hat.setItemMeta(hatMeta);
		
		particles = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta particlesMeta = particles.getItemMeta();
		particlesMeta.setLore(particlesLore);
		particlesMeta.setDisplayName(ChatColor.BLUE + "Particles");
		particles.setItemMeta(particlesMeta);
		
		gadgets = new ItemStack(Material.TRIPWIRE_HOOK, 1);
		ItemMeta gadgetsMeta = gadgets.getItemMeta();
		gadgetsMeta.setLore(gadgetsLore);
		gadgetsMeta.setDisplayName(ChatColor.BLUE + "Gadgets");
		gadgets.setItemMeta(gadgetsMeta);
		
		gui.setItem(0, hat);
		gui.setItem(1, particles);
		gui.setItem(2, gadgets);
		
		
		return gui;
	}
	
	public void addLores() {
		
		hatLore.clear();
		
		hatLore.add(" ");
		hatLore.add(ChatColor.AQUA + "A series of stylish hats for");
		hatLore.add(ChatColor.AQUA + "on your head!");
		hatLore.add(" ");
		
		particlesLore.add(" ");
		particlesLore.add(ChatColor.AQUA + "Fun particles");
		particlesLore.add(ChatColor.AQUA + "that follow you!");
		particlesLore.add(" ");
		
		gadgetsLore.add(" ");
		gadgetsLore.add(ChatColor.AQUA + "These do cooler");
		gadgetsLore.add(ChatColor.AQUA + "things than you do!");
		gadgetsLore.add(" ");
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().equals(gui)) {
			
			event.setCancelled(true);
			
			if(event.getCurrentItem().equals(hat)) {
				Hats hats = new Hats(player, plugin);
				player.openInventory(hats.displayGui());
			}
			
			if(event.getCurrentItem().equals(particles)) {
				Particles particles = new Particles(player, plugin);
				player.openInventory(particles.displayGui());
			}
			
			if(event.getCurrentItem().equals(gadgets)) {
				Gadgets gadgets = new Gadgets(player, plugin);
				player.openInventory(gadgets.displayGui());
			}
		}
	}
	
	
}

package me.jake861.BenServer;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameGUI {
	
	public Player player;
	
	public ArrayList<String> cacticeLore = new ArrayList<>();
	public ArrayList<String> spleefLore = new ArrayList<>();
	public ArrayList<String> towerDefenseLore = new ArrayList<>();
	public ArrayList<String> pixelBattlesLore = new ArrayList<>();
	
	public ItemStack cactice;
	public ItemStack spleef;
	public ItemStack towerDefense;
	public ItemStack pixelBattles;
	
	public Inventory gui;
	private Main plugin;
	
	public GameGUI(Player player, Main plugin) {
		this.player = player;
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public Inventory displayGui() {
		
		spleefLore.clear();
		cacticeLore.clear();
		towerDefenseLore.clear();
		pixelBattlesLore.clear();
		
		addLores();
		
		gui = Bukkit.createInventory(player, 27, ChatColor.AQUA + "Games");
		
		cactice = new ItemStack(Material.CACTUS);
		ItemMeta cacticeim = cactice.getItemMeta();
		cacticeim.setDisplayName(ChatColor.AQUA + "Cactice");
		cacticeim.setLore(cacticeLore);
		cactice.setItemMeta(cacticeim);
		
		spleef = new ItemStack(Material.LEAVES);
		ItemMeta spleefim = spleef.getItemMeta();
		spleefim.setDisplayName(ChatColor.LIGHT_PURPLE + "Spleef");
		spleefim.setLore(spleefLore);
		spleef.setItemMeta(spleefim);
		
		towerDefense = new ItemStack(Material.STONE_SWORD);
		ItemMeta towerDefenseim = towerDefense.getItemMeta();
		towerDefenseim.setDisplayName(ChatColor.GREEN + "Tower Defense");
		towerDefenseim.setLore(towerDefenseLore);
		towerDefense.setItemMeta(towerDefenseim);

		pixelBattles = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.PURPLE.getData());
		ItemMeta pixelBattlesim = pixelBattles.getItemMeta();
		pixelBattlesim.setDisplayName(ChatColor.BLUE + "Pixel Battles");
		pixelBattlesim.setLore(pixelBattlesLore);
		pixelBattles.setItemMeta(pixelBattlesim);
		
		gui.setItem(10, cactice);
		gui.setItem(12, spleef);
		gui.setItem(14, towerDefense);
		gui.setItem(16, pixelBattles);
		
		return gui;
	}
	
	public void addLores() {
		
		spleefLore.add(" ");
		spleefLore.add(ChatColor.YELLOW + "Players: " + ChatColor.AQUA + Integer.toString(plugin.spleef.players.size()) + "/" + Integer.toString(plugin.spleef.MAX_PLAYERS));
		spleefLore.add(ChatColor.YELLOW + "Status: " + ChatColor.AQUA + plugin.spleef.status);
		spleefLore.add(" ");
		spleefLore.add("Dig the ground under other players");
		spleefLore.add("in order to make them fall!");
		spleefLore.add(" ");
		
		cacticeLore.add(" ");
		cacticeLore.add(ChatColor.YELLOW + "Players: " + ChatColor.AQUA + Integer.toString(plugin.cactice.players.size()) + "/" + Integer.toString(plugin.cactice.MAX_PLAYERS));
		cacticeLore.add(ChatColor.YELLOW + "Status: " + ChatColor.AQUA + plugin.cactice.status);
		cacticeLore.add(" ");
		cacticeLore.add("Use a stick enchanted with knockback");
		cacticeLore.add("to knock opponents into cactus!");
		cacticeLore.add(" ");
		
		towerDefenseLore.add(" ");
		towerDefenseLore.add(ChatColor.YELLOW + "Players: " + ChatColor.AQUA + Integer.toString(plugin.towerDefense.players.size()) + "/" + Integer.toString(plugin.towerDefense.MAX_PLAYERS));
		towerDefenseLore.add(ChatColor.YELLOW + "Status: " + ChatColor.AQUA + plugin.towerDefense.status);
		towerDefenseLore.add(" ");
		towerDefenseLore.add("Defenders guard the wool in order");
		towerDefenseLore.add("to stay alive. Invaders destroy the");
		towerDefenseLore.add("wool to prevent defenders from respawning.");
		towerDefenseLore.add(" ");
		
		pixelBattlesLore.add(" ");
		pixelBattlesLore.add(ChatColor.YELLOW + "Players: " + ChatColor.AQUA + Integer.toString(plugin.pixelBattles.players.size()) + "/" + Integer.toString(plugin.pixelBattles.MAX_PLAYERS));
		pixelBattlesLore.add(ChatColor.YELLOW + "Status: " + ChatColor.AQUA + plugin.pixelBattles.status);
		pixelBattlesLore.add(" ");
		pixelBattlesLore.add("Be the fastest drawer in");
		pixelBattlesLore.add("all of the kingdom!");
		pixelBattlesLore.add(" ");
	}

}

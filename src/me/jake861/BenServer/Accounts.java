package me.jake861.BenServer;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jake861.BenServer.Accounts;

public class Accounts implements Listener {
	
	private Main plugin;
	public File file;
	public FileConfiguration fc;
	
	public Accounts(Main plugin) {
		this.plugin = plugin;
	}

	public void init() {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		file = new File(plugin.getDataFolder(), "accounts" + ".yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		fc = YamlConfiguration.loadConfiguration(file);
		
		if(this.getConfig().getConfigurationSection("accounts") == null) {
			this.getConfig().createSection("accounts");
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!this.getConfig().getConfigurationSection("accounts").contains(p.getUniqueId().toString())) {
				createAccount((OfflinePlayer) p);
			}
		}
		
	}
	
	public FileConfiguration getConfig() {
		return plugin.accounts.fc;
	}
	
	public void createAccount(OfflinePlayer player) {
		getConfig().set("accounts." + player.getUniqueId().toString() + ".name", player.getName());
		getConfig().set("accounts." + player.getUniqueId().toString() + ".rank.name", "MEMBER");
		getConfig().set("accounts." + player.getUniqueId().toString() + ".coins", 0);
		getConfig().set("accounts." + player.getUniqueId().toString() + ".kits.cactice.waller.has", false);
		getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.has", false);
		getConfig().set("accounts." + player.getUniqueId().toString() + ".cosmetics.hats.space.wearing", false);
		
		
		
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public String getName(OfflinePlayer player) {
		return getConfig().getString("accounts." + player.getUniqueId().toString() + ".name");
	}
	
	public String getRank(OfflinePlayer player) {
		return getConfig().getString("accounts." + player.getUniqueId().toString() + ".rank.name");
	}
	
	public String getRankPrefix(String rank) {
		if(rank.equalsIgnoreCase("MEMBER")) {
			return "";
		} else if(rank.equalsIgnoreCase("OWNER")) {
			return ChatColor.DARK_RED + "" + ChatColor.BOLD + "OWNER ";
		} else if(rank.equalsIgnoreCase("YOUTUBE")) {
			return ChatColor.RED + "" + ChatColor.BOLD + "YOUTUBE ";
		} else {
			return "";
		}
	}
	
	public Integer getCoins(OfflinePlayer player) {
		return getConfig().getInt("accounts." + player.getUniqueId().toString() + ".coins");
	}
	
	public void setName(OfflinePlayer player, String name) {
		getConfig().set("accounts." + player.getUniqueId().toString() + ".name", name);
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void setRank(OfflinePlayer player, String rank) {
		if(rank.equalsIgnoreCase("member")) {
			getConfig().set("accounts." + player.getUniqueId().toString() + ".rank.name", "MEMBER");
		}
		
		else if(rank.equalsIgnoreCase("owner")) {
			getConfig().set("accounts." + player.getUniqueId().toString() + ".rank.name", "OWNER");
		}
		
		else if(rank.equalsIgnoreCase("youtube")) {
			getConfig().set("accounts." + player.getUniqueId().toString() + ".rank.name", "YOUTUBE");
		}
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void addCoins(OfflinePlayer player, int coins) {
		getConfig().set("accounts." + player.getUniqueId().toString() + ".coins", getCoins(player) + coins);
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void removeCoins(OfflinePlayer player, int coins) {
		getConfig().set("accounts." + player.getUniqueId().toString() + ".coins", getCoins(player) - coins);
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void setCoins(OfflinePlayer player, int coins) {
		getConfig().set("accounts." + player.getUniqueId().toString() + ".coins", coins);
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(getConfig().getConfigurationSection("accounts").getKeys(false).contains(e.getPlayer().getUniqueId().toString())) {
			if(e.getPlayer().getName() != getConfig().getString("accounts." + e.getPlayer().getUniqueId().toString() + ".name")) {
				getConfig().set("accounts." + e.getPlayer().getUniqueId().toString() + ".name", e.getPlayer().getName());
			}
			
			if(getConfig().getString("accounts." + e.getPlayer().getUniqueId().toString() + ".rank.name") == null) {
				setRank(e.getPlayer(), "MEMBER");
			}
		} else {
			createAccount((OfflinePlayer) e.getPlayer());
		}
	}
	
	public void save() {
		try {
			getConfig().save(file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	
	
}

package me.jake861.BenServer.Cosmetics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jake861.BenServer.Main;

public class Hats implements Listener {
	
	private Main plugin;
	
	public Hats(Main plugin) {
		this.plugin = plugin;
		plugin.registerEvents(this);
	}
	
	public Map<UUID, String> currentHat = new HashMap<>();
	
	public void removeHat(Player player) {
		
		currentHat.put(player.getUniqueId(), "NONE");
		
		player.getInventory().setHelmet(null);
	}
	
	public void removeCosmetics(Player player) {
		removeHat(player);
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		currentHat.put(event.getPlayer().getUniqueId(), "NONE");
	}

}

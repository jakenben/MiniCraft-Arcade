package me.jake861.BenServer.Cosmetics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;
import me.jake861.BenServer.Main;

public class Gadgets implements Listener {
	
	private Main plugin;
	
	public Gadgets(Main plugin) {
		this.plugin = plugin;
	}
	
	public Map<UUID, String> currentGadget = new HashMap<>();
	public Map<UUID, Boolean> gadgetsEnabled = new HashMap<>();
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if(currentGadget.get(event.getPlayer().getUniqueId()) == "JETPACK" && gadgetsEnabled.get(event.getPlayer().getUniqueId())) {
			if(event.isSneaking()) {
				Vector velocity;
				if(event.getPlayer().getLocation().getY() <= 200) {
					velocity = new Vector(event.getPlayer().getLocation().getDirection().getX() * 0.8, 0.3, event.getPlayer().getLocation().getDirection().getZ() * 0.8);
				} else {
					velocity = new Vector(event.getPlayer().getLocation().getDirection().getX() * 0.8, 0, event.getPlayer().getLocation().getDirection().getZ() * 0.8);
				}
				event.getPlayer().setVelocity(velocity);
				ParticleEffect.CLOUD.display(event.getPlayer().getLocation(), 50);
				ParticleEffect.FLAME.display(event.getPlayer().getLocation(), 50);
			} 
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(currentGadget.get(event.getPlayer().getUniqueId()) == "JETPACK" && gadgetsEnabled.get(event.getPlayer().getUniqueId())) {
			if(event.getPlayer().isSneaking()) {
				Vector velocity;
				if(event.getPlayer().getLocation().getY() <= 200) {
					velocity = new Vector(event.getPlayer().getLocation().getDirection().getX() * 0.8, 0.3, event.getPlayer().getLocation().getDirection().getZ() * 0.8);
				} else {
					velocity = new Vector(event.getPlayer().getLocation().getDirection().getX() * 0.8, 0, event.getPlayer().getLocation().getDirection().getZ() * 0.8);
				}
				event.getPlayer().setVelocity(velocity);
				ParticleEffect.CLOUD.display(event.getPlayer().getLocation(), 50);
				ParticleEffect.FLAME.display(event.getPlayer().getLocation(), 50);
			} 
		}
	}
	
	
	int gadgetId;
	
	public void repeat() {
		plugin.registerEvents(this);
		gadgetId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(plugin.hub.players.contains(player.getUniqueId())) {
						gadgetsEnabled.put(player.getUniqueId(), true);
					} else {
						gadgetsEnabled.put(player.getUniqueId(), false);
						currentGadget.put(player.getUniqueId(), "NONE");
					}
				}
			}
		}, 0, 1);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		currentGadget.put(event.getPlayer().getUniqueId(), "NONE");
	}
	
}

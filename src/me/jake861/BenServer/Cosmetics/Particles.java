package me.jake861.BenServer.Cosmetics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.util.ParticleEffect;
import me.jake861.BenServer.Main;

public class Particles implements Listener {
	
	private Main plugin;
	
	public Particles(Main plugin) {
		this.plugin = plugin;
	}
	
	public Map<UUID, String> currentParticle = new HashMap<>(); 
	public Map<UUID, Boolean> particlesEnabled = new HashMap<>(); 
	
	int flameTrailId;
	int starredNameId;
	
	public void repeat() {
		
		plugin.registerEvents(this);
		
		flameTrailId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					
					if(particlesEnabled.get(player.getUniqueId())) {
					
						if(currentParticle.get(player.getUniqueId()) == "FLAME_TRAIL") {
							
							ParticleEffect effect = ParticleEffect.FLAME;
							effect.display(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + .3, player.getLocation().getZ()), 30);
							
							ParticleEffect particleEffect = ParticleEffect.BARRIER;
							particleEffect.display(player.getLocation().getDirection().multiply(10), 50, player.getLocation(), 10);
							
							
						}
						
					}
					
				}
				
				
			}
		}, 0, 1);
		
		
		starredNameId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					
					if(particlesEnabled.get(player.getUniqueId())) {
					
						if(currentParticle.get(player.getUniqueId()) == "STARRED_NAME") {
							
		
							TextEffect arc = new TextEffect(plugin.effectManager);
							arc.setLocation(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 3, player.getLocation().getZ(), player.getLocation().getYaw() - 180, player.getLocation().getPitch()));
							arc.particle = ParticleEffect.FIREWORKS_SPARK;
							arc.visibleRange = 50;
							arc.text = player.getName();
							arc.size = 0.06f;
							plugin.effectManager.start(arc);
							cancel(arc);
							
							
							
							
						}
						
					}
					
				}
				
				
			}
		}, 0, 60);
		
		
		
	}
	
	public void cancel(final Effect arc) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				arc.cancel();
			}
		}, 5);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		currentParticle.put(event.getPlayer().getUniqueId(), "NONE");
	}
	

}

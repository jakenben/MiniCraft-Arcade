package me.jake861.BenServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.slikey.effectlib.EffectManager;
import me.jake861.BenServer.Accounts;
import me.jake861.BenServer.Cosmetics.Gadgets;
import me.jake861.BenServer.Cosmetics.Hats;
import me.jake861.BenServer.Cosmetics.Particles;
import me.jake861.BenServer.Paintball.Paintball;
import me.jake861.BenServer.PixelBattles.Gameboard;
import me.jake861.BenServer.PixelBattles.PixelBattles;
import me.jake861.BenServer.PixelBattles.Util;

public class Main extends JavaPlugin {
	
	public Particles particles;
	public Gadgets gadgets;
	public Commands commands;
	public Hub hub;
	public Cactice cactice;
	public Spleef spleef;
	public Accounts accounts;
	public TowerDefense towerDefense;
	public Message message;
	public Scoreboards scoreboards;
	public Hats cosmeticManager;
	public PixelBattles pixelBattles;
	public Gameboard pixelBattlesGameboard;
	public Paintball paintball;
	
	public EffectManager effectManager;

	public void onEnable() {
		
		hub = new Hub(this);
		hub.init();
		
		effectManager = new EffectManager(this);
		
		new Util(this);
		
		
		commands = new Commands(this);
		
		cactice = new Cactice(this);
		
		cosmeticManager = new Hats(this);
		
		spleef = new Spleef(this);
		
		towerDefense = new TowerDefense(this);
		
		accounts = new Accounts(this);
		accounts.init();
		
		message = new Message();
		
		scoreboards = new Scoreboards(this);
		scoreboards.init();
		
		particles = new Particles(this);
		particles.repeat();
		
		gadgets = new Gadgets(this);
		gadgets.repeat();
		
		pixelBattles = new PixelBattles(this);
		
		pixelBattlesGameboard = new Gameboard(this);
		
		paintball = new Paintball(this);
		
		try {
			accounts.getConfig().save(accounts.file);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		registerEvents();
		registerCommands();
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			hub.addPlayer(player.getUniqueId());
		}
	}
	
	public void onDisable() {
		spleef.resetMap();
	}
	
	
	public void registerEvents() {
		this.getServer().getPluginManager().registerEvents(hub, this);
		this.getServer().getPluginManager().registerEvents(cactice, this);
		this.getServer().getPluginManager().registerEvents(spleef, this);
		this.getServer().getPluginManager().registerEvents(accounts, this);
		this.getServer().getPluginManager().registerEvents(towerDefense, this);
		this.getServer().getPluginManager().registerEvents(pixelBattles, this);
		this.getServer().getPluginManager().registerEvents(paintball, this);
	}
	
	public void registerEvents(Object object) {
		this.getServer().getPluginManager().registerEvents((Listener) object, this);
	}
	
	public void registerCommands() {
		getCommand("hub").setExecutor(commands);
		getCommand("updaterank").setExecutor(commands);
		getCommand("buildmode").setExecutor(commands);
		getCommand("coins").setExecutor(commands);
		getCommand("kit").setExecutor(commands);
		getCommand("cactice").setExecutor(commands);
		getCommand("spleef").setExecutor(commands);
		getCommand("towerdefense").setExecutor(commands);
		getCommand("kits").setExecutor(commands);
		getCommand("savespleef").setExecutor(commands);
		getCommand("resetspleef").setExecutor(commands);
		getCommand("intro").setExecutor(commands);
		getCommand("msg").setExecutor(message);
		getCommand("r").setExecutor(message);
		getCommand("addcoins").setExecutor(commands);
		getCommand("removecoins").setExecutor(commands);
		getCommand("pixelbattles").setExecutor(commands);
		
	}
	
	
}

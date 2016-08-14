package me.jake861.BenServer.PixelBattles;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.jake861.BenServer.Main;
import net.md_5.bungee.api.ChatColor;

public class Gameboard {
	
	private Main plugin;
	
	public Gameboard(Main plugin) {
		this.plugin = plugin;
	}

	ScoreboardManager manager;
	Scoreboard scoreboard;
	Objective obj;
	
	public void init() {
		
		manager = null;
		scoreboard = null;
		obj = null;
		
		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		obj = scoreboard.registerNewObjective("PixelBattles", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(plugin.pixelBattles.status == "STARTING" || plugin.pixelBattles.status == "STARTED" || plugin.pixelBattles.status == "ENDED") {
		
			obj.setDisplayName(ChatColor.AQUA + "   " + ChatColor.BOLD + "Mini" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Craft" + ChatColor.RESET + "   ");
			
			obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(15);
			obj.getScore(ChatColor.BLUE + "Completed Drawings:").setScore(14);
			
			obj.getScore(ChatColor.WHITE.toString()).setScore(13);
			
			int counter = 12;
			
			for(UUID uuid : plugin.pixelBattles.players) {
				Player player = Bukkit.getPlayer(uuid);
				obj.getScore(ChatColor.GREEN + player.getName() + ": " + ChatColor.WHITE + plugin.pixelBattles.number.get(player)).setScore(counter);
				counter--;
			}
			
			if(counter >= 2) {
				obj.getScore(ChatColor.WHITE.toString() + ChatColor.RESET.toString()).setScore(counter);
				counter--;
				obj.getScore(ChatColor.BLUE + "Drawings to Win: " + ChatColor.WHITE + "3").setScore(counter);
			}
			
			
		
		} else if(plugin.pixelBattles.status == "WAITING") {
			obj.setDisplayName(ChatColor.BLUE + "WAITING FOR PLAYERS");
			obj.getScore(ChatColor.WHITE.toString()).setScore(15);
			obj.getScore(ChatColor.AQUA + "Players: " + ChatColor.WHITE + Integer.toString(plugin.pixelBattles.players.size())).setScore(14);;
			obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
		} else if(plugin.pixelBattles.status == "COUNTING") {
			obj.setDisplayName(ChatColor.BLUE + "" + "STARTING IN " + ChatColor.AQUA + "" + Integer.toString(plugin.pixelBattles.lobbyCount) + ChatColor.BLUE + " SECONDS");
			obj.getScore(ChatColor.WHITE.toString()).setScore(15);
			obj.getScore(ChatColor.AQUA + "Players: " + ChatColor.WHITE + Integer.toString(plugin.pixelBattles.players.size())).setScore(14);;
			obj.getScore(ChatColor.WHITE.toString() + ChatColor.WHITE.toString()).setScore(13);
		}
		
		for(UUID uuid : plugin.pixelBattles.players) {
			Player player = Bukkit.getPlayer(uuid);
			player.setScoreboard(scoreboard);
		}
	}
	
	public void updateScoreboard() {
		init();
	}
	
}

package me.jake861.BenServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;

public class Scoreboards {

	private Main plugin;
	
	public Scoreboards(Main plugin) {
		this.plugin = plugin;
	}
	
	ScoreboardManager manager;
	public Map<Player, Scoreboard> lobbyBoards = new HashMap<>();
	public Map<Player, Objective> obj15 = new HashMap<>();
	
	public void init() {
		
		for(UUID uuid : plugin.hub.players) {
			Player player = Bukkit.getPlayer(uuid);
		
			lobbyBoards.put(player, null);
			manager = null;
			obj15.put(player, null);
			
			manager = plugin.getServer().getScoreboardManager();
			lobbyBoards.put(player, manager.getNewScoreboard());
			obj15.put(player, lobbyBoards.get(player).registerNewObjective("Lobby", "dummy"));
			
			
			obj15.get(player).setDisplaySlot(DisplaySlot.SIDEBAR);
			obj15.get(player).setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "      Mini" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Craft      ");
			
			Score score15 = obj15.get(player).getScore(ChatColor.WHITE.toString());
			score15.setScore(15);
			
			Score score14 = obj15.get(player).getScore(ChatColor.RED + ""  + "Total Players: " + ChatColor.WHITE + Integer.toString(Bukkit.getOnlinePlayers().size()));
			score14.setScore(14);
			
			Score score12 = obj15.get(player).getScore(ChatColor.RESET.toString()+ ChatColor.RESET.toString());
			score12.setScore(13);
			
			Score score11 = obj15.get(player).getScore("- " + ChatColor.YELLOW + ""  + "Cactice: " + ChatColor.WHITE + Integer.toString(plugin.cactice.players.size()));
			score11.setScore(12);
			
			Score score9 = obj15.get(player).getScore(ChatColor.RESET.toString()     + ChatColor.RESET.toString()+ ChatColor.RESET.toString());
			score9.setScore(11);
			
			Score score8 = obj15.get(player).getScore("- " + ChatColor.GREEN + "" +  "Spleef: " + ChatColor.WHITE + Integer.toString(plugin.spleef.players.size()));
			score8.setScore(10);
			
			Score score6 = obj15.get(player).getScore(ChatColor.RESET.toString()     + ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()    );
			score6.setScore(9);
			
			Score score5 = obj15.get(player).getScore("- " + ChatColor.AQUA + "" + "Tower Defense: " + ChatColor.WHITE + Integer.toString(plugin.towerDefense.players.size()));
			score5.setScore(8);
			
			Score score4 = obj15.get(player).getScore(ChatColor.RED + ChatColor.RESET.toString()     + ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()    );
			score4.setScore(7);
			
			Score score3 = obj15.get(player).getScore("- " + ChatColor.BLUE + "Pixel Battles: " + ChatColor.WHITE + Integer.toString(plugin.pixelBattles.players.size()));
			score3.setScore(6);
			
			Score score2 = obj15.get(player).getScore(ChatColor.RESET.toString()  + ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()+ ChatColor.RESET.toString()          );
			score2.setScore(5);
			
			Score score1 = obj15.get(player).getScore(ChatColor.LIGHT_PURPLE + "Coins: " + ChatColor.WHITE + Integer.toString(plugin.accounts.getCoins(player)));
			score1.setScore(4);
	
			
					
		}
				

	}
	
	public void updateScoreboard() {
		
		init();
		
		for(UUID uuid : plugin.hub.players) {
			Player player = Bukkit.getPlayer(uuid);
			player.setScoreboard(lobbyBoards.get(player));
		}
	
	}
	
	
}

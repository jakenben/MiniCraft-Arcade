package me.jake861.BenServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Message implements CommandExecutor {
	
	public Map<UUID, UUID> lastSent = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		

		
		if(cmd.getName().equalsIgnoreCase("msg")) {
			
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args.length <= 1) {
					player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.YELLOW + "/msg <player> <message>");
				} else {
					boolean found = false;
					Player receiver = null;
					
					List<Player> people = new ArrayList<>();
					for(Player other : Bukkit.getOnlinePlayers()) {
						if(other.getName().toLowerCase().contains(args[0].toLowerCase())) {
							found = true;
							people.add(other);
						}
					}
					
					if(people.size() == 1) {
						receiver = people.get(0);
						
						String message = "";
						
						for(int i = 1; i < args.length; i++) {
							message += args[i] + " ";
						}
						
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + "" + ChatColor.BOLD + " to " + ChatColor.GOLD + "" + ChatColor.BOLD + receiver.getName() +
								ChatColor.YELLOW + "" + ChatColor.BOLD + ": " + message);
								player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 5);
						
						
						receiver.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + "" + ChatColor.BOLD + " to " + ChatColor.GOLD + "" + ChatColor.BOLD + receiver.getName() +
						ChatColor.YELLOW + "" + ChatColor.BOLD + ": " + message);
						receiver.playSound(receiver.getLocation(), Sound.NOTE_PIANO, 1, 5);
						
						lastSent.put(player.getUniqueId(), receiver.getUniqueId());
						
						
					} else if(people.size() > 1) {
						
						String listOfPeople = "";
						
						int counter = 0;
						
						for(Player person : people) {
							counter++;
							if(counter == 1) {
									listOfPeople += person.getName();
							} else {
								listOfPeople += ChatColor.GRAY + ", " + ChatColor.YELLOW + person.getName();
							}
							
						}
						
						player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.GRAY + "There are multiple players matching \"" + args[0] + "\". [" + ChatColor.YELLOW + 
								listOfPeople.trim() + ChatColor.GRAY + "]" );
					} else {
						player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.YELLOW + "Cannot find a player named \"" + args[0] + "\"."  );
					}
				
				}
				
				
				return true;
			}
		
			
		}
		
		if(cmd.getName().equalsIgnoreCase("r")) {
			
			if(sender instanceof Player) {
				
				Player player = (Player) sender;
				
				if(args.length <= 0) {
					player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.YELLOW + "/r <message>");
				} else {
					if(lastSent.get(player.getUniqueId()) != null) {
						
						if(Bukkit.getPlayer(lastSent.get(player.getUniqueId())).isOnline()) {
						
							String message = "";
							
							for(int i = 0; i < args.length; i++) {
								message += args[i] + " ";
							}
							
							Player receiver = Bukkit.getPlayer(lastSent.get(player.getUniqueId()));
							
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + "" + ChatColor.BOLD + " to " + ChatColor.GOLD + "" + ChatColor.BOLD + receiver.getName() +
									ChatColor.YELLOW + "" + ChatColor.BOLD + ": " + message);
									player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 5);
							
							
							receiver.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + "" + ChatColor.BOLD + " to " + ChatColor.GOLD + "" + ChatColor.BOLD + receiver.getName() +
							ChatColor.YELLOW + "" + ChatColor.BOLD + ": " + message);
							receiver.playSound(receiver.getLocation(), Sound.NOTE_PIANO, 1, 5);
							
							
						} else {
							player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.YELLOW + "That player went offline." );
						}
						
						
					} else {
						player.sendMessage(ChatColor.BLUE + "[Message] " + ChatColor.YELLOW + "You haven't recently sent a message to anyone!");
					}
				}
				
				return true;
			}
			
			
		}
		
		
		return false;
	}
	
	
}

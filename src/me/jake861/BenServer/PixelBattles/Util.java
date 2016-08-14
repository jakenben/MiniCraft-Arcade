package me.jake861.BenServer.PixelBattles;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.jake861.BenServer.Main;

public class Util {
	
	private static Main plugin;
	
	public Util(Main plugin) {
		Util.plugin = plugin;
	}
	
	private static World gameWorld = Bukkit.getWorld("PixelBattles");
	private static World drawingWorld = Bukkit.getWorld("Drawings");
	private static DrawingManager dm = new DrawingManager();
	private static BuildAreaManager bam = new BuildAreaManager();
	
	public static DrawingManager getDrawingManager() {
		return dm;
	}
	
	public static BuildAreaManager getBuildAreaManager() {
		return bam;
	}
	
	public static World getGameWorld() {
		if(gameWorld == null) {
			Bukkit.createWorld(new WorldCreator("PixelBattles"));
			gameWorld = Bukkit.getWorld("PixelBattles");
		}
		
			return gameWorld;
		
		
	}
	
	public static World getDrawingWorld() {
		if(drawingWorld == null) {
			Bukkit.createWorld(new WorldCreator("Drawings"));
		}
		return drawingWorld;
	}

}


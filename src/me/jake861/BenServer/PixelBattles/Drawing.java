package me.jake861.BenServer.PixelBattles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;

public class Drawing {
	
	private String name = "";
	private Location topLeft;
	private Location bottomRight;
	private List<Location> blocks = new ArrayList<>();
	private World drawingWorld = Bukkit.getWorld("Drawings");
	
	public Drawing() {
		if(drawingWorld == null) {
			Bukkit.createWorld(new WorldCreator("Drawings"));
			drawingWorld = Bukkit.getWorld("Drawings");
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTopLeft(Location loc) {
		this.topLeft = loc;
	}
	
	public void setBottomRight(Location loc) {
		this.bottomRight = loc;
	}
	
	public void setBlocks() {
		blocks.clear();
		for(double x = topLeft.getX(); x>= bottomRight.getX(); x--)
		for(double y = topLeft.getY(); y >= bottomRight.getY(); y--) {
			Location loc = new Location(drawingWorld, x, y, topLeft.getZ());
			blocks.add(loc);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public List<Location> getBlocks() {
		return blocks;
	}
	
	public World getDrawingWorld() {
		return drawingWorld;
	}
 
}

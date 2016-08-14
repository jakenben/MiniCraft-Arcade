package me.jake861.BenServer.PixelBattles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BuildArea {
	
	private Player player = null;
	private Location viewTopLeft;
	private Location viewBottomRight;
	private Location buildTopLeft;
	private Location buildBottomRight;
	private List<Location> viewBlocks = new ArrayList<>();
	private List<Location> buildBlocks = new ArrayList<>();
	private Location playerSpawn;
	
	public void setPlayer(Player pl) {
		player = pl;
	}
	
	public void setViewTopLeft(Location loc) {
		viewTopLeft = loc;
	}
	
	public void setViewBottomRight(Location loc) {
		viewBottomRight = loc;
	}
	
	public void setBuildTopLeft(Location loc) {
		buildTopLeft = loc;
	}
	
	public void setBuildBottomRight(Location loc) {
		buildBottomRight = loc;
	}
	
	public void setPlayerSpawn(Location loc) {
		playerSpawn = loc;
	}
	
	public void setViewBlocks() {
		viewBlocks.clear();
		for(double z = viewTopLeft.getZ(); z >= viewBottomRight.getZ(); z--)
		for(double y = viewTopLeft.getY(); y >= viewBottomRight.getY(); y--) {
			Location loc = new Location(Util.getGameWorld(), viewTopLeft.getX(), y, z);
			viewBlocks.add(loc);
		}
	}
	
	public void setBuildBlocks() {
		for(double x = buildTopLeft.getX(); x >= buildBottomRight.getX(); x--)
		for(double y = buildTopLeft.getY(); y >= buildBottomRight.getY(); y--) {
			Location loc = new Location(Util.getGameWorld(), x, y, buildTopLeft.getZ());
			buildBlocks.add(loc);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<Location> getViewBlocks() {
		return viewBlocks;
	}
	
	public List<Location> getBuildBlocks() {
		return buildBlocks;
	}
	
	public Location getPlayerSpawn() {
		return playerSpawn;
	}
	
	public boolean hasPlayer() {
		if(player != null) {
			return true;
		} else {
			return false;
		}
	}
	

}

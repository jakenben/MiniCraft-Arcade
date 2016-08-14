package me.jake861.BenServer.PixelBattles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BuildAreaManager {
	
	private List<BuildArea> buildAreas = new ArrayList<>();
	
	public void createBuildAreas() {
		
		buildAreas.clear();
		
		BuildArea ba1 = new BuildArea();
		ba1.setPlayer(null);
		ba1.setBuildTopLeft(new Location(Util.getGameWorld(), 245, 10, 16));
		ba1.setBuildBottomRight(new Location(Util.getGameWorld(), 240, 5, 16));
		ba1.setViewTopLeft(new Location(Util.getGameWorld(), 237, 10, 13));
		ba1.setViewBottomRight(new Location(Util.getGameWorld(), 237, 5, 8));
		ba1.setViewBlocks();
		ba1.setBuildBlocks();
		ba1.setPlayerSpawn(new Location(Util.getGameWorld(), 245, 4, 8, 0, 45));
		
		
		BuildArea ba2 = new BuildArea();
		ba2.setPlayer(null);
		ba2.setBuildTopLeft(new Location(Util.getGameWorld(), 228, 10, 16));
		ba2.setBuildBottomRight(new Location(Util.getGameWorld(), 223, 5, 16));
		ba2.setViewTopLeft(new Location(Util.getGameWorld(), 220, 10, 13));
		ba2.setViewBottomRight(new Location(Util.getGameWorld(), 220, 5, 8));
		ba2.setViewBlocks();
		ba2.setBuildBlocks();
		ba2.setPlayerSpawn(new Location(Util.getGameWorld(), 227, 4, 9, 0, 45));
		
		BuildArea ba3 = new BuildArea();
		ba3.setPlayer(null);
		ba3.setBuildTopLeft(new Location(Util.getGameWorld(), 212, 10, 16));
		ba3.setBuildBottomRight(new Location(Util.getGameWorld(), 207, 5, 16));
		ba3.setViewTopLeft(new Location(Util.getGameWorld(), 204, 10, 13));
		ba3.setViewBottomRight(new Location(Util.getGameWorld(), 204, 5, 8));
		ba3.setViewBlocks();
		ba3.setBuildBlocks();
		ba3.setPlayerSpawn(new Location(Util.getGameWorld(), 212, 4, 8, 0, 45));
		
		BuildArea ba4 = new BuildArea();
		ba4.setPlayer(null);
		ba4.setBuildTopLeft(new Location(Util.getGameWorld(), 196, 10, 16));
		ba4.setBuildBottomRight(new Location(Util.getGameWorld(), 191, 5, 16));
		ba4.setViewTopLeft(new Location(Util.getGameWorld(), 188, 10, 13));
		ba4.setViewBottomRight(new Location(Util.getGameWorld(), 188, 5, 8));
		ba4.setViewBlocks();
		ba4.setBuildBlocks();
		ba4.setPlayerSpawn(new Location(Util.getGameWorld(), 195, 4, 9, 0, 45));
		
		BuildArea ba5 = new BuildArea();
		ba5.setPlayer(null);
		ba5.setBuildTopLeft(new Location(Util.getGameWorld(), 178, 10, 16));
		ba5.setBuildBottomRight(new Location(Util.getGameWorld(), 173, 5, 16));
		ba5.setViewTopLeft(new Location(Util.getGameWorld(), 169, 10, 13));
		ba5.setViewBottomRight(new Location(Util.getGameWorld(), 169, 5, 8));
		ba5.setViewBlocks();
		ba5.setBuildBlocks();
		ba5.setPlayerSpawn(new Location(Util.getGameWorld(), 177, 4, 9, 0, 45));
		
		BuildArea ba6 = new BuildArea();
		ba6.setPlayer(null);
		ba6.setBuildTopLeft(new Location(Util.getGameWorld(), 161, 10, 16));
		ba6.setBuildBottomRight(new Location(Util.getGameWorld(), 156, 5, 16));
		ba6.setViewTopLeft(new Location(Util.getGameWorld(), 153, 10, 13));
		ba6.setViewBottomRight(new Location(Util.getGameWorld(), 153, 5, 8));
		ba6.setViewBlocks();
		ba6.setBuildBlocks();
		ba6.setPlayerSpawn(new Location(Util.getGameWorld(), 160, 4, 9, 0, 45));
		
		BuildArea ba7 = new BuildArea();
		ba7.setPlayer(null);
		ba7.setBuildTopLeft(new Location(Util.getGameWorld(), 145, 10, 16));
		ba7.setBuildBottomRight(new Location(Util.getGameWorld(), 140, 5, 16));
		ba7.setViewTopLeft(new Location(Util.getGameWorld(), 137, 10, 13));
		ba7.setViewBottomRight(new Location(Util.getGameWorld(), 137, 5, 8));
		ba7.setViewBlocks();
		ba7.setBuildBlocks();
		ba7.setPlayerSpawn(new Location(Util.getGameWorld(), 144, 4, 9, 0, 45));
		
		BuildArea ba8 = new BuildArea();
		ba8.setPlayer(null);
		ba8.setBuildTopLeft(new Location(Util.getGameWorld(), 129, 10, 16));
		ba8.setBuildBottomRight(new Location(Util.getGameWorld(), 124, 5, 16));
		ba8.setViewTopLeft(new Location(Util.getGameWorld(), 121, 10, 13));
		ba8.setViewBottomRight(new Location(Util.getGameWorld(), 121, 5, 8));
		ba8.setViewBlocks();
		ba8.setBuildBlocks();
		ba8.setPlayerSpawn(new Location(Util.getGameWorld(), 128, 4, 9, 0, 45));
		
		BuildArea ba9 = new BuildArea();
		ba9.setPlayer(null);
		ba9.setBuildTopLeft(new Location(Util.getGameWorld(), 113, 10, 16));
		ba9.setBuildBottomRight(new Location(Util.getGameWorld(), 108, 5, 16));
		ba9.setViewTopLeft(new Location(Util.getGameWorld(), 105, 10, 13));
		ba9.setViewBottomRight(new Location(Util.getGameWorld(), 105, 5, 8));
		ba9.setViewBlocks();
		ba9.setBuildBlocks();
		ba9.setPlayerSpawn(new Location(Util.getGameWorld(), 113, 4, 9, 0, 45));
		
		BuildArea ba10 = new BuildArea();
		ba10.setPlayer(null);
		ba10.setBuildTopLeft(new Location(Util.getGameWorld(), 97, 10, 16));
		ba10.setBuildBottomRight(new Location(Util.getGameWorld(), 92, 5, 16));
		ba10.setViewTopLeft(new Location(Util.getGameWorld(), 89, 10, 13));
		ba10.setViewBottomRight(new Location(Util.getGameWorld(), 89, 5, 8));
		ba10.setViewBlocks();
		ba10.setBuildBlocks();
		ba10.setPlayerSpawn(new Location(Util.getGameWorld(), 97, 4, 9, 0, 45));
		
		BuildArea ba11 = new BuildArea();
		ba11.setPlayer(null);
		ba11.setBuildTopLeft(new Location(Util.getGameWorld(), 80, 10, 16));
		ba11.setBuildBottomRight(new Location(Util.getGameWorld(), 75, 5, 16));
		ba11.setViewTopLeft(new Location(Util.getGameWorld(), 72, 10, 13));
		ba11.setViewBottomRight(new Location(Util.getGameWorld(), 72, 5, 8));
		ba11.setViewBlocks();
		ba11.setBuildBlocks();
		ba11.setPlayerSpawn(new Location(Util.getGameWorld(), 79, 4, 9, 0, 45));
		
		BuildArea ba12 = new BuildArea();
		ba12.setPlayer(null);
		ba12.setBuildTopLeft(new Location(Util.getGameWorld(), 64, 10, 16));
		ba12.setBuildBottomRight(new Location(Util.getGameWorld(), 59, 5, 16));
		ba12.setViewTopLeft(new Location(Util.getGameWorld(), 56, 10, 13));
		ba12.setViewBottomRight(new Location(Util.getGameWorld(), 56, 5, 8));
		ba12.setViewBlocks();
		ba12.setBuildBlocks();
		ba12.setPlayerSpawn(new Location(Util.getGameWorld(), 64, 4, 9, 0, 45));
		
		buildAreas.add(ba1);
		buildAreas.add(ba2);
		buildAreas.add(ba3);
		buildAreas.add(ba4);
		buildAreas.add(ba5);
		buildAreas.add(ba6);
		buildAreas.add(ba7);
		buildAreas.add(ba8);
		buildAreas.add(ba9);
		buildAreas.add(ba10);
		buildAreas.add(ba11);
		buildAreas.add(ba12);
		
		
	}
	
	public void assignBuildArea(List<UUID> players) {
		int counter = 0;
		for(UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			buildAreas.get(counter).setPlayer(player);
			counter++;
 		}
	}
	
	public void clearBuildAreas() {
		for(BuildArea ba : buildAreas) {
			ba.setPlayer(null);
		}
	}
	
	public BuildArea getBuildArea(Player player) {
		BuildArea playerBa = null;
		boolean found = false;
		for(BuildArea ba : buildAreas) {
			if(ba.getPlayer() == player) {
				playerBa = ba;
				found = true;
			}
		}
		if(found) {
			return playerBa;
		} else {
			return null;
		}
		
	}

}

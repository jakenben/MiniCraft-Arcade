package me.jake861.BenServer.PixelBattles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class DrawingManager {
	
	private List<Drawing> drawings = new ArrayList<>();
	
	public void createDrawings() {
		
		drawings.clear();
		
		Drawing drawing1 = new Drawing();
		drawing1.setName("Bucktooth Piggy");
		drawing1.setTopLeft(new Location(drawing1.getDrawingWorld(), 18, 9, 5));
		drawing1.setBottomRight(new Location(drawing1.getDrawingWorld(), 13, 4, 5));
		drawing1.setBlocks();
		
		Drawing drawing2 = new Drawing();
		drawing2.setName("Apple");
		drawing2.setTopLeft(new Location(drawing2.getDrawingWorld(), -1, 9, 1));
		drawing2.setBottomRight(new Location(drawing2.getDrawingWorld(), -6, 4, 1));
		drawing2.setBlocks();
		
		Drawing drawing3 = new Drawing();
		drawing3.setName("Smiley Face");
		drawing3.setTopLeft(new Location(drawing3.getDrawingWorld(), -22, 9, 5));
		drawing3.setBottomRight(new Location(drawing3.getDrawingWorld(), -27, 4, 5));
		drawing3.setBlocks();
		
		Drawing drawing4 = new Drawing();
		drawing4.setName("Spongebob");
		drawing4.setTopLeft(new Location(drawing4.getDrawingWorld(), -33, 9, 5));
		drawing4.setBottomRight(new Location(drawing4.getDrawingWorld(), -38, 4, 5));
		drawing4.setBlocks();
		
		Drawing drawing5 = new Drawing();
		drawing5.setName("Natural Forest");
		drawing5.setTopLeft(new Location(drawing5.getDrawingWorld(), -44, 9, 5));
		drawing5.setBottomRight(new Location(drawing5.getDrawingWorld(), -49, 4, 5));
		drawing5.setBlocks();
		
		
		Drawing drawing7 = new Drawing();
		drawing7.setName("Crown");
		drawing7.setTopLeft(new Location(drawing7.getDrawingWorld(), -55, 9, 5));
		drawing7.setBottomRight(new Location(drawing7.getDrawingWorld(), -60, 4, 5));
		drawing7.setBlocks();
		
		Drawing drawing8 = new Drawing();
		drawing8.setName("Baby Jake");
		drawing8.setTopLeft(new Location(drawing8.getDrawingWorld(), -33, 9, -3));
		drawing8.setBottomRight(new Location(drawing8.getDrawingWorld(), -38, 4, -3));
		drawing8.setBlocks();
		
		
		Drawing drawing9 = new Drawing();
		drawing9.setName("Wood");
		drawing9.setTopLeft(new Location(drawing9.getDrawingWorld(), -47, 9, -5));
		drawing9.setBottomRight(new Location(drawing9.getDrawingWorld(), -52, 4, -5));
		drawing9.setBlocks();
		
		
		Drawing drawing10 = new Drawing();
		drawing10.setName("Nice");
		drawing10.setTopLeft(new Location(drawing7.getDrawingWorld(), -47, 9, -9));
		drawing10.setBottomRight(new Location(drawing7.getDrawingWorld(), -52, 4, -9));
		drawing10.setBlocks();
		
		Drawing drawing11 = new Drawing();
		drawing11.setName("Ores");
		drawing11.setTopLeft(new Location(drawing11.getDrawingWorld(), -24, 9, -11));
		drawing11.setBottomRight(new Location(drawing11.getDrawingWorld(), -29, 4, -11));
		drawing11.setBlocks();
		
		Drawing drawing12 = new Drawing();
		drawing12.setName("Wolf");
		drawing12.setTopLeft(new Location(drawing12.getDrawingWorld(), 2, 9, -4));
		drawing12.setBottomRight(new Location(drawing12.getDrawingWorld(), -3, 4, -4));
		drawing12.setBlocks();
		
		drawings.add(drawing1);
		drawings.add(drawing2);
		drawings.add(drawing3);
		drawings.add(drawing4);
		drawings.add(drawing5);
		drawings.add(drawing7);
		drawings.add(drawing8);
		drawings.add(drawing9);
		drawings.add(drawing10);
		drawings.add(drawing11);
		drawings.add(drawing12);
		
		
	}
	
	public List<Drawing> getDrawings() {
		return drawings;
	}

}

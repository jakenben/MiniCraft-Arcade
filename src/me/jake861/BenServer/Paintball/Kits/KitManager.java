package me.jake861.BenServer.Paintball.Kits;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitManager {
	
	private Map<UUID, String> kit = new HashMap<>();

	public void setKit(UUID uuid, String kit) {
		this.kit.put(uuid, kit);
	}
	
	public String getKit(UUID uuid) {
		return kit.get(uuid);
	}
	
	public void equipItems() {
		
		
		
		
	}

}

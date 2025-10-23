package fr.eradium.eramod.client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.eradium.eramod.EramodMod;

public class PlayerSkinCache {
	private static final Map<String, CachedPlayerData> CACHE = new HashMap<>();
	
	public static class CachedPlayerData {
		private final UUID uuid;
		private final String name;
		private final String textureBase64;
		private final String model;
		
		public CachedPlayerData(UUID uuid, String name, String textureBase64, String model) {
			this.uuid = uuid;
			this.name = name;
			this.textureBase64 = textureBase64;
			this.model = model;
		}
		
		public UUID getUuid() {
			return uuid;
		}
		
		public String getName() {
			return name;
		}
		
		public String getTextureBase64() {
			return textureBase64;
		}
		
		public String getModel() {
			return model;
		}
	}
	
	/**
	 * Get cached player data by name (case-insensitive)
	 */
	public static CachedPlayerData get(String playerName) {
		return CACHE.get(playerName.toLowerCase());
	}
	
	/**
	 * Store player data in cache
	 */
	public static void put(String playerName, UUID uuid, String textureBase64, String model) {
		CACHE.put(playerName.toLowerCase(), new CachedPlayerData(uuid, playerName, textureBase64, model));
		EramodMod.LOGGER.info("[PlayerSkinCache] Cached skin data for: {}", playerName);
	}
	
	/**
	 * Check if player data is cached
	 */
	public static boolean has(String playerName) {
		return CACHE.containsKey(playerName.toLowerCase());
	}
	
	/**
	 * Clear the entire cache
	 */
	public static void clear() {
		CACHE.clear();
		EramodMod.LOGGER.info("[PlayerSkinCache] Cache cleared");
	}
	
	/**
	 * Get cache size
	 */
	public static int size() {
		return CACHE.size();
	}
}

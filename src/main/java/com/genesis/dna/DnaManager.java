// dna/DnaManager.java
package com.genesis.dna;

import com.genesis.Genesis;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DnaManager {

    private final Genesis plugin;
    private final Map<UUID, Set<DnaType>> playerDna = new HashMap<>();
    private final Map<UUID, Map<DnaType, Long>> cooldowns = new HashMap<>();
    private final int MAX_DNA = 3;
    private final long ABILITY_COOLDOWN = 60000; // 60 seconds in milliseconds
    private File dataFile;
    private FileConfiguration dataConfig;

    public DnaManager(Genesis plugin) {
        this.plugin = plugin;
        setupDataFile();
    }

    private void setupDataFile() {
        dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        if (dataConfig.contains("players")) {
            for (String uuidStr : dataConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                List<String> dnaList = dataConfig.getStringList("players." + uuidStr + ".dna");
                Set<DnaType> dnaSet = new HashSet<>();
                for (String dnaName : dnaList) {
                    try {
                        dnaSet.add(DnaType.valueOf(dnaName));
                    } catch (IllegalArgumentException ignored) {}
                }
                playerDna.put(uuid, dnaSet);
            }
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, Set<DnaType>> entry : playerDna.entrySet()) {
            List<String> dnaNames = new ArrayList<>();
            for (DnaType dna : entry.getValue()) {
                dnaNames.add(dna.name());
            }
            dataConfig.set("players." + entry.getKey().toString() + ".dna", dnaNames);
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<DnaType> getPlayerDna(Player player) {
        return playerDna.getOrDefault(player.getUniqueId(), new HashSet<>());
    }

    public boolean addDna(Player player, DnaType dnaType) {
        Set<DnaType> dna = playerDna.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        
        if (dna.contains(dnaType)) {
            return false; // Already has this DNA
        }
        
        if (dna.size() >= MAX_DNA) {
            return false; // At max capacity
        }
        
        dna.add(dnaType);
        saveData();
        return true;
    }

    public boolean removeDna(Player player, DnaType dnaType) {
        Set<DnaType> dna = playerDna.get(player.getUniqueId());
        if (dna != null && dna.remove(dnaType)) {
            saveData();
            return true;
        }
        return false;
    }

    public boolean hasDna(Player player, DnaType dnaType) {
        Set<DnaType> dna = playerDna.get(player.getUniqueId());
        return dna != null && dna.contains(dnaType);
    }

    public int getDnaCount(Player player) {
        Set<DnaType> dna = playerDna.get(player.getUniqueId());
        return dna != null ? dna.size() : 0;
    }

    public int getMaxDna() {
        return MAX_DNA;
    }

    public boolean isOnCooldown(Player player, DnaType dnaType) {
        Map<DnaType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return false;
        
        Long lastUse = playerCooldowns.get(dnaType);
        if (lastUse == null) return false;
        
        return System.currentTimeMillis() - lastUse < ABILITY_COOLDOWN;
    }

    public long getRemainingCooldown(Player player, DnaType dnaType) {
        Map<DnaType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return 0;
        
        Long lastUse = playerCooldowns.get(dnaType);
        if (lastUse == null) return 0;
        
        long remaining = ABILITY_COOLDOWN - (System.currentTimeMillis() - lastUse);
        return Math.max(0, remaining);
    }

    public void setCooldown(Player player, DnaType dnaType) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                 .put(dnaType, System.currentTimeMillis());
    }
}


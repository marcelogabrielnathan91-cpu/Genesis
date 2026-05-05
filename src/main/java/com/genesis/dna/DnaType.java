// dna/DnaType.java
package com.genesis.dna;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum DnaType {
    ZOMBIE(EntityType.ZOMBIE, Material.ROTTEN_FLESH, ChatColor.DARK_GREEN, 
           "Undying Rage", "Gain Strength II and Resistance I for 15 seconds"),
    
    SKELETON(EntityType.SKELETON, Material.BONE, ChatColor.WHITE, 
             "Dead Eye", "Gain infinite arrows and +50% arrow damage for 20 seconds"),
    
    ENDERMAN(EntityType.ENDERMAN, Material.ENDER_PEARL, ChatColor.DARK_PURPLE, 
             "Phase Shift", "Teleport 15 blocks in the direction you're looking"),
    
    CREEPER(EntityType.CREEPER, Material.GUNPOWDER, ChatColor.GREEN, 
            "Volatile", "Create an explosion that damages enemies but not you"),
    
    WITHER(EntityType.WITHER, Material.NETHER_STAR, ChatColor.DARK_GRAY, 
           "Soul Drain", "Drain health from nearby enemies, healing yourself"),
    
    ENDER_DRAGON(EntityType.ENDER_DRAGON, Material.DRAGON_BREATH, ChatColor.LIGHT_PURPLE, 
                 "Dragon's Wrath", "Breathe dragon fire in front of you"),
    
    BLAZE(EntityType.BLAZE, Material.BLAZE_POWDER, ChatColor.GOLD, 
          "Inferno", "Launch a barrage of fireballs and become fire immune for 30 seconds"),
    
    PIGLIN(EntityType.PIGLIN, Material.GOLD_INGOT, ChatColor.YELLOW, 
           "Golden Fortune", "Nearby mobs drop gold and you gain Luck III for 60 seconds");

    private final EntityType entityType;
    private final Material displayMaterial;
    private final ChatColor color;
    private final String abilityName;
    private final String abilityDescription;

    DnaType(EntityType entityType, Material displayMaterial, ChatColor color, 
            String abilityName, String abilityDescription) {
        this.entityType = entityType;
        this.displayMaterial = displayMaterial;
        this.color = color;
        this.abilityName = abilityName;
        this.abilityDescription = abilityDescription;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Material getDisplayMaterial() {
        return displayMaterial;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public String getAbilityDescription() {
        return abilityDescription;
    }

    public String getDisplayName() {
        return color + name().replace("_", " ") + " DNA";
    }

    public static DnaType fromEntityType(EntityType type) {
        for (DnaType dna : values()) {
            if (dna.getEntityType() == type) {
                return dna;
            }
        }
        return null;
    }
}

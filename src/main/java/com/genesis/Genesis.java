// Genesis.java
package com.genesis;

import com.genesis.commands.DnaCommand;
import com.genesis.dna.DnaManager;
import com.genesis.items.DnaTransplanter;
import com.genesis.items.SoulTranquilizer;
import com.genesis.listeners.*;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Genesis extends JavaPlugin {

    private static Genesis instance;
    private DnaManager dnaManager;
    
    // Custom item keys
    public static NamespacedKey DNA_TRANSPLANTER_KEY;
    public static NamespacedKey SOUL_TRANQUILIZER_KEY;
    public static NamespacedKey DNA_ITEM_KEY;
    public static NamespacedKey DNA_TYPE_KEY;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize keys
        DNA_TRANSPLANTER_KEY = new NamespacedKey(this, "dna_transplanter");
        SOUL_TRANQUILIZER_KEY = new NamespacedKey(this, "soul_tranquilizer");
        DNA_ITEM_KEY = new NamespacedKey(this, "dna_item");
        DNA_TYPE_KEY = new NamespacedKey(this, "dna_type");
        
        // Initialize managers
        dnaManager = new DnaManager(this);
        
        // Register recipes
        DnaTransplanter.registerRecipe(this);
        SoulTranquilizer.registerRecipe(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        
        // Register commands
        getCommand("dna").setExecutor(new DnaCommand(this));
        getCommand("dna").setTabCompleter(new DnaCommand(this));
        
        saveDefaultConfig();
        dnaManager.loadData();
        
        getLogger().info("Genesis SMP Plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (dnaManager != null) {
            dnaManager.saveData();
        }
        getLogger().info("Genesis SMP Plugin disabled!");
    }

    public static Genesis getInstance() {
        return instance;
    }

    public DnaManager getDnaManager() {
        return dnaManager;
    }
}


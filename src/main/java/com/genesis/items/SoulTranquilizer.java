// items/SoulTranquilizer.java
package com.genesis.items;

import com.genesis.Genesis;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class SoulTranquilizer {

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Soul Tranquilizer");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Extracts DNA from slain creatures",
            "",
            ChatColor.LIGHT_PURPLE + "The blade hums with",
            ChatColor.LIGHT_PURPLE + "otherworldly energy...",
            "",
            ChatColor.DARK_AQUA + "Kill mobs to harvest their DNA"
        ));
        
        meta.getPersistentDataContainer().set(
            Genesis.SOUL_TRANQUILIZER_KEY, 
            PersistentDataType.BOOLEAN, 
            true
        );
        
        item.setItemMeta(meta);
        return item;
    }

    public static void registerRecipe(Genesis plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "soul_tranquilizer_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, create());
        
        recipe.shape(" D ", " I ", " S ");
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);
        
        plugin.getServer().addRecipe(recipe);
    }

    public static boolean isTranquilizer(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(Genesis.SOUL_TRANQUILIZER_KEY, PersistentDataType.BOOLEAN);
    }
}


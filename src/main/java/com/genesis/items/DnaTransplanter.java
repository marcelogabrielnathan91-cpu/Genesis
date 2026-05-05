// items/DnaTransplanter.java
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

public class DnaTransplanter {

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "DNA Transplanter");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to open the DNA interface",
            "",
            ChatColor.DARK_PURPLE + "Merge the essence of mobs",
            ChatColor.DARK_PURPLE + "into your very being...",
            "",
            ChatColor.YELLOW + "Max DNA Slots: " + ChatColor.WHITE + "3"
        ));
        
        meta.getPersistentDataContainer().set(
            Genesis.DNA_TRANSPLANTER_KEY, 
            PersistentDataType.BOOLEAN, 
            true
        );
        
        item.setItemMeta(meta);
        return item;
    }

    public static void registerRecipe(Genesis plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "dna_transplanter_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, create());
        
        recipe.shape("IRI", "RDR", "IRI");
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('D', Material.DIAMOND);
        
        plugin.getServer().addRecipe(recipe);
    }

    public static boolean isTransplanter(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(Genesis.DNA_TRANSPLANTER_KEY, PersistentDataType.BOOLEAN);
    }
}


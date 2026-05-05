// gui/DnaGui.java
package com.genesis.gui;

import com.genesis.Genesis;
import com.genesis.dna.DnaManager;
import com.genesis.dna.DnaType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DnaGui {

    public static final String GUI_TITLE = ChatColor.DARK_AQUA + "✧ DNA Transplanter ✧";

    public static Inventory createGui(Player player, Genesis plugin) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);
        D


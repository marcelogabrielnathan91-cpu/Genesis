// dna/DnaAbilities.java
package com.genesis.dna;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import com.genesis.Genesis;

public class DnaAbilities {

    public static void activateAbility(Player player, DnaType dnaType) {
        switch (dnaType) {
            case ZOMBIE -> activateZombie(player);
            case SKELETON -> activateSkeleton(player);
            case ENDERMAN -> activateEnderman(player);
            case CREEPER -> activateCreeper(player);
            case WITHER -> activateWither(player);
            case ENDER_DRAGON -> activateEnderDragon(player);
            case BLAZE -> activateBlaze(player);
            case PIGLIN -> activatePiglin(player);
        }
    }

    // Zombie: Gain Strength II and Resistance I for 15 seconds
    private static void activateZombie(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 300, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 300, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5);
        player.sendMessage(ChatColor.DARK_GREEN + "⚔ Undying Rage activated! You feel immense power!");
    }

    // Skeleton: Gain infinite arrows effect and arrow damage boost
    private static void activateSkeleton(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 0));
        // Visual indicator - glowing effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 400, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SKELETON_AMBIENT, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5);
        player.sendMessage(ChatColor.WHITE + "🎯 Dead Eye activated! Your aim is true!");
        
        // Give arrows if they don't have any
        if (!player.getInventory().contains(Material.ARROW)) {
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.ARROW, 64));
        }
    }

    // Enderman: Teleport 15 blocks in look direction
    private static void activateEnderman(Player player) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection().normalize();
        Location target = loc.clone();
        
        // Find safe teleport location up to 15 blocks
        for (int i = 1; i <= 15; i++) {
            Location check = loc.clone().add(direction.clone().multiply(i));
            if (check.getBlock().getType().isSolid()) {
                break;
            }
            target = check;
        }
        
        // Ensure safe landing
        target.setY(target.getBlockY());
        while (!target.getBlock().getType().isAir() && target.getBlockY() < 320) {
            target.add(0, 1, 0);
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.5, 1, 0.5);
        player.teleport(target);
        player.getWorld().playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.PORTAL, target, 50, 0.5, 1, 0.5);
        player.sendMessage(ChatColor.DARK_PURPLE + "✦ Phase Shift! You bend through space!");
    }

    // Creeper: Create explosion that doesn't hurt you
    private static void activateCreeper(Player player) {
        Location loc = player.getLocation();
        player.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation().add(0, 1, 0), 10, 0.3, 0.3, 0.3);
                
                if (ticks >= 30) { // 1.5 seconds
                    Location explosionLoc = player.getLocation();
                    
                    // Damage nearby entities but not the player
                    for (Entity entity : player.getNearbyEntities(4, 4, 4)) {
                        if (entity instanceof LivingEntity living && entity != player) {
                            living.damage(10.0, player);
                            Vector knockback = entity.getLocation().toVector()
                                .subtract(explosionLoc.toVector()).normalize().multiply(1.5);
                            entity.setVelocity(knockback);
                        }
                    }
                    
                    player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, explosionLoc, 1);
                    player.getWorld().playSound(explosionLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GREEN + "💥 Volatile explosion released!");
                    cancel();
                }
            }
        }.runTaskTimer(Genesis.getInstance(), 0L, 1L);
    }

    // Wither: Drain health from nearby enemies
    private static void activateWither(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 100 || !player.isOnline()) { // 5 seconds
                    cancel();
                    return;
                }
                
                double totalDrained = 0;
                for (Entity entity : player.getNearbyEntities(6, 6, 6)) {
                    if (entity instanceof LivingEntity living && !(entity instanceof Player)) {
                        living.damage(2.0, player);
                        living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 0));
                        totalDrained += 1;
                        
                        // Visual effect
                        Location from = living.getLocation().add(0, 1, 0);
                        Location to = player.getLocation().add(0, 1, 0);
                        Vector direction = to.toVector().subtract(from.toVector()).normalize();
                        for (double i = 0; i < from.distance(to); i += 0.5) {
                            Location particle = from.clone().add(direction.clone().multiply(i));
                            player.getWorld().spawnParticle(Particle.SOUL, particle, 1, 0, 0, 0, 0);
                        }
                    }
                }
                
                if (totalDrained > 0) {
                    player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + totalDrained));
                }
                
                ticks += 20;
            }
        }.runTaskTimer(Genesis.getInstance(), 0L, 20L);
        
        player.sendMessage(ChatColor.DARK_GRAY + "☠ Soul Drain activated! Feeding on nearby souls...");
    }

    // Ender Dragon: Breathe dragon fire
    private static void activateEnderDragon(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        
        Vector direction = player.getLocation().getDirection().normalize();
        Location start = player.getEyeLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 20) {
                    cancel();
                    return;
                }
                
                for (int i = 1; i <= 10; i++) {
                    Location loc = start.clone().add(direction.clone().multiply(i));
                    player.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc, 5, 0.2, 0.2, 0.2);
                    
                    for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                        if (entity instanceof LivingEntity living && entity != player) {
                            living.damage(4.0, player);
                            living.setFireTicks(60);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(Genesis.getInstance(), 0L, 2L);
        
        player.sendMessage(ChatColor.LIGHT_PURPLE + "🐲 Dragon's Wrath! Feel the dragon's breath!");
    }

    // Blaze: Launch fireballs and become fire immune
    private static void activateBlaze(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 1.0f);
        
        new BukkitRunnable() {
            int fireballs = 0;
            @Override
            public void run() {
                if (fireballs >= 5 || !player.isOnline()) {
                    cancel();
                    return;
                }
                
                Vector direction = player.getLocation().getDirection();
                Fireball fireball = player.getWorld().spawn(
                    player.getEyeLocation().add(direction), 
                    Fireball.class
                );
                fireball.setDirection(direction);
                fireball.setShooter(player);
                fireball.setYield(1.5f);
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
                fireballs++;
            }
        }.runTaskTimer(Genesis.getInstance(), 0L, 10L);
        
        player.sendMessage(ChatColor.GOLD + "🔥 Inferno activated! Fire cannot harm you!");
    }

    // Piglin: Nearby mobs drop gold, gain Luck
    private static void activatePiglin(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1200, 2));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, 1.0f, 1.0f);
        
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                entity.getWorld().dropItemNaturally(
                    entity.getLocation(), 
                    new org.bukkit.inventory.ItemStack(Material.GOLD_NUGGET, (int)(Math.random() * 3) + 1)
                );
                entity.getWorld().spawnParticle(Particle.WAX_OFF, entity.getLocation().add(0, 1, 0), 10, 0.3, 0.3, 0.3);
            }
        }
        
        player.sendMessage(ChatColor.YELLOW + "✨ Golden Fortune! Riches surround you!");
    }
}


package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DuckSwordListener implements Listener {

    private static final int DUCKLING_COUNT = 4;
    private static final double TARGET_RADIUS = 10.0;
    private final Main plugin;

    public DuckSwordListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isDuckSword(item)) {
            return;
        }

        Location origin = event.getEntity().getLocation();
        Entity target = event.getEntity();
        spawnDucklings(origin, target);
    }

    private boolean isDuckSword(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(DuckCommand.getDuckKey(), PersistentDataType.BOOLEAN)
                && Boolean.TRUE.equals(container.get(DuckCommand.getDuckKey(), PersistentDataType.BOOLEAN));
    }

    private void spawnDucklings(Location origin, Entity preferredTarget) {
        for (int i = 0; i < DUCKLING_COUNT; i++) {
            Location loc = origin.clone().add(
                    (Math.random() - 0.5) * 2.0,
                    0.5,
                    (Math.random() - 0.5) * 2.0
            );

            Chicken duckling = (Chicken) origin.getWorld().spawnEntity(loc, EntityType.CHICKEN);
            duckling.setBaby();
            duckling.customName(Component.text("Duckling").color(NamedTextColor.YELLOW));
            duckling.setCustomNameVisible(true);
            duckling.setVelocity(new Vector((Math.random() - 0.5) * 0.8, 0.4, (Math.random() - 0.5) * 0.8));
            duckling.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, 200, 1, false, false
            ));

            if (duckling instanceof Mob ducklingMob) {
                Entity target = chooseTarget(duckling, preferredTarget);
                if (target instanceof Mob targetMob) {
                    ducklingMob.setTarget(targetMob);
                }
            }
        }
    }

    private Entity chooseTarget(Chicken duckling, Entity preferredTarget) {
        if (preferredTarget instanceof Monster) {
            return preferredTarget;
        }
        double closestDistance = Double.MAX_VALUE;
        Entity closest = null;
        for (Entity entity : duckling.getNearbyEntities(TARGET_RADIUS, TARGET_RADIUS, TARGET_RADIUS)) {
            if (entity instanceof Monster && entity.isValid()) {
                double distance = entity.getLocation().distanceSquared(duckling.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = entity;
                }
            }
        }
        return closest;
    }
}

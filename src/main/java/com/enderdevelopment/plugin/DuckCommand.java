package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public class DuckCommand implements CommandExecutor {

    private static final NamespacedKey DUCK_KEY = new NamespacedKey(Main.getInstance(), "duck_sword");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can hold the Duck Sword!")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (!sender.hasPermission("duck.sword")) {
            sender.sendMessage(Component.text("You don't have permission to use the Duck Sword.")
                    .color(NamedTextColor.RED));
            return true;
        }

        player.getInventory().addItem(createDuckSword());

        player.sendMessage(Component.text("Quack! You now wield the Duck Sword.")
                .color(NamedTextColor.GOLD));

        return true;
    }

    public static ItemStack createDuckSword() {
        ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.displayName(Component.text("Duck Sword")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(java.util.List.of(
                Component.text("A sword shaped like a duck.").color(NamedTextColor.YELLOW),
                Component.text("Releases ducklings when you strike!").color(NamedTextColor.AQUA)
        ));

        Enchantment sharpness = Registry.ENCHANTMENT.get(NamespacedKey.minecraft("sharpness"));
        if (sharpness != null) {
            meta.addEnchant(sharpness, 3, true);
        }

        meta.getPersistentDataContainer().set(DUCK_KEY, org.bukkit.persistence.PersistentDataType.BOOLEAN, true);

        sword.setItemMeta(meta);
        return sword;
    }

    public static NamespacedKey getDuckKey() {
        return DUCK_KEY;
    }
}

package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("duck").setExecutor(new DuckCommand());
        getServer().getPluginManager().registerEvents(new DuckSwordListener(this), this);

        getLogger().info("Duck Sword plugin enabled. Quack!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Duck Sword plugin disabled.");
    }

    public static Main getInstance() {
        return instance;
    }
}

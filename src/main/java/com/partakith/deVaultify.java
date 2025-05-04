package com.partakith;

import org.bukkit.plugin.java.JavaPlugin;

public class deVaultify extends JavaPlugin {

    private static deVaultify instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getLogger().info("deVaultify loaded.");
        BalanceManager.loadBalances();
    }

    @Override
    public void onDisable() {
        BalanceManager.saveBalances();
        getLogger().info("deVaultify disabled.");
    }

    public static deVaultify getInstance() {
        return instance;
    }
}
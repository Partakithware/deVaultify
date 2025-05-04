package com.partakith;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class BalanceManager {

    private static File balanceFile;
    private static FileConfiguration balanceConfig;
    private static final HashMap<UUID, Double> balances = new HashMap<>();

    public static void loadBalances() {
        balanceFile = new File(deVaultify.getInstance().getDataFolder(), "player-balances.yml");
        if (!balanceFile.exists()) {
            try {
                balanceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        balanceConfig = YamlConfiguration.loadConfiguration(balanceFile);

        for (String key : balanceConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double balance = balanceConfig.getDouble(key);
            balances.put(uuid, balance);
        }
    }

    public static void saveBalances() {
        for (UUID uuid : balances.keySet()) {
            balanceConfig.set(uuid.toString(), balances.get(uuid));
        }

        try {
            balanceConfig.save(balanceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public static boolean withdraw(Player player, double amount) {
        double current = getBalance(player);
        if (current >= amount) {
            balances.put(player.getUniqueId(), current - amount);
            return true;
        }
        return false;
    }

    public static void deposit(Player player, double amount) {
        double current = getBalance(player);
        balances.put(player.getUniqueId(), current + amount);
    }
}
package com.partakith;

import org.bukkit.entity.Player;

public class deVaultAPI {

    public static double getBalance(Player player) {
        return BalanceManager.getBalance(player);
    }

    public static boolean withdraw(Player player, double amount) {
        return BalanceManager.withdraw(player, amount);
    }

    public static void deposit(Player player, double amount) {
        BalanceManager.deposit(player, amount);
    }
}
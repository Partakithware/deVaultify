The goal was to make a simple economy only non vault plugin that uses yml. 

This is so that other plugins could hook into it instead of relying on vault.

Another goal was also to make the use case, simple and easy to utilize.

I imagine vault could be ran alongside this...but I highly recommend you don't try such unless you need to. 

The management systems are separate so vault currency is not deVault related;

The API is set to recieve updates, I will be attempting to think of new things I would like to add (or is requested) and fix it up more and more over time.

Example Usage for a creating a basic ecnonomy using deVaultify v0.0.4 (SNAPSHOT 0.0.1) (Github was giving me trouble so I had to update version number)


In your plugins POM:
Make sure to include the repo and set it up in dependancies
```
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/partakithware/devaultify</url>
  </repository
```
```
    <dependency>
        <groupId>com.github.partakithware</groupId>
        <artifactId>devaultify</artifactId>
        <version>0.0.4</version>
    </dependency>
```

Basic Example Class for an economy using deVaultAPI:

```
package yourpackagename;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.partakithware.deVaultAPI;
import com.github.partakithware.BalanceManager;

public class deVaultEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("deVaultEconomy enabled.");
        
        // Save balances every 5 minutes (6000 ticks)
        getServer().getScheduler().runTaskTimer(this, () -> {
            BalanceManager.saveBalances();
            getLogger().info("Auto-saved player balances.");
        }, 6000L, 6000L);
    }

    @Override
    public void onDisable() {
        getLogger().info("deVaultEconomy disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("bal") || cmd.equals("balance")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command is for players only.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("devault.balance")) {
                player.sendMessage("§cYou do not have permission to view your balance.");
                return true;
            }
            double balance = deVaultAPI.getBalance(player);
            player.sendMessage("§aYour balance is: §6" + balance);
            return true;
        }

        if (cmd.equals("pay")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command is for players only.");
                return true;
            }
            Player senderPlayer = (Player) sender;
            if (!senderPlayer.hasPermission("devault.pay")) {
                senderPlayer.sendMessage("§cYou do not have permission to use /pay.");
                return true;
            }
            if (args.length != 2) {
                senderPlayer.sendMessage("§cUsage: /pay <player> <amount>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                senderPlayer.sendMessage("§cPlayer not found.");
                return true;
            }

            if (target.equals(senderPlayer)) {
                senderPlayer.sendMessage("§cYou can't pay yourself.");
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                senderPlayer.sendMessage("§cPlease enter a valid positive amount.");
                return true;
            }

            if (!deVaultAPI.withdraw(senderPlayer, amount)) {
                senderPlayer.sendMessage("§cYou do not have enough money.");
                return true;
            }

            deVaultAPI.deposit(target, amount);
            senderPlayer.sendMessage("§aYou paid §6" + target.getName() + " §a" + amount);
            target.sendMessage("§aYou received §6" + amount + " §afrom §e" + senderPlayer.getName());
            
            return true;
        }

        if (cmd.equals("deposit")) {
            if (!sender.hasPermission("devault.deposit")) {
                sender.sendMessage("§cYou do not have permission to use /deposit.");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage("§cUsage: /deposit <player> <amount>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                sender.sendMessage("§cPlease enter a valid positive amount.");
                return true;
            }

            deVaultAPI.deposit(target, amount);
            sender.sendMessage("§aDeposited §6" + amount + " §ato §e" + target.getName());
            target.sendMessage("§aYou received §6" + amount + " §afrom admin.");
            return true;
        }

        return false;
    }
}
```

This basic example is made to get the point across. You could create more advanced setups of course. There are also updates coming that will add more features to be used.

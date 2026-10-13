package ir.Greenbot.greenstats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    private final GreenStats plugin;
    private final StatsManager statsManager;

    public AdminCommand(GreenStats plugin, StatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("greenstats.admin")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getConfig()
                    .getString("messages.no-permission", "&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getPrefix() + "&eUsage: /greenstats reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            statsManager.load();
            sender.sendMessage(plugin.getPrefix() + plugin.getConfig()
                    .getString("messages.reloaded", "&aConfig reloaded!"));
            return true;
        }

        sender.sendMessage(plugin.getPrefix() + "&eUsage: /greenstats reload");
        return true;
    }
}

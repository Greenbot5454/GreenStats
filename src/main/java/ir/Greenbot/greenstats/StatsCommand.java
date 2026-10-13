package ir.Greenbot.greenstats;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    private final GreenStats plugin;
    private final StatsManager statsManager;

    public StatsCommand(GreenStats plugin, StatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("greenstats.stats")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        String targetName;
        UUID targetUuid;

        if (args.length > 0) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.getName() == null && !target.hasPlayedBefore()) {
                sender.sendMessage(plugin.getMessage("player-not-found"));
                return true;
            }
            targetName = target.getName() != null ? target.getName() : args[0];
            targetUuid = target.getUniqueId();
        } else {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(plugin.getMessage("prefix") + "§cConsole must specify a player!");
                return true;
            }
            targetName = p.getName();
            targetUuid = p.getUniqueId();
        }

        StatsData data = statsManager.getStats(targetUuid);

        sender.sendMessage(plugin.getMessage("stats-header", "{player}", targetName));
        sender.sendMessage(plugin.getMessage("stats-kills", "{kills}", String.valueOf(data.getKills())));
        sender.sendMessage(plugin.getMessage("stats-deaths", "{deaths}", String.valueOf(data.getDeaths())));
        sender.sendMessage(plugin.getMessage("stats-kdr", "{kdr}", String.valueOf(data.getKdr())));
        sender.sendMessage(plugin.getMessage("stats-streak", "{streak}", String.valueOf(data.getCurrentStreak())));
        sender.sendMessage(plugin.getMessage("stats-best-streak", "{best_streak}", String.valueOf(data.getBestStreak())));
        sender.sendMessage(plugin.getMessage("stats-footer"));

        return true;
    }
}

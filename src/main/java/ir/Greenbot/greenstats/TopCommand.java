package ir.Greenbot.greenstats;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TopCommand implements CommandExecutor {

    private final GreenStats plugin;
    private final StatsManager statsManager;

    public TopCommand(GreenStats plugin, StatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("greenstats.top")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        List<Map.Entry<UUID, StatsData>> top = statsManager.getTopKillers(10);

        sender.sendMessage(plugin.getMessage("top-header"));

        int rank = 1;
        for (Map.Entry<UUID, StatsData> entry : top) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
            String name = op.getName() != null ? op.getName() : "Unknown";

            sender.sendMessage(plugin.getMessage("top-entry",
                    "{rank}", String.valueOf(rank),
                    "{player}", name,
                    "{kills}", String.valueOf(entry.getValue().getKills())));
            rank++;
        }

        sender.sendMessage(plugin.getMessage("top-footer"));
        return true;
    }
}

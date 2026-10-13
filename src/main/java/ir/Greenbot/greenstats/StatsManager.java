package ir.Greenbot.greenstats;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsManager {

    private final GreenStats plugin;
    private final File statsFile;
    private final FileConfiguration statsConfig;

    // UUID -> StatsData
    private final Map<UUID, StatsData> stats = new HashMap<>();

    public StatsManager(GreenStats plugin) {
        this.plugin = plugin;
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create stats.yml: " + e.getMessage());
            }
        }

        this.statsConfig = YamlConfiguration.loadConfiguration(statsFile);
        load();
    }

    public void load() {
        stats.clear();
        ConfigurationSection section = statsConfig.getConfigurationSection("players");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                ConfigurationSection s = section.getConfigurationSection(key);
                if (s == null) continue;

                int kills = s.getInt("kills", 0);
                int deaths = s.getInt("deaths", 0);
                int currentStreak = s.getInt("currentStreak", 0);
                int bestStreak = s.getInt("bestStreak", 0);

                stats.put(uuid, new StatsData(kills, deaths, currentStreak, bestStreak));
            } catch (IllegalArgumentException ignored) {
                // UUID نامعتبر
            }
        }
    }

    public void save() {
        statsConfig.set("players", null);
        for (Map.Entry<UUID, StatsData> entry : stats.entrySet()) {
            String path = "players." + entry.getKey().toString();
            StatsData d = entry.getValue();
            statsConfig.set(path + ".kills", d.getKills());
            statsConfig.set(path + ".deaths", d.getDeaths());
            statsConfig.set(path + ".currentStreak", d.getCurrentStreak());
            statsConfig.set(path + ".bestStreak", d.getBestStreak());
        }
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save stats.yml: " + e.getMessage());
        }
    }

    public StatsData getStats(UUID uuid) {
        return stats.computeIfAbsent(uuid, k -> new StatsData());
    }

    public void addKill(UUID killer) {
        StatsData d = getStats(killer);
        d.addKill();
    }

    public void addDeath(UUID victim) {
        StatsData d = getStats(victim);
        d.addDeath();
    }

    // لیست بازیکنان مرتب‌شده بر اساس kill
    public List<Map.Entry<UUID, StatsData>> getTopKillers(int limit) {
        List<Map.Entry<UUID, StatsData>> list = new ArrayList<>(stats.entrySet());
        list.sort(Comparator.comparingInt((Map.Entry<UUID, StatsData> e) -> e.getValue().getKills()).reversed());
        if (list.size() > limit) {
            return list.subList(0, limit);
        }
        return list;
    }
}

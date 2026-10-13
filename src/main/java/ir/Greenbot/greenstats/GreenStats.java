package ir.Greenbot.greenstats;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class GreenStats extends JavaPlugin {

    private StatsManager statsManager;

    @Override
    public void onEnable() {
        // لاگ سبز
        getComponentLogger().info(
                Component.text("[GreenStats] Enabling !").color(NamedTextColor.GREEN)
        );

        // ساخت config.yml پیش‌فرض
        saveDefaultConfig();

        // ساخت StatsManager
        this.statsManager = new StatsManager(this);

        // ثبت listener ها
        getServer().getPluginManager().registerEvents(new DeathListener(this, statsManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this, statsManager), this);

        // ثبت دستورات
        PluginCommand statsCmd = getCommand("stats");
        if (statsCmd != null) statsCmd.setExecutor(new StatsCommand(this, statsManager));

        PluginCommand topCmd = getCommand("topkills");
        if (topCmd != null) topCmd.setExecutor(new TopCommand(this, statsManager));

        PluginCommand adminCmd = getCommand("greenstats");
        if (adminCmd != null) adminCmd.setExecutor(new AdminCommand(this, statsManager));

        // تسک خودکار ذخیره‌سازی
        long saveInterval = getConfig().getLong("database.save-interval", 300L) * 20L;
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            statsManager.save();
        }, saveInterval, saveInterval);

        getComponentLogger().info(
                Component.text("[GreenStats] Enabled!").color(NamedTextColor.GREEN)
        );
    }

    @Override
    public void onDisable() {
        getComponentLogger().info(
                Component.text("[GreenStats] Disabling...").color(NamedTextColor.RED)
        );

        if (statsManager != null) {
            statsManager.save();
        }

        getComponentLogger().info(
                Component.text("[GreenStats] Disabled!").color(NamedTextColor.RED)
        );
    }

    // گرفتن پیام از config با جایگزینی placeholder
    public String getMessage(String key, String... replacements) {
        String msg = getConfig().getString("messages." + key, "&cMessage not found: " + key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return msg;
    }

    public String getPrefix() {
        return getConfig().getString("prefix", "&aGreenStats &8» &r");
    }

    public String color(String text) {
        return text;
    }

    public Component colorComponent(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}

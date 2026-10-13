package ir.Greenbot.greenstats;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DeathListener implements Listener {

    private final GreenStats plugin;
    private final StatsManager statsManager;

    public DeathListener(GreenStats plugin, StatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // ثبت مرگ برای victim
        statsManager.addDeath(victim.getUniqueId());

        // اگه killer وجود داشت، ثبت kill
        if (killer != null && !killer.getUniqueId().equals(victim.getUniqueId())) {
            statsManager.addKill(killer.getUniqueId());

            // پیام kill
            String msg = plugin.getMessage("kill-message",
                    "{killer}", killer.getName(),
                    "{victim}", victim.getName(),
                    "{kills}", String.valueOf(statsManager.getStats(killer.getUniqueId()).getKills()));

            killer.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg));
            victim.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg));
        }
    }

    // ذخیره آمار وقتی بازیکن از سرور خارج میشه
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        statsManager.save();
    }
}

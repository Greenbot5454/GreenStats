package ir.Greenbot.greenstats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class JoinListener implements Listener {

    private final GreenStats plugin;
    private final StatsManager statsManager;

    public JoinListener(GreenStats plugin, StatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // چک کن توی config فعاله
        if (!plugin.getConfig().getBoolean("book.give-on-join", true)) return;

        // بعد از ۲۰ تیک (۱ ثانیه) کتاب رو بده تا از پاک شدن inventory جلوگیری بشه
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;

            int slot = plugin.getConfig().getInt("book.slot", 1);
            if (slot < 0 || slot > 8) slot = 1;

            player.getInventory().setItem(slot, BookItem.createBook(plugin, statsManager, player));
        }, 20L);
    }
}

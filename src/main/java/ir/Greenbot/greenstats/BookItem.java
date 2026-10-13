package ir.Greenbot.greenstats;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookItem {

    public static ItemStack createBook(GreenStats plugin, StatsManager statsManager, Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta == null) return book;

        StatsData data = statsManager.getStats(player.getUniqueId());

        // عنوان و نویسنده
        String title = plugin.getConfig().getString("book.title", "&a&lYour Stats");
        String author = plugin.getConfig().getString("book.author", "&aGreenStats");

        meta.title(LegacyComponentSerializer.legacyAmpersand().deserialize(title));
        meta.author(LegacyComponentSerializer.legacyAmpersand().deserialize(author));

        // صفحه‌ها
        List<String> rawPages = plugin.getConfig().getStringList("book.pages");
        List<net.kyori.adventure.text.Component> pages = new ArrayList<>();

        for (String rawPage : rawPages) {
            String filled = rawPage
                    .replace("{player}", player.getName())
                    .replace("{kills}", String.valueOf(data.getKills()))
                    .replace("{deaths}", String.valueOf(data.getDeaths()))
                    .replace("{kdr}", String.valueOf(data.getKdr()))
                    .replace("{streak}", String.valueOf(data.getCurrentStreak()))
                    .replace("{best_streak}", String.valueOf(data.getBestStreak()));

            pages.add(LegacyComponentSerializer.legacyAmpersand().deserialize(filled));
        }

        meta.pages(pages);
        book.setItemMeta(meta);
        return book;
    }
}

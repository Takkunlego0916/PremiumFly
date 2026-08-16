package io.github.takkunlego0916.premiumFly;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class MessageManager {

    private final PremiumFly plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private String language;

    public MessageManager(PremiumFly plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();
        String configured = config.getString("language", "jp");
        language = "en".equalsIgnoreCase(configured) ? "en" : "jp";
    }

    public String getLanguage() {
        return language;
    }

    private String raw(String key) {
        FileConfiguration config = plugin.getConfig();
        String value = config.getString("messages." + language + "." + key);
        if (value != null) {
            return value;
        }
        String fallback = config.getString("messages.en." + key);
        return fallback != null ? fallback : key;
    }

    public Component render(String key, TagResolver... placeholders) {
        return miniMessage.deserialize(raw(key), placeholders);
    }

    public void send(CommandSender target, String key, TagResolver... placeholders) {
        target.sendMessage(render(key, placeholders));
    }

    public void actionBar(Player target, String key, TagResolver... placeholders) {
        target.sendActionBar(render(key, placeholders));
    }
}

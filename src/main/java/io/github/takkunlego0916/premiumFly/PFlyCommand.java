package io.github.takkunlego0916.premiumFly;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PFlyCommand implements CommandExecutor, TabCompleter {

    private final PremiumFly plugin;
    private final FlightManager flightManager;
    private final MessageManager messageManager;

    public PFlyCommand(PremiumFly plugin, FlightManager flightManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.flightManager = flightManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pfly.admin")) {
            messageManager.send(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            messageManager.send(sender, "help");
            return true;
        }

        String first = args[0].toLowerCase(Locale.ROOT);

        switch (first) {
            case "reload" -> {
                plugin.reload();
                messageManager.send(sender, "reload-success");
            }
            case "list" -> handleList(sender);
            case "info" -> handleInfo(sender);
            case "help" -> messageManager.send(sender, "help");
            default -> {
                if (args.length != 2) {
                    messageManager.send(sender, "help");
                    return true;
                }
                handleGrant(sender, args[0], args[1]);
            }
        }

        return true;
    }

    private void handleGrant(CommandSender sender, String targetName, String action) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            messageManager.send(sender, "player-not-found");
            return;
        }

        boolean enable = action.equalsIgnoreCase("enable");
        boolean disable = action.equalsIgnoreCase("disable");

        if (!enable && !disable) {
            messageManager.send(sender, "help");
            return;
        }

        boolean currentlyGranted = flightManager.hasGrantFlag(target);

        if (enable) {
            if (currentlyGranted) {
                messageManager.send(sender, "already-enabled", Placeholder.unparsed("player", target.getName()));
                return;
            }
            flightManager.setGrantFlag(target, true);
            messageManager.send(sender, "enabled-sender", Placeholder.unparsed("player", target.getName()));
            messageManager.send(target, "enabled-target");
        } else {
            if (!currentlyGranted) {
                messageManager.send(sender, "already-disabled", Placeholder.unparsed("player", target.getName()));
                return;
            }
            flightManager.setGrantFlag(target, false);
            messageManager.send(sender, "disabled-sender", Placeholder.unparsed("player", target.getName()));
            messageManager.send(target, "disabled-target");
        }
    }

    private void handleList(CommandSender sender) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (flightManager.hasGrantFlag(player) || flightManager.hasPermissionGrant(player)) {
                names.add(player.getName());
            }
        }

        if (names.isEmpty()) {
            messageManager.send(sender, "list-empty");
            return;
        }

        messageManager.send(sender, "list-header", Placeholder.unparsed("count", String.valueOf(names.size())));
        for (String name : names) {
            messageManager.send(sender, "list-entry", Placeholder.unparsed("player", name));
        }
    }

    private void handleInfo(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        messageManager.send(sender, "info-name", Placeholder.unparsed("version", version));
        messageManager.send(sender, "info-author");

        UpdateChecker checker = plugin.getUpdateChecker();
        if (checker == null || !checker.hasResult()) {
            messageManager.send(sender, "info-update-unknown");
            return;
        }

        if (checker.isUpToDate()) {
            messageManager.send(sender, "info-uptodate");
        } else {
            messageManager.send(sender, "info-outdated",
                    Placeholder.unparsed("latest", checker.getLatestVersion()),
                    Placeholder.unparsed("current", version),
                    Placeholder.unparsed("url", checker.getPageUrl()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("pfly.admin")) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                options.add(player.getName());
            }
            options.add("reload");
            options.add("list");
            options.add("info");
            options.add("help");
            return filter(options, args[0]);
        }

        if (args.length == 2) {
            String first = args[0].toLowerCase(Locale.ROOT);
            if (!first.equals("reload") && !first.equals("list") && !first.equals("info") && !first.equals("help")) {
                return filter(List.of("enable", "disable"), args[1]);
            }
        }

        return List.of();
    }

    private List<String> filter(List<String> options, String prefix) {
        String lower = prefix.toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                result.add(option);
            }
        }
        return result;
    }
}

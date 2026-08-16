package io.github.takkunlego0916.premiumFly;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FlightListener implements Listener {

    private final PremiumFly plugin;
    private final FlightManager flightManager;
    private final MessageManager messageManager;
    private final Map<UUID, Long> hintCooldowns = new HashMap<>();

    public FlightListener(PremiumFly plugin, FlightManager flightManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.flightManager = flightManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        flightManager.migrateLegacyTag(player);
        flightManager.applyState(player);
        notifyIfOutdated(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        flightManager.forget(id);
        hintCooldowns.remove(id);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                flightManager.applyState(player);
            }
        }, 2L);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        flightManager.applyState(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if (flightManager.isExcludedGameMode(player)
                || flightManager.isWorldDisabled(player)
                || !flightManager.isEligible(player)
                || player.isFlying()) {
            return;
        }

        flightManager.registerJump(player);
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (flightManager.isExcludedGameMode(player)
                || flightManager.isWorldDisabled(player)
                || !flightManager.isEligible(player)) {
            return;
        }

        if (event.isFlying()) {
            if (flightManager.consumeJumpIfValid(player)) {
                messageManager.actionBar(player, "flight-start");
                flightManager.playActivateEffects(player);
            } else {
                event.setCancelled(true);
                sendJumpHint(player);
            }
        } else {
            flightManager.clearJumpState(player);
            messageManager.actionBar(player, "flight-end");
            flightManager.playDeactivateEffects(player);
        }
    }

    private void sendJumpHint(Player player) {
        if (!plugin.getConfig().getBoolean("hints.jump-hint-enabled", true)) {
            return;
        }

        long cooldownMillis = Math.max(0, plugin.getConfig().getLong("hints.jump-hint-cooldown-seconds", 3)) * 1000L;
        long now = System.currentTimeMillis();
        Long last = hintCooldowns.get(player.getUniqueId());

        if (last != null && now - last < cooldownMillis) {
            return;
        }

        hintCooldowns.put(player.getUniqueId(), now);
        messageManager.actionBar(player, "jump-hint");
    }

    private void notifyIfOutdated(Player player) {
        UpdateChecker checker = plugin.getUpdateChecker();
        if (checker == null || !checker.hasResult() || checker.isUpToDate()) {
            return;
        }
        if (!player.hasPermission("premiumfly.notify")) {
            return;
        }

        messageManager.send(player, "info-outdated",
                Placeholder.unparsed("latest", checker.getLatestVersion()),
                Placeholder.unparsed("current", plugin.getDescription().getVersion()),
                Placeholder.unparsed("url", checker.getPageUrl()));
    }
}

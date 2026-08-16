package io.github.takkunlego0916.premiumFly;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PremiumFly extends JavaPlugin {

    private FlightManager flightManager;
    private MessageManager messageManager;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        messageManager = new MessageManager(this);
        flightManager = new FlightManager(this);

        Bukkit.getPluginManager().registerEvents(new FlightListener(this, flightManager, messageManager), this);

        PFlyCommand command = new PFlyCommand(this, flightManager, messageManager);
        getCommand("pfly").setExecutor(command);
        getCommand("pfly").setTabCompleter(command);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                flightManager.applyState(player);
            }
        }, 0L, 20L);

        setupMetrics();
        setupUpdateChecker();

        getLogger().info("PremiumFly v" + getDescription().getVersion() + " has been enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        if (flightManager != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                flightManager.reset(player);
            }
        }

        getLogger().info("PremiumFly has been disabled.");
    }

    public void reload() {
        reloadConfig();
        messageManager.reload();
        flightManager.reload();

        for (Player player : Bukkit.getOnlinePlayers()) {
            flightManager.applyState(player);
        }
    }

    private void setupMetrics() {
        int pluginId = getConfig().getInt("metrics.plugin-id", 0);
        if (getConfig().getBoolean("metrics.enabled", true) && pluginId > 0) {
            new Metrics(this, pluginId);
        }
    }

    private void setupUpdateChecker() {
        if (getConfig().getBoolean("update-checker.enabled", true)) {
            updateChecker = new UpdateChecker(this);
            updateChecker.check();
        }
    }

    public FlightManager getFlightManager() {
        return flightManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}

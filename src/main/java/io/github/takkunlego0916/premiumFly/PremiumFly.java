package io.github.takkunlego0916.premiumFly;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class PremiumFly extends JavaPlugin implements Listener {

    private final String TAG = "premium";
    private String lang;


    private final HashSet<UUID> jumped = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        lang = getConfig().getString("language", "jp");

        Bukkit.getPluginManager().registerEvents(this, this);


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    updateFly(p);
                }
            }
        }.runTaskTimer(this, 0L, 20L);

        getLogger().info("PremiumFly が有効化されました！（ダブルジャンプ対応）");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("pfly")) return false;

        if (!(sender.hasPermission("pfly.admin"))) {
            sender.sendMessage(msg("no_perm"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadPlugin();
            sender.sendMessage("§aPremiumFly の設定をリロードしました。");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(msg("usage"));
            return true;
        }


        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(msg("not_found"));
            return true;
        }

        if (args[1].equalsIgnoreCase("enable")) {
            target.addScoreboardTag(TAG);
            updateFly(target);

            sender.sendMessage(msg("enabled_sender").replace("%player%", target.getName()));
            target.sendMessage(msg("enabled_target"));
            return true;
        }

        if (args[1].equalsIgnoreCase("disable")) {
            target.removeScoreboardTag(TAG);
            updateFly(target);

            sender.sendMessage(msg("disabled_sender").replace("%player%", target.getName()));
            target.sendMessage(msg("disabled_target"));
            return true;
        }

        sender.sendMessage(msg("usage"));
        return true;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        updateFly(e.getPlayer());
        jumped.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(this, () -> updateFly(e.getPlayer()), 2L);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if (!p.getScoreboardTags().contains(TAG)) return;

        if (p.getGameMode() == GameMode.CREATIVE) return;

        if (!event.getFrom().getBlock().getType().isSolid()
                && event.getTo().getBlock().getType().isSolid()) {
            jumped.remove(id);
        }
    }


    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();


        if (!p.getScoreboardTags().contains(TAG) || !p.getAllowFlight()) {
            jumped.remove(id);    
            event.setCancelled(true);
            p.setFlying(false);
            return;
        }

        if (p.getGameMode() == GameMode.CREATIVE) return;

        event.setCancelled(true);

        if (!jumped.contains(id)) {
            jumped.add(id);
            p.setFlying(false);
        } else {
            p.setFlying(true);
            jumped.remove(id); 
        }
    }





    private void updateFly(Player p) {
        boolean hasTag = p.getScoreboardTags().contains(TAG);

        if (p.getGameMode() == GameMode.CREATIVE) return;

        if (hasTag) {
            p.setAllowFlight(true);

            if (p.isOnGround()) {
                p.setFlying(false);
            }
        } else {
            p.setAllowFlight(false);
            p.setFlying(false);
            jumped.remove(p.getUniqueId());
        }
    }



    private String msg(String key) {
        if (lang.equalsIgnoreCase("en")) {
            switch (key) {
                case "no_perm":
                    return "§cYou do not have permission.";
                case "usage":
                    return "§eUsage: /pfly <player> <enable|disable> or /pfly reload";
                case "not_found":
                    return "§cPlayer not found.";
                case "enabled_sender":
                    return "§aGave PremiumFly to %player%.";
                case "enabled_target":
                    return "§aPremiumFly enabled!";
                case "disabled_sender":
                    return "§cRemoved PremiumFly from %player%.";
                case "disabled_target":
                    return "§cPremiumFly disabled!";
            }
        }

        switch (key) {
            case "no_perm":
                return "§c権限がありません。";
            case "usage":
                return "§e使用方法: /pfly <player> <enable|disable> または /pfly reload";
            case "not_found":
                return "§cプレイヤーが見つかりません。";
            case "enabled_sender":
                return "§a%player% に PremiumFly を付与しました。";
            case "enabled_target":
                return "§aPremiumFly が有効化されました！";
            case "disabled_sender":
                return "§c%player% の PremiumFly を解除しました。";
            case "disabled_target":
                return "§cPremiumFly が無効化されました。";
        }
        return key;
    }

    private void reloadPlugin() {
        reloadConfig();              
        lang = getConfig().getString("language", "jp"); 


        for (Player p : Bukkit.getOnlinePlayers()) {
            updateFly(p);
        }

        getLogger().info("PremiumFly の設定をリロードしました！ (lang=" + lang + ")");
    }



}

package io.github.takkunlego0916.premiumFly;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class FlightManager {

    private static final String LEGACY_TAG = "premium";

    private final PremiumFly plugin;
    private final NamespacedKey grantedKey;
    private final Map<UUID, Long> pendingJumps = new HashMap<>();

    private double flightSpeed;
    private int jumpWindowTicks;
    private Set<String> disabledWorlds;

    private boolean soundEnabled;
    private Sound activateSound;
    private Sound deactivateSound;

    private boolean particlesEnabled;
    private Particle activateParticle;

    public FlightManager(PremiumFly plugin) {
        this.plugin = plugin;
        this.grantedKey = new NamespacedKey(plugin, "granted");
        reload();
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();

        flightSpeed = clamp(config.getDouble("flight.speed", 0.2), 0.1, 1.0);
        jumpWindowTicks = Math.max(5, config.getInt("flight.jump-window-ticks", 40));
        disabledWorlds = new HashSet<>(config.getStringList("flight.disabled-worlds"));

        soundEnabled = config.getBoolean("effects.sound.enabled", true);
        activateSound = parseSound(config.getString("effects.sound.activate"), Sound.ENTITY_ENDERMAN_TELEPORT);
        deactivateSound = parseSound(config.getString("effects.sound.deactivate"), Sound.ENTITY_BAT_TAKEOFF);

        particlesEnabled = config.getBoolean("effects.particles.enabled", true);
        activateParticle = parseParticle(config.getString("effects.particles.activate"), Particle.CLOUD);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private Sound parseSound(String name, Sound fallback) {
        if (name == null || name.isBlank()) {
            return fallback;
        }
        try {
            return Sound.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning("Unknown sound '" + name + "' in config.yml, using the default instead.");
            return fallback;
        }
    }

    private Particle parseParticle(String name, Particle fallback) {
        if (name == null || name.isBlank()) {
            return fallback;
        }
        try {
            return Particle.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning("Unknown particle '" + name + "' in config.yml, using the default instead.");
            return fallback;
        }
    }

    public boolean isExcludedGameMode(Player player) {
        GameMode mode = player.getGameMode();
        return mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR;
    }

    public boolean isWorldDisabled(Player player) {
        return disabledWorlds.contains(player.getWorld().getName());
    }

    public boolean hasGrantFlag(Player player) {
        return player.getPersistentDataContainer().getOrDefault(grantedKey, PersistentDataType.BOOLEAN, false);
    }

    public boolean hasPermissionGrant(Player player) {
        return player.hasPermission("premiumfly.fly");
    }

    public boolean isEligible(Player player) {
        return hasGrantFlag(player) || hasPermissionGrant(player);
    }

    public void setGrantFlag(Player player, boolean granted) {
        if (granted) {
            player.getPersistentDataContainer().set(grantedKey, PersistentDataType.BOOLEAN, true);
        } else {
            player.getPersistentDataContainer().remove(grantedKey);
        }
        applyState(player);
    }

    public void migrateLegacyTag(Player player) {
        if (!player.getScoreboardTags().contains(LEGACY_TAG)) {
            return;
        }
        player.removeScoreboardTag(LEGACY_TAG);
        if (!hasGrantFlag(player)) {
            player.getPersistentDataContainer().set(grantedKey, PersistentDataType.BOOLEAN, true);
        }
    }

    public void applyState(Player player) {
        UUID id = player.getUniqueId();

        if (isExcludedGameMode(player)) {
            return;
        }

        boolean allowed = isEligible(player) && !isWorldDisabled(player);

        if (allowed) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
            if (player.getFlySpeed() != (float) flightSpeed) {
                player.setFlySpeed((float) flightSpeed);
            }
            if (player.isOnGround()) {
                if (player.isFlying()) {
                    player.setFlying(false);
                }
                pendingJumps.remove(id);
            }
        } else {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
            }
            if (player.isFlying()) {
                player.setFlying(false);
            }
            pendingJumps.remove(id);
        }
    }

    public void reset(Player player) {
        if (!isExcludedGameMode(player) && isEligible(player)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        pendingJumps.remove(player.getUniqueId());
    }

    public void registerJump(Player player) {
        pendingJumps.put(player.getUniqueId(), player.getWorld().getFullTime());
    }

    public boolean consumeJumpIfValid(Player player) {
        Long jumpTick = pendingJumps.remove(player.getUniqueId());
        if (jumpTick == null) {
            return false;
        }
        long elapsed = player.getWorld().getFullTime() - jumpTick;
        return elapsed >= 0 && elapsed <= jumpWindowTicks;
    }

    public void clearJumpState(Player player) {
        pendingJumps.remove(player.getUniqueId());
    }

    public void forget(UUID playerId) {
        pendingJumps.remove(playerId);
    }

    public void playActivateEffects(Player player) {
        Location location = player.getLocation();
        if (soundEnabled) {
            player.getWorld().playSound(location, activateSound, 0.6f, 1.4f);
        }
        if (particlesEnabled) {
            player.getWorld().spawnParticle(activateParticle, location.clone().add(0, 0.2, 0), 12, 0.3, 0.2, 0.3, 0.01);
        }
    }

    public void playDeactivateEffects(Player player) {
        if (soundEnabled) {
            player.getWorld().playSound(player.getLocation(), deactivateSound, 0.5f, 0.8f);
        }
    }
}

# PremiumFly

![Banner](https://github.com/Takkunlego0916/PremiumFly/blob/main/banner.png)

![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Paper%20%2F%20Spigot-orange)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
![License](https://img.shields.io/badge/License-MIT-informational)

日本語の説明は [README-jp.md](README-jp.md) をご覧ください。

**PremiumFly** brings Creative-style flight to Survival, Adventure, and any other non-Creative game mode — triggered by a satisfying double jump, just like Creative mode. Reward your donors, VIPs, or staff with a flight perk that never breaks the survival feel of your server.

---

## Table of Contents

- [Why PremiumFly](#why-premiumfly)
- [Features](#features)
- [How It Feels To Play](#how-it-feels-to-play)
- [Requirements](#requirements)
- [Installation](#installation)
- [Commands](#commands)
- [Permissions](#permissions)
- [Configuration](#configuration)
- [PlaceholderAPI / Other Plugins](#integrating-with-other-plugins)
- [FAQ](#faq)
- [Support](#support)
- [License](#license)

---

## Why PremiumFly

Most "fly" plugins just flip `allowFlight` on. PremiumFly is built to feel like an intentional *game mechanic* — a double jump — rather than a slash command toggle, so donors and VIPs get something that actually feels premium.

- Built specifically for **Paper 1.21**, using the modern Adventure/MiniMessage text API — no legacy `&`-code messages.
- Grants can come from a **permission node** (great for LuckPerms rank groups) *or* a simple admin command — your choice.
- Player data is stored safely using Bukkit's **Persistent Data Container**, not scoreboard tags, so it won't collide with scoreboard plugins.

## Features

- 🕹️ **Double-jump flight** — jump once normally, jump again mid-air to take off, exactly like Creative mode.
- 🔑 **Two ways to grant access**
  - `/pfly <player> enable` for one-off, manual grants.
  - The `premiumfly.fly` permission node for automatic grants through LuckPerms or any permission plugin.
- 🌐 **Fully localized** — built-in Japanese and English message packs, both fully editable in `config.yml` using [MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting (hex colors, gradients, hover text, and more).
- 🔊 **Sound & particle feedback** on take-off and landing, so flight feels like an ability, not a toggle — fully configurable, can be disabled.
- 🌍 **Per-world control** — disable PremiumFly in specific worlds (minigames, parkour maps, etc.) via `disabled-worlds` in the config.
- 🧭 **Admin quality-of-life** — tab completion, `/pfly list` to see who currently has flight, `/pfly info` for version/update status, and `/pfly reload` to reload without a restart.
- 🔄 **Built-in update checker** — get notified in-game (and in console) when a new version is published on Modrinth.
- 📊 **Optional [bStats](https://bstats.org) metrics** — see real anonymous usage stats for your own copy of the plugin.
- 🛡️ **Safe by design**
  - Automatically revokes flight the moment permission is removed or the target world changes.
  - Never touches Creative or Spectator mode flight, so staff and admins are unaffected.
  - Cleans up its own state on `/reload` and plugin disable so nobody gets stuck flying (or gets kicked for "flying").

## How It Feels To Play

1. Jump once → a completely normal jump.
2. While still airborne, jump again → flight engages, with a sound and particle burst.
3. Fly around freely, just like Creative mode.
4. Land, or double-tap again → flight ends.
5. Jump twice again whenever you want to take off once more.

## Requirements

- **Paper** 1.21 or later (built and tested against `1.21.11`). Spigot is not supported — PremiumFly relies on Paper-only APIs for performance and reliability.
- **Java 21** or later.

## Installation

1. Download the latest `PremiumFly-<version>.jar` from [Modrinth](https://modrinth.com/plugin/premiumfly) or [GitHub Releases](https://github.com/Takkunlego0916/PremiumFly/releases).
2. Drop it into your server's `plugins/` folder.
3. Start or restart the server.
4. Edit `plugins/PremiumFly/config.yml` to taste, then run `/pfly reload`.

## Commands

| Command                    | Description                                   | Permission   |
| --------------------------- | ---------------------------------------------- | ------------ |
| `/pfly <player> enable`    | Grant PremiumFly to a player                  | `pfly.admin` |
| `/pfly <player> disable`   | Revoke PremiumFly from a player               | `pfly.admin` |
| `/pfly list`                | List every online player who currently has PremiumFly | `pfly.admin` |
| `/pfly reload`             | Reload `config.yml` without restarting        | `pfly.admin` |
| `/pfly info`                | Show plugin version and update status         | `pfly.admin` |
| `/pfly help`                 | Show the command list in-game                 | `pfly.admin` |

## Permissions

| Permission          | Default | Description                                                              |
| -------------------- | ------- | -------------------------------------------------------------------------- |
| `pfly.admin`         | `op`    | Access to every `/pfly` subcommand.                                       |
| `premiumfly.fly`     | `false` | Automatically grants double-jump flight — no command needed. Perfect for LuckPerms rank groups. |
| `premiumfly.notify`  | `op`    | Notifies the player on join if a newer PremiumFly version is available.  |

## Configuration

Every value below lives in `config.yml` and reloads live with `/pfly reload`.

```yaml
language: jp

flight:
  speed: 0.2
  jump-window-ticks: 40
  disabled-worlds: []

effects:
  sound:
    enabled: true
    activate: ENTITY_ENDERMAN_TELEPORT
    deactivate: ENTITY_BAT_TAKEOFF
  particles:
    enabled: true
    activate: CLOUD

hints:
  jump-hint-enabled: true
  jump-hint-cooldown-seconds: 3

update-checker:
  enabled: true
  modrinth-id: premiumfly

metrics:
  enabled: true
  plugin-id: 0

messages:
  jp: { ... }
  en: { ... }
```

- `flight.speed` — flying speed while PremiumFly is active (`0.1`–`1.0`).
- `flight.jump-window-ticks` — how long a player has, after a real jump, to double-jump into flight.
- `flight.disabled-worlds` — world names where PremiumFly never activates.
- `update-checker.modrinth-id` — set this to your own Modrinth project slug if you fork this plugin.
- `metrics.plugin-id` — get a free plugin ID at [bStats.org](https://bstats.org) to enable metrics; leave `0` to keep metrics off.
- `messages` — every in-game string, fully overridable per language with MiniMessage formatting.

## Integrating With Other Plugins

Flight grants are stored in a `PersistentDataContainer` under the key `premiumfly:granted`, so other plugins can safely read it without touching scoreboards. If you already manage ranks with LuckPerms (or any permission plugin), simply give the desired group the `premiumfly.fly` permission — no command required.

## FAQ

**Does this work in Spigot, or only Paper?**
Paper only. PremiumFly uses Paper's `PlayerJumpEvent` for reliable, low-overhead jump detection — this event does not exist on vanilla Spigot.

**Will this conflict with Essentials `/fly` or other flight plugins?**
No — PremiumFly only manages flight for players it has granted, and safely restores their state if the plugin is disabled or reloaded.

**Can I use this without commands, purely through LuckPerms?**
Yes — grant the `premiumfly.fly` permission to a group and every member instantly gets double-jump flight.

**Upgrading from an older PremiumFly version?**
Existing grants are migrated automatically the next time each player joins — no action needed.

## Support

Found a bug or have a feature request? Please open an issue on [GitHub](https://github.com/Takkunlego0916/PremiumFly/issues).

## License

PremiumFly is released under the [MIT License](LICENSE) — free to use, modify, and redistribute.

## Author

Created by **Takkunlego0916**

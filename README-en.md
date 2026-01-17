# PremiumFly

![Banner](https://github.com/Takkunlego0916/PremiumFly/blob/main/banner.png)

![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Paper%20%2F%20Spigot-orange)

日本人へ。一番下に日本語の説明があります。

**PremiumFly** is a Paper/Spigot plugin that adds **double-jump flight** for selected players in Survival mode, similar to Creative flight but triggered by a double jump.

This plugin is designed for premium ranks, donors, or special roles on your server.

---

## Features

- **Double-Jump Flight** in Survival mode  
  - First jump → normal jump  
  - Second jump → start flying (like Creative mode)

- **Permission-based control**
  - Only authorized players can use PremiumFly

- **Admin command to enable/disable flight per player**

- **Multi-language support**
  - Supports Japanese and English (configurable in `config.yml`)

- **Reload support**
  - You can reload the config without restarting the server

- **Safe & stable**
  - Automatically disables flight when permission is removed  
  - Resets flight properly when landing or changing game mode

---

## Requirements

- Paper or Spigot **1.21.11** (or compatible versions)
- Java 17+ (recommended)

---

## Installation

1. Download the latest version of `PremiumFly.jar`
2. Put it in your server's `plugins` folder
3. Restart or reload the server
4. Edit `plugins/PremiumFly/config.yml` if needed

---

## Configuration (`config.yml`)

```yaml
# PremiumFly 言語設定
# jp = 日本語
# en = English

# PremiumFly language settings
# jp = Japanese
# en = English
language: jp
```

## Commands
| Command                  | Description                     |
| ------------------------ | ------------------------------- |
| `/pfly <player> enable`  | Enable PremiumFly for a player  |
| `/pfly <player> disable` | Disable PremiumFly for a player |
| `/pfly reload`           | Reload the plugin configuration |

## Permissions
| Permission   | Description                        |
| ------------ | ---------------------------------- |
| `pfly.admin` | Allows use of all `/pfly` commands |


💡 Note: By default, players still need to be granted PremiumFly via the /pfly enable command.

## How It Works (for players)

Jump once → normal jump

Jump again in mid-air → flight mode starts

Land on the ground → flight resets

Jump twice again → fly again

This behavior closely mimics Creative mode while still keeping the Survival feel.

## License

This project is open-source. Feel free to modify and distribute it.

## Auther

Created by Takkunlego0916
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


# Japanese

**PremiumFly** は、Paper / Spigot 向けのプラグインで、  
**サバイバルモードでも“ダブルジャンプで飛行”できる機能**を追加します。

通常のサバイバルの雰囲気を保ちながら、クリエイティブのような自由な移動を実現できるため、  
**寄付者ランク（Donator）・VIP・特別ロール向けの特典**として最適です。

---

## 主な機能

- **サバイバルでダブルジャンプ飛行**
  - 1回目のジャンプ → 通常ジャンプ  
  - 2回目のジャンプ → 飛行開始（クリエイティブ風）

- **権限（Permission）対応**
  - 管理者だけがプレイヤーに付与できる設計

- **コマンドで個別に ON / OFF**
  - プレイヤーごとに有効化・無効化が可能

- **多言語対応**
  - `config.yml` から日本語 / 英語を切り替え可能

- **リロード対応**
  - サーバー再起動なしで設定を反映可能

- **安全設計**
  - 権限が外れたら自動で飛行無効化  
  - 着地時やゲームモード変更時に状態を適切にリセット

---

## 動作環境

- Paper または Spigot **1.21.11**（または互換バージョン）
- Java 17 以上（推奨）

---

## インストール方法

1. 最新の `PremiumFly.jar` をダウンロード
2. `plugins` フォルダに配置
3. サーバーを再起動 または `/reload`
4. 必要に応じて `plugins/PremiumFly/config.yml` を編集

---

## 設定ファイル（`config.yml`）

```yaml
# PremiumFly 言語設定
# jp = 日本語
# en = English

# PremiumFly language settings
# jp = Japanese
# en = English
language: jp
```

## コマンド一覧

| コマンド                   | 説明                              |
| ------------------------ | -------------------------------- |
| `/pfly <player> enable`  | 指定したプレイヤーに PremiumFly を付与 |
| `/pfly <player> disable` | 指定したプレイヤーの PremiumFly を解除 |
| `/pfly reload`           | 設定ファイルを再読み込み               |


## 権限
| 権限ノード     | 説明                     |
| ------------ | ----------------------- |
| `pfly.admin` | `/pfly` コマンドの使用を許可 |



## プレイヤーの操作方法
1. 1回ジャンプ → 普通のジャンプ
2. 空中でもう一度ジャンプ → 飛行開始
3. 地面に着地 → 飛行がリセット
4. 再び2回ジャンプ → 再度飛行可能
クリエイティブに近い操作感を保ちつつ、サバイバルのバランスを崩しにくい仕様です。

## ライセンス
このプラグインはオープンソースです。 自由に改変・再配布できます。

## 作者
Created by Takkunlego0916
=======


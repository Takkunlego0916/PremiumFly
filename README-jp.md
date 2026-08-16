# PremiumFly

![Banner](https://github.com/Takkunlego0916/PremiumFly/blob/main/banner.png)

![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Paper%20%2F%20Spigot-orange)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
![License](https://img.shields.io/badge/License-MIT-informational)

English README is available at [README.md](README.md) / [README-en.md](README-en.md).

**PremiumFly** は、サバイバル・アドベンチャーなど「クリエイティブ以外」のゲームモードに、クリエイティブさながらの飛行を追加するプラグインです。トリガーは気持ちのいい「ダブルジャンプ」。サバイバルの雰囲気を壊さずに、寄付者・VIP・スタッフへのご褒美として使えます。

---

## 目次

- [PremiumFly の特長](#premiumfly-の特長)
- [機能一覧](#機能一覧)
- [プレイ感覚](#プレイ感覚)
- [動作環境](#動作環境)
- [インストール](#インストール)
- [コマンド](#コマンド)
- [権限](#権限)
- [設定ファイル](#設定ファイル)
- [他プラグインとの連携](#他プラグインとの連携)
- [よくある質問](#よくある質問)
- [サポート](#サポート)
- [ライセンス](#ライセンス)

---

## PremiumFly の特長

よくある「フライトプラグイン」は `allowFlight` を切り替えるだけのものがほとんどです。PremiumFly は「ダブルジャンプ」という意図されたゲームメカニクスとして飛行を実装しているため、コマンド一発のトグルとは一線を画す、本当に "プレミアム" な体験を提供します。

- **Paper 1.21** 向けに設計。最新の Adventure / MiniMessage テキスト API を採用し、古い `&` カラーコードは使用していません。
- 付与方法は **権限ノード**（LuckPerms のランクグループに最適）と **管理者コマンド** の2通りから選択可能。
- プレイヤーデータは Bukkit の **Persistent Data Container** で安全に保存。スコアボードタグを使わないため、他のスコアボード系プラグインと競合しません。

## 機能一覧

- 🕹️ **ダブルジャンプ飛行** — 1回目は普通のジャンプ、空中でもう一度ジャンプすると、クリエイティブそのままに飛行開始。
- 🔑 **2通りの付与方法**
  - `/pfly <player> enable` によるその場限りの手動付与。
  - `premiumfly.fly` 権限ノードによる、LuckPerms 等を使った自動付与。
- 🌐 **完全多言語対応** — 日本語・英語のメッセージを標準搭載。すべて `config.yml` 内で [MiniMessage](https://docs.advntr.dev/minimessage/format.html) 形式で自由に編集可能（HEXカラー、グラデーション、ホバーテキストなども対応）。
- 🔊 **離陸・着地時のサウンド＆パーティクル演出** — 単なるON/OFFではなく「能力」として感じられる演出。設定でオフにすることも可能。
- 🌍 **ワールドごとの制御** — `disabled-worlds` の設定で、ミニゲームやパルクールマップなど特定ワールドでのみ無効化可能。
- 🧭 **管理者向けの使いやすさ** — タブ補完、現在飛行権限を持つプレイヤー一覧を表示する `/pfly list`、バージョン・アップデート状況を確認できる `/pfly info`、再起動不要の `/pfly reload` を搭載。
- 🔄 **アップデートチェッカー内蔵** — Modrinth に新しいバージョンが公開されると、ゲーム内・コンソールの両方で通知。
- 📊 **[bStats](https://bstats.org) メトリクス（任意）** — 自分の導入環境での匿名利用統計を確認可能。
- 🛡️ **安全設計**
  - 権限が外れた瞬間、またはワールドを移動した瞬間に自動で飛行を無効化。
  - クリエイティブ・スペクテイターモードの飛行には一切干渉しないため、スタッフ・管理者への影響はありません。
  - `/reload` やプラグイン無効化時にも状態をきちんと後片付けするため、「飛行状態のまま固まる」「不正な飛行でキックされる」といった事故を防止。

## プレイ感覚

1. 1回ジャンプ → 通常のジャンプ。
2. 空中でもう一度ジャンプ → サウンドとパーティクルとともに飛行開始。
3. クリエイティブそのままに自由に飛行。
4. 着地する、またはもう一度ダブルタップ → 飛行終了。
5. またジャンプを2回行えば、いつでも再び飛行可能。

## 動作環境

- **Paper** 1.21 以降（`1.21.11` でビルド・動作確認済み）。Spigot には対応していません — PremiumFly は、パフォーマンスと信頼性のために Paper 専用APIを使用しています。
- **Java 21** 以降。

## インストール

1. [Modrinth](https://modrinth.com/plugin/premiumfly) または [GitHub Releases](https://github.com/Takkunlego0916/PremiumFly/releases) から最新の `PremiumFly-<version>.jar` をダウンロード。
2. サーバーの `plugins/` フォルダに配置。
3. サーバーを起動または再起動。
4. 必要に応じて `plugins/PremiumFly/config.yml` を編集し、`/pfly reload` を実行。

## コマンド

| コマンド                    | 説明                                       | 必要権限      |
| --------------------------- | ------------------------------------------ | ------------- |
| `/pfly <player> enable`    | 指定プレイヤーに PremiumFly を付与          | `pfly.admin` |
| `/pfly <player> disable`   | 指定プレイヤーから PremiumFly を解除        | `pfly.admin` |
| `/pfly list`                | 現在 PremiumFly を持つオンラインプレイヤー一覧 | `pfly.admin` |
| `/pfly reload`             | サーバー再起動なしで `config.yml` を再読込  | `pfly.admin` |
| `/pfly info`                 | バージョン・アップデート状況を表示           | `pfly.admin` |
| `/pfly help`                 | ゲーム内でコマンド一覧を表示                 | `pfly.admin` |

## 権限

| 権限                 | デフォルト | 説明                                                                 |
| -------------------- | ---------- | ---------------------------------------------------------------------- |
| `pfly.admin`         | `op`       | すべての `/pfly` サブコマンドを使用可能。                              |
| `premiumfly.fly`     | `false`    | コマンド不要でダブルジャンプ飛行を自動付与。LuckPerms のランクグループに最適。 |
| `premiumfly.notify`  | `op`       | 新しいバージョンが公開された際、参加時に通知を受け取る。                |

## 設定ファイル

以下はすべて `config.yml` 内の項目で、`/pfly reload` で即座に反映されます。

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

- `flight.speed` — PremiumFly 使用中の飛行速度（`0.1`〜`1.0`）。
- `flight.jump-window-ticks` — 実際にジャンプしてから、飛行に切り替わるまでの猶予（tick）。
- `flight.disabled-worlds` — PremiumFly を無効化するワールド名の一覧。
- `update-checker.modrinth-id` — このプラグインをフォークして公開する場合は、自分の Modrinth プロジェクトのスラッグに変更してください。
- `metrics.plugin-id` — [bStats.org](https://bstats.org) で無料のプラグインIDを取得すると有効化されます。`0` のままだと送信されません。
- `messages` — ゲーム内のすべての文言。言語ごとに MiniMessage 形式で自由に上書き可能。

## 他プラグインとの連携

飛行の付与状態は `premiumfly:granted` というキーで `PersistentDataContainer` に保存されているため、他プラグインからスコアボードを介さず安全に参照できます。すでに LuckPerms 等でランクを管理している場合は、該当グループに `premiumfly.fly` 権限を付与するだけで、コマンド操作なしに自動反映されます。

## よくある質問

**Spigot でも動きますか？Paper 専用ですか？**
Paper 専用です。PremiumFly は、軽量かつ確実にジャンプを検知するために Paper の `PlayerJumpEvent` を使用しており、このイベントはバニラの Spigot には存在しません。

**Essentials の `/fly` や他の飛行プラグインと競合しますか？**
しません。PremiumFly は自身が付与したプレイヤーの飛行状態のみを管理し、プラグインの無効化・リロード時にも安全に状態を復元します。

**コマンドを一切使わず、LuckPerms だけで運用できますか？**
できます。グループに `premiumfly.fly` 権限を付与するだけで、そのグループの全員が即座にダブルジャンプ飛行を利用できます。

**古いバージョンの PremiumFly からアップグレードしても大丈夫ですか？**
問題ありません。既存の付与データは、各プレイヤーが次回ログインした際に自動で移行されます。

## サポート

不具合報告・機能要望は [GitHub Issues](https://github.com/Takkunlego0916/PremiumFly/issues) までお願いします。

## ライセンス

PremiumFly は [MIT License](LICENSE) のもとで公開されています。自由に利用・改変・再配布いただけます。

## 作者

Created by **Takkunlego0916**

# LifeLog2

Minecraft (Paper 1.21.x) 用の軽量統計プラグインです。プレイヤーの **死亡回数** と **累計プレイ時間** を YAML ファイルに記録し、コマンドまたは GUI で閲覧できます。

## 特徴

* **自動トラッキング**

  * ログイン／ログアウトでセッション時間を計測し、累積プレイ時間 (hours) を保存
  * `PlayerDeathEvent` で死亡回数をインクリメント
* **ランキング表示**
  `/lifelog top`：死亡回数ランキング
  `/lifelog top playtime`：プレイ時間ランキング
* **個人統計表示**
  `/lifelog` または `/lifelog me` で自分の統計をチャットに表示
* **インゲーム GUI**
  `/lifelog gui` で Totem／Clock を使ったシンプルな 27 スロット GUI を開き、クリック無効化済み
* **ファイル保存 (stats.yml)**
  サーバーフォルダの `plugins/LifeLog2/` 内に自動生成される YAML に統計が保存されます
* **Scoreboard 不使用**
  要望に合わせてスコアボードは一切利用していません

## 動作環境

| 項目      | 推奨                |
| ------- | ----------------- |
| サーバーソフト | PaperMC 1.21.8 以降 |
| Java    | 17 以上             |
| Kotlin  | 2.2.x             |

## ビルド方法

```bash
git clone https://github.com/yourname/LifeLog2.git
cd LifeLog2
./gradlew shadowJar        # Windows の場合 gradlew.bat shadowJar
```

生成物: `build/libs/LifeLog2-<version>-all.jar`

## インストール手順

1. 上記 JAR を `plugins/` フォルダへ配置
2. サーバーを再起動 or `/reload confirm`
3. コンソールに `LifeLog2 enabled without scoreboard.` が出ればOK

## コマンド一覧

| コマンド                    | サブ引数        | 説明                |
| ----------------------- | ----------- | ----------------- |
| `/lifelog`              | (なし) / `me` | 自分の死亡回数・プレイ時間表示   |
| `/lifelog top`          |             | 死亡回数ランキング (上位10)  |
| `/lifelog top playtime` |             | プレイ時間ランキング (上位10) |
| `/lifelog gui`          |             | GUI で自分の統計を表示     |

### 権限

| パーミッション       | デフォルト          |
| ------------- | -------------- |
| `lifelog.use` | **true** (全員可) |

## データ形式 (`stats.yml`)

```yml
"<PlayerUUID>":
  deaths: <Long>
  playTicks: <Long>   # 1tick=1/20秒
```

## カスタマイズ

現バージョンでは設定ファイルはありません。PR・Issue で要望ください。

## 開発情報

* Kotlin 2.2.20-Beta2
* Gradle Shadow Plugin 8.3.0 (fat‑jar 生成)
* Paper API 1.21.8-SNAPSHOT

## ライセンス

[MIT License](LICENSE)

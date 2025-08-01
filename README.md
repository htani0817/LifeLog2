# LifeLog2

**LifeLog2** は Paper 1.21.8（**Java 21 必須**）向けの Kotlin 製プラグインです。  
過去ワールドの `stats/<UUID>.json` も読み込み、プレイヤーの **死亡回数** と **プレイ時間** をリアルタイム集計して可視化します。

---

## 主な機能

| 機能                | 説明                                                                                      |
|---------------------|-------------------------------------------------------------------------------------------|
| 死亡カウント        | `PlayerDeathEvent` で死亡ごとに 1 増分                                                   |
| プレイ時間計測      | Join から Quit までのセッションをティック単位で加算                                      |
| 履歴バックフィル    | サーバー起動時に `world/stats/*.json` を走査して初期値を生成                              |
| サイドバー表示      | Scoreboard API (`Criteria.DUMMY`) で「Top Deaths」を常時掲示                              |
| コマンド            | `/lifelog me`（自分の統計）・`/lifelog top`（死亡数上位10名）                             |
| データ永続化        | `plugins/LifeLog2/stats.yml` に UUID ごとの `deaths` と `playTicks` を保存                |

---

## 動作要件

* **Paper 1.21.8** 以降  
* **Java 21**  
* Kotlin 2.2.20-Beta2（Shadow Jar に stdlib 同梱）  
* 追加プラグイン不要

---

## ビルド

```bash
git clone https://github.com/<yourname>/LifeLog2.git
cd LifeLog2
./gradlew shadowJar          # → build/libs/LifeLog2-1.0-SNAPSHOT.jar

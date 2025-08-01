plugins {
    kotlin("jvm") version "2.2.20-Beta2"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")   // Paper API 1.21.8 :contentReference[oaicite:5]{index=5}
    implementation(kotlin("stdlib"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))        // Java 21 toolchain :contentReference[oaicite:6]{index=6}
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")                // 出力: LifeLog2.jar
    minimize()                               // 使わないクラスを除去
    relocate("kotlin", "${project.group}.lifelog.shadow.kotlin")  // stdlib 衝突回避
}
tasks.build { dependsOn("shadowJar") }

tasks.runServer {
    minecraftVersion("1.21.8")               // 実際に使うビルド番号で統一
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

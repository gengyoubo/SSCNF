# Shape Shifter Curse NeoForge

Minecraft 1.21.1 / NeoForge 21.1.259 / Java 21。

工程位于当前根目录，基础包名为 `github.com.gengyoubo.sscnf`。
使用项目自带的 Gradle 8.14.3 Wrapper；IDE 中也应选择 Wrapper 和 JDK 21。

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
.\gradlew.bat runServer
```

运行必需 GeckoLib 4.8.4；Curios 为可选饰品集成，JEI 为可选配方展示。
内置形态、能力、模型绑定和饰品能力通过 Java 注册；模型、动画及原版数据包资源保留为资源文件。

当前移植仍在进行，尚未通过完整编译和游戏启动验证。

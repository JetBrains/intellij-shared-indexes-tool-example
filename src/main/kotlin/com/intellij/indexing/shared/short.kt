package com.intellij.indexing.shared

import com.intellij.indexing.shared.builder.*
import java.nio.file.Paths

private val PROJECT_HOME = Paths.get("C:\\Users\\user\\workspace\\pet-project")
private val IJ_HOME =
    Paths.get("C:\\Users\\user\\AppData\\Local\\JetBrains\\Toolbox\\apps\\PyCharm-P\\ch-0\\231.8109.197")

fun short1() {
    // Runs project opening twice (without shared indexes and with shared indexes provided through local server)
    // and prints measured times.
    measureSharedIndexesBoost(PROJECT_HOME, IJ_HOME)
}

fun short2() {
    // example with many options omitted,
    // builds indexes using IJ_HOME and starts local server with them inside data directory.

    val dataDir = Paths.get(DEFAULT_DATA_DIR)

    val url = IntelliJSharedIndexes(dataDir.tempDir())
        .project(PROJECT_HOME)
        .using(IntelliJ.fromLocallyInstalled(IJ_HOME))
        .buildAndUpload(
            dataDir.serverDir(),
            "http://$DEFAULT_HOST:$DEFAULT_PORT"
        )

    println(
        "Put the following lines into ${PROJECT_HOME.intellijYaml()}:\n" +
                sharedIndexesConfigurationForIntelliJYaml(url)
    )

    startServerOnLocalIndexes(dataDir.serverDir())
}
package com.intellij.indexing.shared

import com.intellij.extras.downloader.IntelliJBasedProduct
import com.intellij.extras.runner.IntelliJOptions
import com.intellij.indexing.shared.builder.*
import com.intellij.indexing.shared.cdn.S3
import com.intellij.indexing.shared.cdn.metadata.IndexEntryCompression
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDate
import kotlin.io.path.createDirectories
import kotlin.io.path.div

fun long1() {
    // example with most options specified,
    // builds indexes using specified IDEs and uploads them to the configured S3,
    // similar example with fewer options and local server is below.

    val gradleBuildPath = Paths.get(".") / "build"
    val downloadsPath = (gradleBuildPath / "ij-cache").createDirectories()
    val unpacksPath = (gradleBuildPath / "ij-installations").createDirectories()
    val tempPath = (gradleBuildPath / "ij-temp").createDirectories()

    val projectHome = Paths.get("C:\\Users\\user\\workspace\\java-design-patterns")

    val url = IntelliJSharedIndexes(tempPath, downloadsPath, unpacksPath)
        .project("jdp", projectHome)
        .using(
            IntelliJ.fromUrl(
                "https://download.jetbrains.com/idea/ideaIU-2022.3.3.exe",
                "3e7856afba10f03f4dfab8d46db06f14e2915d87dc591e60aab49cc5890682fe"
            )
        )
        .using(
            listOf(
                // 2023.1 and latest 2023.1.x
                IntelliJ.byProductAndBuild(IntelliJBasedProduct.IDEAUltimate, "231.8109.175"),
                IntelliJ.byProductAndMajorVersion(IntelliJBasedProduct.IDEAUltimate, "231")
            )
        )
        .commit(
            // if not specified, `git rev-parse HEAD` is called
            "commit-hash-value"
        )
        .compression(IndexEntryCompression.PLAIN)
        .options(
            IntelliJOptions(
                xmx = "4g",
                environmentVariablesToAdd = mapOf(
                    // https://www.jetbrains.com/help/license_server/configure_automatic_server_discovery.html#configure-automatic-license-discovery
                    // "JETBRAINS_LICENSE_SERVER" to "url"
                )
            )
        )
        .timeout(
            // interrupt and fail if one of indexes dump takes more than 15 minutes
            Duration.ofMinutes(15)
        )
        .doNotUseDefaultConfigDirectory()
        .buildAndUpload(
            // default minio settings with `shared-indexes` bucket
            S3(
                "http://127.0.0.1:9000/shared-indexes",
                "shared-indexes",
                "http://127.0.0.1:9000",
            ),
            rebuildCdnIf = {
                // remove obsolete indexes every tenth day, see `buildAndUpload` method javadoc
                LocalDate.now().dayOfYear % 10 == 0
            },
            projectIndexesExpirationInDays = 15
        )

    println(
        "Put the following lines into ${projectHome.intellijYaml()}:\n" +
                sharedIndexesConfigurationForIntelliJYaml(url)
    )
}

fun long2() {
    val gradleBuildPath = Paths.get(".") / "build"
    val tempPath = (gradleBuildPath / "ij-temp").createDirectories()

    val projectHome = Paths.get("/home/user/workspace/java-design-patterns")
    val port = 45124

    val url = IntelliJSharedIndexes(tempPath)
        .project(projectHome)
        .using(
            IntelliJ.fromUrl(
                "https://download.jetbrains.com/idea/ideaIU-2022.3.3.tar.gz",
                "c302bd84b48a56ef1b0f033e8e93a0da5590f80482eae172db6130da035314a6"
            )
        )
        .using(
            listOf(
                // 2023.1 and latest 2023.1.x
                IntelliJ.byProductAndBuild(IntelliJBasedProduct.IDEAUltimate, "231.8109.175"),
                IntelliJ.byProductAndMajorVersion(IntelliJBasedProduct.IDEAUltimate, "231")
            )
        )
        .options(
            IntelliJOptions(
                environmentVariablesToAdd = mapOf(
                    // https://www.jetbrains.com/help/license_server/configure_automatic_server_discovery.html#configure-automatic-license-discovery
                    // "JETBRAINS_LICENSE_SERVER" to "url"
                )
            )
        )
        .buildAndUpload(
            gradleBuildPath.serverDir(),
            "http://127.0.0.1:$port",
            rebuildCdnIf = { LocalDate.now().dayOfYear % 10 == 0 }
        )

    println(
        "Put the following lines into ${projectHome.intellijYaml()}:\n" +
                sharedIndexesConfigurationForIntelliJYaml(url)
    )

    startServerOnLocalIndexes(gradleBuildPath.serverDir(), port = port)
}
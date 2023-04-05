package com.intellij.indexing.shared

import com.intellij.indexing.shared.builder.buildSharedIndexesVersion
import java.nio.file.Paths

fun main() {
    // displays version of shared indexes infrastructure in the specified IDE,
    // the same as `version` command in CLI.
    buildSharedIndexesVersion(
        Paths.get("C:\\Users\\user\\AppData\\Local\\JetBrains\\Toolbox\\apps\\PyCharm-P\\ch-0\\231.8109.197")
    )
}
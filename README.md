# IntelliJ Shared Indexes Tool Example

Here you can find examples of the IntelliJ shared indexes tool which
simplifies the process of building and uploading project shared indexes.

The main dependency here is `com.jetbrains.intellij.indexing.shared:ij-shared-indexes-tool`.
Also, you can play with the command line version located in [com.jetbrains.intellij.indexing.shared:ij-shared-indexes-tool-cli](https://packages.jetbrains.team/maven/p/ij/intellij-shared-indexes/com/jetbrains/intellij/indexing/shared/ij-shared-indexes-tool-cli/), just select a version and download a zip file.

These packages allow you to try and understand if shared indexes could be useful for your project.
If yes, they can help you to configure your environment for building and uploading project shared indexes either to a server running locally, or any S3-compatible storage.

Usages of `com.jetbrains.intellij.indexing.shared:ij-shared-indexes-tool` can be found in `main.kt`, `short.kt` and `long.kt`.

CLI can be unpacked and used in the following way: `./bin/ij-shared-indexes-tool-cli <command> <options>`.

Example: `./bin/ij-shared-indexes-tool-cli indexes-server --ij <IDE_DIR> --project <PROJECT_DIR>`.

Please read the [following](https://www.jetbrains.com/help/idea/shared-indexes.html#project-shared-indexes) documentation for more details.

If you have any questions or suggestions, don't hesitate to file an [issue](https://github.com/JetBrains/intellij-shared-indexes-tool-example/issues).
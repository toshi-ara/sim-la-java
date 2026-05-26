plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.beryx.jlink") version "3.2.0"
}

group = "io.github.toshiara"
val appName = "SimLocalAnesthetics"
val version = ""
val appVersion = "1.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}


val proguardRuntime by configurations.creating

dependencies {
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation("junit:junit:4.13.2")

    "proguardRuntime"("com.guardsquare:proguard-base:7.9.1")
    "proguardRuntime"("com.guardsquare:proguard-core:9.1.5")
    "proguardRuntime"("org.apache.logging.log4j:log4j-api:2.20.0")
    "proguardRuntime"("org.apache.logging.log4j:log4j-core:2.20.0")
}

val jarTask = tasks.named<Jar>("jar") {
    archiveBaseName.set(appName)
    archiveVersion.set("")
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "io.github.toshiara.simla.SimLocalAnesthetics")
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

val runProguard = tasks.register<JavaExec>("runProguard") {
    dependsOn(jarTask)
    mainClass.set("proguard.ProGuard")
    classpath = proguardRuntime
    args("@${project.projectDir}/proguard.pro")

    inputs.file(jarTask.get().archiveFile)
    outputs.file(layout.buildDirectory.file("libs/${appName}-minified.jar"))
}



val jlinkDir = layout.buildDirectory.dir("jlink-runtime")

tasks.register<Exec>("createJLinkImage") {
    dependsOn(runProguard)
    val outputDir = jlinkDir.get().asFile

    outputs.upToDateWhen { false }
    outputs.dir(jlinkDir)

    doFirst {
        if (outputDir.exists()) {
            outputDir.deleteRecursively()
        }
    }

    val javaHome = System.getProperty("java.home")
    val jlink = "$javaHome/bin/jlink"

    val modules = listOf(
        "java.base",
        "java.desktop"
    ).joinToString(",")

    commandLine(
        jlink,
        "--add-modules", modules,
        "--strip-debug",
        "--no-man-pages",
        "--no-header-files",
        "--compress", "zip-2",
        "--output", jlinkDir.get().asFile.absolutePath
    )
}


val distDir = layout.buildDirectory.dir("dist")

// 1. Linux: JAR + run script
tasks.register<Tar>("packageLinuxLight") {
    dependsOn("createJLinkImage")
    archiveFileName.set("SimLA-Linux-Portable.tar.bz2")
    destinationDirectory.set(distDir)

    compression = Compression.BZIP2
    from(layout.buildDirectory.file("libs/SimLocalAnesthetics-minified.jar"))

    doFirst {
        val runSh = file("${layout.buildDirectory.get()}/run.sh")
        runSh.writeText("""
            #!/bin/sh
            java -Xms64m -Dawt.useSystemAAFontSettings=on -jar "$(dirname "$0")/SimLocalAnesthetics-minified.jar"
        """.trimIndent())
        runSh.setExecutable(true)
    }
    from(layout.buildDirectory.file("run.sh"))
}

// 2. Linux: JAR + JRE + run script
tasks.register<Tar>("packageLinuxFull") {
    dependsOn("createJLinkImage")
    archiveFileName.set("SimLA-Linux-WithJRE.tar.bz2")
    destinationDirectory.set(distDir)

    compression = Compression.BZIP2
    from(layout.buildDirectory.dir("jlink-runtime")) {
        into("runtime")
    }
    from(layout.buildDirectory.file("libs/SimLocalAnesthetics-minified.jar"))

    doFirst {
        val runSh = file("${layout.buildDirectory.get()}/run.sh")
        runSh.writeText("""
            #!/bin/sh
            ./runtime/bin/java -Xms64m -Dawt.useSystemAAFontSettings=on -jar "$(dirname "$0")/SimLocalAnesthetics-minified.jar"
        """.trimIndent())
        runSh.setExecutable(true)
    }
    from(layout.buildDirectory.file("run.sh"))
}


// 3. Windows: JAR + JRE + run script
tasks.register<Exec>("createJPackage") {
    dependsOn("runProguard")
    dependsOn("createJLinkImage")

    val outputDir = layout.buildDirectory.dir("jpackage").get().asFile
    val stagingDir = layout.buildDirectory.dir("jpackage-staging").get().asFile

    doFirst {
        if (outputDir.exists()) outputDir.deleteRecursively()
        if (stagingDir.exists()) stagingDir.deleteRecursively()
        stagingDir.mkdirs()

        val minifiedJar = layout.buildDirectory.file("libs/SimLocalAnesthetics-minified.jar").get().asFile
        if (minifiedJar.exists()) {
            minifiedJar.copyTo(stagingDir.resolve(minifiedJar.name))
        } else {
            throw GradleException("Minified JAR not found!")
        }
    }

    val javaHome = System.getProperty("java.home")
    commandLine(
        "$javaHome/bin/jpackage",
        "--name", "SimLocalAnesthetics",
        "--input", stagingDir.absolutePath,
        "--main-jar", "SimLocalAnesthetics-minified.jar",
        "--main-class", "io.github.toshiara.simla.SimLocalAnesthetics",
        "--runtime-image", jlinkDir.get().asFile.absolutePath,
        "--dest", outputDir.absolutePath,
        "--type", "app-image"
    )
}

tasks.register<Zip>("packageWindowsFull") {
    dependsOn("createJPackage")
    archiveFileName.set("SimLA-Windows-WithJRE.zip")
    destinationDirectory.set(distDir)

    from(layout.buildDirectory.dir("jpackage/SimLocalAnesthetics")) {
        into(".")
    }
}


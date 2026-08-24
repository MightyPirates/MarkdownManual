val modId: String by project
val minecraftVersion: String = libs.versions.minecraft.get()
val neoforgeVersion: String = libs.versions.neoforge.platform.get()
val neoforgeLoaderVersion: String = libs.versions.neoforge.loader.get()
val architecturyVersion: String = libs.versions.architectury.get()

val devOnlyMods: Configuration by configurations.creating

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    runs {
        named("client") { runDir = "run/client" }
        named("server") { runDir = "run/server" }
    }
}

repositories {
    maven("https://maven.neoforged.net/releases")
}

configurations.named("modRuntimeOnly") { extendsFrom(devOnlyMods) }

dependencies {
    neoForge(libs.neoforge.platform)
    modImplementation(libs.neoforge.architectury)

    // Allows `remapSourcesJar` to resolve `@ExpectPlatform` in the common sources it bundles.
    compileOnly(libs.architectury.injectables)

    // Not used by mod, just for dev convenience.
    devOnlyMods(libs.jei.neoforge)
}

tasks {
    processResources {
        val properties = mapOf(
            "version" to project.version,
            "minecraftVersion" to minecraftVersion,
            "loaderVersion" to neoforgeLoaderVersion,
            "neoforgeVersion" to neoforgeVersion,
            "architecturyVersion" to architecturyVersion
        )
        inputs.properties(properties)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(properties)
        }
    }

    remapJar {
        atAccessWideners.add("${modId}.accesswidener")
    }
}

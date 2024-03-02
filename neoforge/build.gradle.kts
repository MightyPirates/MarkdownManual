val modId: String by project
val minecraftVersion: String = libs.versions.minecraft.get()
val neoforgeVersion: String = libs.versions.neoforge.platform.get()
val neoforgeLoaderVersion: String = libs.versions.neoforge.loader.get()
val architecturyVersion: String = libs.versions.architectury.get()

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    neoForge {
        //convertAccessWideners.set(true)
        //extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    neoForge(libs.neoforge.platform)
    modApi(libs.neoforge.architectury)
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
        filesMatching("META-INF/mods.toml") {
            expand(properties)
        }
    }
}

val modId: String by project
val minecraftVersion: String = libs.versions.minecraft.get()
val fabricApiVersion: String = libs.versions.fabric.api.get()
val architecturyVersion: String = libs.versions.architectury.get()

val devOnlyMods: Configuration by configurations.creating

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations.named("modRuntimeOnly") { extendsFrom(devOnlyMods) }

dependencies {
    modImplementation(libs.fabric.loader)
    modApi(libs.fabric.api)
    modApi(libs.fabric.architectury)

    // Allows `remapSourcesJar` to resolve `@ExpectPlatform` in the common sources it bundles.
    compileOnly(libs.architectury.injectables)

    // Not used by mod, just for dev convenience.
    devOnlyMods(libs.jei.fabric)
}

tasks {
    processResources {
        val properties = mapOf(
            "version" to project.version,
            "minecraftVersion" to minecraftVersion,
            "fabricApiVersion" to fabricApiVersion,
            "architecturyVersion" to architecturyVersion
        )
        inputs.properties(properties)
        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }

    remapJar {
        injectAccessWidener.set(true)
    }
}

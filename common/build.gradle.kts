val enabledPlatforms: String by project
val modId: String by project

architectury {
    common(enabledPlatforms.split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/${modId}.accesswidener"))
}

dependencies {
    modImplementation(libs.fabric.loader)
    modApi(libs.architectury.api)
}

tasks {
    register<Jar>("apiJar") {
        from(sourceSets.main.get().allSource)
        from(sourceSets.main.get().output)
        archiveClassifier.set("api")
        include("li/cil/manual/api/**")
    }

    jar {
        dependsOn("apiJar")
    }
}

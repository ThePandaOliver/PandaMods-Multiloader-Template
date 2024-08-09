// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
    getByName("developmentFabric").extendsFrom(configurations["common"])
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":common", "transformProductionFabric")) { isTransitive = false }
}

tasks {
    base.archivesName.set(base.archivesName.get() + "-Fabric")

    remapJar {
        injectAccessWidener.set(true)
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}
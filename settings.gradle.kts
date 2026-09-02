pluginManagement.repositories.mavenLocal()
pluginManagement.repositories.mavenCentral()
pluginManagement.repositories.gradlePluginPortal()

// ── MEM-CAT-ROLLOUT-5 — Catalog workspace published (MEMPHIS): single pin per borough (D4) ──
// education.cccp:workspace-catalog:0.0.31 — cross-borough source of truth for plugin versions.
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("ws") {
            from("education.cccp:workspace-catalog:0.0.31")
        }
    }
}

rootProject.name = "runner-gradle"
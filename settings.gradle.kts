pluginManagement.repositories.mavenLocal()
pluginManagement.repositories.mavenCentral()
pluginManagement.repositories.gradlePluginPortal()

// ── MEM-CAT-ROLLOUT-5 — Catalog workspace served by JitPack (CNV-17) ──
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("ws") {
            from("com.github.cccp-education.workspace-bom:workspace-catalog:0.0.136")
        }
    }
}
// CNV-16 — JitPack mirror for the education.cccp.build.* conventions
// (bypasses Sonatype / Gradle Plugin Portal). The JitPack tag is pinned while
// the canonical plugin declarations may stay on the released version.
pluginManagement.repositories.maven("https://jitpack.io")
pluginManagement.resolutionStrategy.eachPlugin {
    when {
        requested.id.id.startsWith("education.cccp.build.") ->
            useModule("com.github.cccp-education.conventions-gradle:conventions-plugin:0.0.17")

        // CNV-16 — bakery 0.0.36 (BKY-CI-MIRROR / pushCiMirror) is served from
        // JitPack (Central quota blocked, Portal not the pilot target).
        requested.id.id == "education.cccp.bakery" ->
            useModule("com.github.cccp-education.bakery-gradle:bakery-plugin:0.0.39")
    }
}

rootProject.name = "runner-gradle"
dependencyResolutionManagement.repositories.maven("https://jitpack.io")

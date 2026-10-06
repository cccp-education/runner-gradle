// Fix conflit classpath : Gradle 9.6.1 épingle le Kotlin embarqué (strictly 2.3.21)
// en conflit avec les constraints platform workspace-bom (2.4.10) tirées transitivement
// via bakery-plugin → document-plugin → plantuml-plugin → workspace-bom.
buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.4.10")
            force("org.jetbrains:annotations:26.0.2-1")
            // The bakery-plugin POM pins its build-time workspace-bom; the runner
            // aligns the whole classpath on the latest published BOM so every site
            // baked through N3 resolves the newest constraints (bakery 0.0.22 now
            // pins BOM 0.0.65 itself → codebase-plugin 0.0.17).
            force("education.cccp:workspace-bom:0.0.65")
        }
    }
}

plugins {
    // MEM-CAT-ROLLOUT-5 (D7/C7) — alias from the published workspace catalog: no hardcoded
    // version anymore, the cross-borough source of truth drives the bakery plugin version.
    alias(ws.plugins.bakery)
}

val siteName: String = project.findProperty("siteName") as String?
    ?: throw GradleException("siteName property required. Usage: -PsiteName=<domain>")
val officePath = System.getenv("OFFICE_PATH") ?: "${System.getProperty("user.home")}/workspace/office"
val resolvedConfigPath: String = file("$officePath/sites/$siteName/site.yml").absolutePath

bakery { configPath = resolvedConfigPath }


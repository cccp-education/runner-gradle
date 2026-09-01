// Fix conflit classpath : Gradle 9.6.1 épingle le Kotlin embarqué (strictly 2.3.21)
// en conflit avec les constraints platform workspace-bom (2.4.10) tirées transitivement
// via bakery-plugin → document-plugin → plantuml-plugin → workspace-bom.
buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.4.10")
            force("org.jetbrains:annotations:26.0.2-1")
        }
    }
}

plugins {
    id("education.cccp.bakery") version "0.0.9"
}

val siteName: String = project.findProperty("siteName") as String?
    ?: throw GradleException("siteName property required. Usage: -PsiteName=<domain>")
val officePath = System.getenv("OFFICE_PATH") ?: "${System.getProperty("user.home")}/workspace/office"
val resolvedConfigPath: String = file("$officePath/sites/$siteName/site.yml").absolutePath

bakery { configPath = resolvedConfigPath }


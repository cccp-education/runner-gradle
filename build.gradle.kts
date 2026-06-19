plugins {
    id("education.cccp.bakery") version "0.0.1"
}

val siteName: String = project.findProperty("siteName") as String?
    ?: throw GradleException("siteName property required. Usage: -PsiteName=<domain>")
val officePath = System.getenv("OFFICE_PATH") ?: "${System.getProperty("user.home")}/workspace/office"
val resolvedConfigPath: String = file("$officePath/sites/$siteName/site.yml").absolutePath

bakery { configPath = resolvedConfigPath }

tasks.register("publishSite") {
    group = "publish"
    description = "Bake + deploy $siteName"
    dependsOn("bake", "deploySite")
}

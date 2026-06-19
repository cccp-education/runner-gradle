plugins {
    id("education.cccp.bakery") version "0.0.1"
}

val siteName: String = project.findProperty("siteName") as String?
    ?: throw GradleException("siteName property required. Usage: -PsiteName=<domain>")
val resolvedConfigPath = file("${System.getenv("OFFICE_PATH") ?: throw GradleException("OFFICE_PATH not set")}/sites/$siteName/site.yml").absolutePath

bakery { configPath = resolvedConfigPath }

tasks.register("publishSite") {
    group = "publish"
    description = "Bake + deploy $siteName"
    dependsOn("bake", "deploySite")
}

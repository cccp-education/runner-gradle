plugins {
    id("education.cccp.bakery") version "0.0.1"
}

val siteName: String = project.findProperty("siteName") as String? ?: "cheroliv.com"

bakery { configPath = file("${System.getenv("OFFICE_PATH") ?: throw GradleException("OFFICE_PATH not set")}/sites/$siteName/site.yml").absolutePath }

tasks.register("publishSite") {
    group = "publish"
    description = "Bake + deploy $siteName"
    dependsOn("bake", "deploySite")
}

plugins {
    id("education.cccp.bakery") version "0.0.1"
}

bakery { configPath = file("${System.getenv("OFFICE_PATH") ?: throw GradleException("OFFICE_PATH not set")}/sites/cheroliv.com/site.yml").absolutePath }

tasks.register("publishSite") {
    group = "publish"
    description = "Bake + deploy cheroliv.com"
    dependsOn("bake", "deploySite")
}

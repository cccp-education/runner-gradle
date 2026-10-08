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
            // pins BOM 0.0.65 itself -> codebase-plugin 0.0.17). S-282 : aligned on BOM 0.0.69 (document 0.0.21, fix D2 CHE-DIAGRAM).
            force("education.cccp:workspace-bom:0.0.70")
            // PLT-DIAGRAM-OWNERSHIP US-5 (S-221) — CHE-DIAGRAM D1 (white diagrams).
            // The bake classpath pulls `net.sf.saxon:Saxon-HE:11.4` via
            // `document-plugin -> epubcheck:5.2.1`. PlantUML serialises its SVG
            // through that Saxon: 11.4 emits `xmlns=''` + hex entities on the root
            // child elements, which makes the browser stop rendering the SVG
            // (uniform white). Saxon 9.9.1-7 emits a correct SVG (visible render,
            // σ≈8700). Proven by fresh re-bake (cache cleared): 11.4 → 0 visible,
            // 9.9.1-7 → all diagrams visible.
            force("net.sf.saxon:Saxon-HE:9.9.1-7")
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

// S-064 (décision pilote) — OPT-IN single-endpoint LLM override.
//
// The site's `ollama:` section is an N0 contract ([contracts.i18n.OllamaConfig])
// that guards the port range: `portStart must be >= 11437`. A port outside the
// pool (e.g. 11434, whose account still has quota while the pool is exhausted)
// cannot be expressed through the YAML. The bakery DSL `ia { }` with
// `enabled = true` is never overridden by the YAML (IaConfigResolver precedence),
// and a single endpoint uses the non-pooled LlmService — so it bypasses the
// contract port guard by construction.
//
// Default: absent → the legal pool from `site.yml` is used (backward compat).
// Usage: `-PllmSingleEndpoint=http://localhost:11434 [-PllmModel=...]`.
val llmSingleEndpoint: String? = project.findProperty("llmSingleEndpoint") as String?
if (llmSingleEndpoint != null) {
    bakery {
        ia {
            baseUrl = llmSingleEndpoint
            modelName = (project.findProperty("llmModel") as String?) ?: "nemotron-3-super:cloud"
            enabled = true
            timeout = java.time.Duration.ofSeconds(300)
        }
    }
}


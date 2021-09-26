package com.kyleduo.aladdin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin used to remove Aladdin related code blocks and classes since Aladdin is aimed to
 * help developers during development and debugging phase.
 */
class AladdinReleasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
    }
}
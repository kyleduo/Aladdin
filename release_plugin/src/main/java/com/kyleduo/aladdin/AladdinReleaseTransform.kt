package com.kyleduo.aladdin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import org.gradle.api.Project

/**
 * @author kyleduo on 2021/9/26
 */
class AladdinReleaseTransform(private val project: Project) : Transform() {

    private val logger = project.logger

    override fun getName(): String = "AladdinReleasePlugin"

    override fun isIncremental(): Boolean = true

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        transformInvocation ?: return
        transformInvocation.inputs.forEach {
            it.jarInputs.forEach { jar ->
                logger.lifecycle(jar.toString())
            }

            it.directoryInputs.forEach { dir ->
                logger.lifecycle(dir.toString())
            }
        }
    }
}

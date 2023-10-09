package com.example.mybookshelf.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withoutNameStartingWith
import com.lemonappdev.konsist.api.verify.assert
import kotlinx.serialization.Serializable
import org.junit.Test

class ArchitectureTest {
    @Test
    fun `architecture test - testing here`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                // Layers of architecture defined here
                val network = Layer("Network", "com.example.mybookshelf.network..")
                val presentation = Layer("Presentation", "com.example.mybookshelf.ui..")
                val model = Layer("Model", "com.example.mybookshelf.model..")
                val data = Layer("Data", "com.example.mybookshelf.data..")

            // Separation of layers defined here
                presentation.dependsOn(model, data)
                data.dependsOn(model, network)
                network.dependsOn(model)
            }
    }

    @Test
    fun `'Repository' classes should reside in 'data' package, unless fake`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Repository")
            .withoutNameStartingWith("Fake")
            .assert { it.resideInPackage("com.example.mybookshelf.data") }
    }

    @Test
    fun `all classes with 'serializable' annotation reside in model package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(Serializable::class)
            .properties()
            .assert { it.resideInPackage("com.example.mybookshelf.model") }
    }
}

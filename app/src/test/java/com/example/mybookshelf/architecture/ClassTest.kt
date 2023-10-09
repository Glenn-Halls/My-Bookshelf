package com.example.mybookshelf.architecture

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withParentOf
import com.lemonappdev.konsist.api.verify.assert
import org.junit.Test

class ClassTest{
    @Test
    fun `ensure all interfaces in network package are labelled with 'Api' in class name`() {
        Konsist
            .scopeFromPackage("com.example.myBookshelf.network")
            .interfaces()
            .assert { it.hasNameContaining("Api") }
    }

    @Test
    fun `ensure classes extending ViewModel have 'ViewModel' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(ViewModel::class)
            .assert { it.name.endsWith("ViewModel") }
    }
}

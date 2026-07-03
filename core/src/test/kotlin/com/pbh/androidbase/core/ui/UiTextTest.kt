package com.pbh.androidbase.core.ui

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class UiTextTest {
    @Test
    fun `dynamic text returns value without reading context`() {
        val context = mockk<Context>(relaxed = true)

        assertEquals("Ready", UiText.Dynamic("Ready").asString(context))
    }

    @Test
    fun `resource text resolves through context`() {
        val context = mockk<Context>()
        every { context.getString(42) } returns "Localized"

        assertEquals("Localized", UiText.Resource(42).asString(context))
    }
}

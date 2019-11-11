// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.i8n

import kute.i18n.isValid
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class LocaleTest {
    @Test
    fun `Locale#US is valid locale`() {
        assertTrue(Locale.US.isValid())
    }

    @Test
    fun `Locale#ENGLISH is valid locale`() {
        assertTrue(Locale.ENGLISH.isValid())
    }

    @Test
    fun `Locale 'en-US' is valid locale`() {
        assertTrue(Locale.forLanguageTag("en-US").isValid())
    }

    @Test
    fun `Locale 'en' is valid locale`() {
        assertTrue(Locale.forLanguageTag("en").isValid())
    }

    @Test
    fun `Locale 'xd-XD' is invalid locale`() {
        assertFalse(Locale.forLanguageTag("xd-XD").isValid())
    }

    @Test
    fun `Locale 'en-XD' is invalid locale`() {
        assertFalse(Locale.forLanguageTag("en-XD").isValid())
    }

    @Test
    fun `Locale 'xd-US' is invalid locale`() {
        assertFalse(Locale.forLanguageTag("xd-US").isValid())
    }

    @Test
    fun `Locale '' is invalid locale`() {
        assertFalse(Locale.forLanguageTag("").isValid())
    }
}
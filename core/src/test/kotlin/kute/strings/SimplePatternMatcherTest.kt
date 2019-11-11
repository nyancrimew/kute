// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.strings

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalStdlibApi
class SimplePatternMatcherTest {
    @Test
    fun `Basic start anchored pattern`() {
        val matcher = SimplePatternMatcher("Hel")

        assertTrue(matcher.matches("Hello"))
        assertTrue(matcher.matches("Hel"))
        assertFalse(matcher.matches("AHel"))
        assertFalse(matcher.matches("He"))
    }

    @Test
    fun `Wildcard start anchored pattern`() {
        val matcher = SimplePatternMatcher("Hel*")

        assertTrue(matcher.matches("Hello"))
        assertTrue(matcher.matches("Hel"))
        assertFalse(matcher.matches("AHel"))
        assertFalse(matcher.matches("He"))
    }

    @Test
    fun `End anchored pattern`() {
        val matcher = SimplePatternMatcher("*Hel")

        assertTrue(matcher.matches("Hel"))
        assertTrue(matcher.matches("AHel"))
        assertFalse(matcher.matches("Hello"))
        assertFalse(matcher.matches("He"))
    }

    @Test
    fun `Unanchored pattern`() {
        val matcher = SimplePatternMatcher("*Hel*")

        assertTrue(matcher.matches("Hello"))
        assertTrue(matcher.matches("Hel"))
        assertTrue(matcher.matches("AHel"))
        assertTrue(matcher.matches("AHello"))
        assertFalse(matcher.matches("Test"))
    }

    @Test
    fun `Pattern with wildcard`() {
        val matcher = SimplePatternMatcher("H?l*")

        assertTrue(matcher.matches("Hello"))
        assertTrue(matcher.matches("Hallo"))
        assertTrue(matcher.matches("Hel"))
        assertFalse(matcher.matches("Test"))
        assertFalse(matcher.matches("AHel"))
    }

    @Test
    fun `End Anchored pattern with wildcard`() {
        val matcher = SimplePatternMatcher("*H?l")

        assertTrue(matcher.matches("Hel"))
        assertTrue(matcher.matches("Oh Hel"))
        assertTrue(matcher.matches("Oh Hal"))
        assertTrue(matcher.matches("AHel"))
        assertFalse(matcher.matches("Test"))
        assertFalse(matcher.matches("Hello"))
    }

    @Test
    fun `Wildcard matches anything`() {
        val matcher = SimplePatternMatcher("*")

        assertTrue(matcher.matches("Hel"))
        assertTrue(matcher.matches("Oh Hel"))
        assertTrue(matcher.matches("Oh Hal"))
        assertTrue(matcher.matches("AHel"))
        assertTrue(matcher.matches("Test"))
        assertTrue(matcher.matches("Hello"))
    }

    @Test
    fun `Single char wildcard matches anything with min length`() {
        val matcher = SimplePatternMatcher("??")

        assertTrue(matcher.matches("Hel"))
        assertTrue(matcher.matches("Oh Hel"))
        assertTrue(matcher.matches("Oh Hal"))
        assertTrue(matcher.matches("AHel"))
        assertTrue(matcher.matches("Test"))
        assertTrue(matcher.matches("Hello"))
        assertFalse(matcher.matches("H"))
    }

    @Test
    fun `Inline wildcard matches lazily`() {
        val matcher = SimplePatternMatcher("Hel*d")

        assertTrue(matcher.matches("Hello World"))
        assertTrue(matcher.matches("Hell has a lot of fund"))
        assertTrue(matcher.matches("Held"))
        assertFalse(matcher.matches("AHel"))
        assertFalse(matcher.matches("Test"))
        assertFalse(matcher.matches("Hello"))
        assertFalse(matcher.matches("H"))
    }

    @Test
    fun `Inline wildcard matches minimum char based on single wildcard`() {
        val matcher = SimplePatternMatcher("Hel*?d")

        assertTrue(matcher.matches("Hello World"))
        assertTrue(matcher.matches("Hell has a lot of fund"))
        assertFalse(matcher.matches("Held"))
        assertFalse(matcher.matches("AHel"))
        assertFalse(matcher.matches("Test"))
        assertFalse(matcher.matches("Hello"))
    }

    @Test
    fun `Characters can be escaped`() {
        val matcher = SimplePatternMatcher("Hello\\*You\\?")

        assertTrue(matcher.matches("Hello*You?"))
        assertTrue(matcher.matches("Hello*You? Guys"))
        assertFalse(matcher.matches("Hello*You!"))
        assertFalse(matcher.matches("Hello You?"))

        val matcher2 = SimplePatternMatcher("Hello\\You\\\\")
        assertTrue(matcher2.matches("HelloYou\\"))
        assertTrue(matcher2.matches("HelloYou\\ Test"))
        assertFalse(matcher2.matches("HelloYou"))
        assertFalse(matcher2.matches("HelloYou "))

        val matcher3 = SimplePatternMatcher("Hello?\\You")
        assertTrue(matcher3.matches("HelloaYou"))
        assertTrue(matcher3.matches("Hello You Test"))
        assertFalse(matcher3.matches("HelloYou"))
        assertFalse(matcher3.matches("HelloYou\\"))
    }

    @Test
    fun `Complex patterns`() {
        val matcher = SimplePatternMatcher("*H?llo*W*l?")

        assertTrue(matcher.matches("Hallo Welt"))
        assertTrue(matcher.matches("Hello World"))
        assertTrue(matcher.matches("Hello Weeeeeeeeeeeeeeeld"))
        assertTrue(matcher.matches("Example: Hello World"))
        assertFalse(matcher.matches("Hello World & Moon"))
        assertFalse(matcher.matches("Hello Worlld"))

        val matcher2 = SimplePatternMatcher("*H?llo*W*?*")

        assertTrue(matcher2.matches("Hallo Welten"))
        assertTrue(matcher2.matches("Hello World"))
        assertTrue(matcher2.matches("Hello Weeeeeeeeeeeeeeeld"))
        assertTrue(matcher2.matches("Example: Hello World"))
        assertTrue(matcher2.matches("Hello World & Moon"))
        assertTrue(matcher2.matches("Hello Worlld"))
        assertTrue(matcher2.matches("Hello W"))
        assertFalse(matcher2.matches("Bello World"))
    }
}
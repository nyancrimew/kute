// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.collections

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class CollectionsTest {

    @Test
    fun `Replace values in list`() {
        val list = listOf(1, 2, 3, 3, 2, 4, 3)

        assertThat(list.replace(3, 9), equalTo(listOf(1, 2, 9, 9, 2, 4, 9)))
    }

    @Test
    fun `Double average of values in list by selector`() {
        val list = listOf(1, 2, 3, 4)
        val avg = list.avgByDouble { it.toDouble() }

        assertThat(avg, equalTo(2.5))
    }

    @Test
    fun `Int average of values in list by selector`() {
        val list = listOf(1, 2, 3, 4)
        val avg = list.avgBy { it }

        assertThat(avg, equalTo(2))
    }

    @Test
    fun `Double average of values in list`() {
        val list = listOf(1.0, 2.0, 3.0, 4.0)
        val avg = list.avg()

        assertThat(avg, equalTo(2.5))
    }

    @Test
    fun `Int average of values in list`() {
        val list = listOf(1, 2, 3, 4)
        val avg = list.avg()

        assertThat(avg, equalTo(2))
    }

    @Test
    fun `Double average of values in int list`() {
        val list = listOf(1, 2, 3, 4)
        val avg = list.doubleAvg()

        assertThat(avg, equalTo(2.5))
    }
}
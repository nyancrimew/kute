// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.math

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import kotlin.math.PI

class DoubleTest {
    @Test
    fun `round pi to 2 decimals`() {
        assertThat(PI.roundToDecimals(2), equalTo(3.14))
    }

    @Test
    fun `round 2,1 to 2 decimals`() {
        assertThat(2.1.roundToDecimals(2), equalTo(2.1))
    }
}
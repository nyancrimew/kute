// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

interface PreferenceValueEncoder<T> {
    fun encode(value: T): String
    fun decode(string: String): T
}
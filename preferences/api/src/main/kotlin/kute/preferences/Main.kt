// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

fun main() {
    val prefs = Preferences(InMemoryStore())
    var aa by prefs.test
    aa = "lolo"
    println(aa)
    println(prefs.test.key)
}
// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

class Preferences(val store: PreferenceStore) {
    val test by Preference(store, null, "")
}

fun test() {
    val prefs = Preferences(InMemoryStore())
    
    prefs.test { key, oldValue, newValue ->  
        println(key)
    }

    val value by prefs.test
}
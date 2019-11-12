// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

interface PreferenceStore {
    fun <T> get(key: String, default: T): T
    operator fun <T> set(key: String, value: T)
    fun <T> observe(key: String, default: T, observer: PreferenceObserver<T>)
}
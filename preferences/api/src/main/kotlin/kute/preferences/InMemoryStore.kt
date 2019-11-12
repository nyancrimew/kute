// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

class InMemoryStore : PreferenceStore {
    private val map = mutableMapOf<String, Any?>()
    override fun <T> get(key: String, default: T): T = (map[key] as T?) ?: default

    override fun <T> set(key: String, value: T) {
        map[key] = value
    }

    override fun <T> observe(key: String, default: T, observer: PreferenceObserver<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
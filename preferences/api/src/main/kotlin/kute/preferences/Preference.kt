// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceDelegate<T>(private val pref: Preference<T>) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = pref.get(property)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = pref.set(property, value)
}

open class Preference<T>(private val store: PreferenceStore, internal var key: String?, private val default: T) {
    private val delegate = PreferenceDelegate(this)

    inline operator fun getValue(thisRef: Preferences, property: KProperty<*>): Preference<T> = this

    operator fun provideDelegate(thisRef: Preferences, property: KProperty<*>): Preference<T>  = this.also {
        key = key ?: property.name
    }

    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): PreferenceDelegate<T>  = delegate

    inline operator fun invoke(crossinline observer: PreferenceObserver<T>) {

    }

    internal fun get(property: KProperty<*>): T = get(store, key ?: property.name)
    internal fun set(property: KProperty<*>, value: T) = set(store, key ?: property.name, value)

    open fun get(store: PreferenceStore, key: String): T = store.get(key, default)
    open fun set(store: PreferenceStore, key: String, value: T) {
        store[key] =  value
    }
}

class EncoderPreference<T>(store: PreferenceStore, key: String?, default: T, private val encoder: PreferenceValueEncoder<T>): Preference<T>(store, key, default) {
    private val encodedDefault = encoder.encode(default)

    override fun get(store: PreferenceStore, key: String): T = encoder.decode(store.get(key, encodedDefault))
    override fun set(store: PreferenceStore, key: String, value: T) {
        store[key] = encoder.encode(value)
    }
}
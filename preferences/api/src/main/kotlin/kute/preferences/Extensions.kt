// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

fun <T> Preferences.preference(key: String? = null, default: T) = Preference(store, key, default)
fun Preferences.string(key: String? = null) = preference(key, "")
fun Preferences.int(key: String? = null, default: Int = 0) = preference<Int>(key, default)
fun Preferences.long(key: String? = null, default: Long = 0L) = preference<Long>(key, default)
fun Preferences.double(key: String? = null, default: Double = 0.0) = preference<Double>(key, default)
fun Preferences.float(key: String? = null, default: Float = 0f) = preference<Float>(key, default)
fun Preferences.bool(key: String? = null, default: Boolean = false) = preference<Boolean>(key, default)

fun <T> Preferences.nullable(key: String? = null, default: T? = null) = Preference(store, key, default)
fun Preferences.nullable(key: String? = null, default: String? = "") = nullable<String>(key, default)
fun Preferences.nullable(key: String? = null, default: Int? = 0) = nullable<Int>(key, default)
fun Preferences.nullable(key: String? = null, default: Long? = 0L) = nullable<Long>(key, default)
fun Preferences.nullable(key: String? = null, default: Double? = 0.0) = nullable<Double>(key, default)
fun Preferences.nullable(key: String? = null, default: Float? = 0f) = nullable<Float>(key, default)
fun Preferences.nullable(key: String? = null, default: Boolean? = false) = nullable<Boolean>(key, default)

fun <T> Preferences.preference(key: String? = null, default: T, encoder: PreferenceValueEncoder<T>) = EncoderPreference(store, key, default, encoder)

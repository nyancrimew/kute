// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

@file:Suppress("NOTHING_TO_INLINE")

package kute.collections

/**
 * Returns a list containing the result of replacing each occurence of [value] with [replacement].
 */
inline fun <T> Iterable<T>.replace(value: T, replacement: T) = map { if (it == value) replacement else it }

/**
 * Returns the average of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Collection<T>.avgByDouble(crossinline selector: (T) -> Double): Double = sumByDouble(selector) / size

/**
 * Returns the average of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Collection<T>.avgBy(crossinline selector: (T) -> Int): Int = sumBy(selector) / size

/**
 * Returns the average of all values in the collection.
 */
inline fun Collection<Double>.avg(): Double = sum() / size

/**
 * Returns the average of all values in the collection.
 */
inline fun Collection<Int>.avg(): Int = sum() / size

/**
 * Returns the average of all values in the collection.
 */
inline fun Collection<Int>.doubleAvg(): Double = sum() / size.toDouble()
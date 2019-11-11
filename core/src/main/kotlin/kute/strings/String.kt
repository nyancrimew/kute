// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.strings

val CharSequence.lineCount get() = lines().count()

fun <T : CharSequence> T?.emptyAsNull(): T? = if (isNullOrEmpty()) null else this

fun <T : CharSequence> T?.blankAsNull(): T? = if (isNullOrBlank()) null else this

// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.preferences

typealias PreferenceObserver <T> = (key: String, oldValue: T?, newValue: T?) -> Unit
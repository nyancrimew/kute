// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.i18n

import java.util.*

/**
 * Check whether this locale is valid.
 * A locale is considered valid, if it has a valid ISO3 language and optionally a (valid) ISO3 country.
 */
fun Locale.isValid() = try {
    (!language.isNullOrBlank() && isO3Language != null) && (country.isNullOrBlank() || isO3Country != null)
} catch (ignored: MissingResourceException) {
    false
}
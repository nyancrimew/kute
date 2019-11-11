// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.ktor

import io.ktor.application.ApplicationCall
import io.ktor.request.acceptLanguageItems
import kute.i18n.isValid
import java.util.*

/**
 * Attempts to convert the entries of the `Accept-Language` header to a [Locale].
 * Returns the first successfully converted locale, or null.
 */
val ApplicationCall.locale
    get() = request.acceptLanguageItems().map { Locale.forLanguageTag(it.value) }.firstOrNull { it.isValid() }
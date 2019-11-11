// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.ktor

import io.ktor.http.HttpHeaders
import io.ktor.request.ApplicationRequest
import io.ktor.request.header
import io.ktor.request.userAgent

/**
 * @see @see [The W3C Working Group Notes on DNT](https://www.w3.org/TR/tracking-dnt/)
 */
val HttpHeaders.DNT get() = "DNT"

/**
 * The `DNT` header is a mechanism for expressing a user's preference regarding tracking.
 *
 * A value of `true` means that the user opts out of tracking.
 *
 * The default value you will get from most User Agents is `null` (unset Header), most applications treat this the
 * same way as `false` (user opts in to tracking), though there is no official guidance on how an unset header should be treated.
 *
 * @see [The W3C Working Group Notes on DNT](https://www.w3.org/TR/tracking-dnt/)
 * @see [EFF's Guide on implementing DNT](https://github.com/EFForg/dnt-guide#eff-how-to-implement-dnt-guide)
 */
val ApplicationRequest.dnt get(): Boolean? = header(HttpHeaders.DNT)?.startsWith('1')

// Simple regex to detect some common commercial crawlers
// This currently doesn't try to detect crawling/http libraries
// Data from: https://github.com/monperrus/crawler-user-agents
private const val botRegexPattern =
    "(bot|spider|crawl|feedfetcher|mediapartners|apis-google|slurp|convera|ia_archiver|yandeximages)"
private val botRegex = botRegexPattern.toRegex(setOf(RegexOption.IGNORE_CASE))

/**
 * Checks the requesting UserAgent against a regex, which is able to detect most common, commercial crawlers and bots
 *
 * This currently doesn't explicitly try to detect http libraries/tools
 */
val ApplicationRequest.isBot get() = userAgent()?.contains(botRegex) == true
// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.ktor

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test

class CallTest {
    @Test
    fun `Language de-DE is correctly detected`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertThat(call.locale!!.country, equalTo("DE"))
                    assertThat(call.locale!!.language, equalTo("de"))
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.AcceptLanguage, "de-DE, de-CH;q=0.7, *;q=0.5")
            }
        }
    }

    @Test
    fun `Language de-DE is correctly detected, when preceeded by illegal languages`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertThat(call.locale!!.language, equalTo("de"))
                    assertThat(call.locale!!.country, equalTo("DE"))
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.AcceptLanguage, "xx, aaaaa66, de-DE, de-CH;q=0.7, *;q=0.5")
            }
        }
    }

    @Test
    fun `Language de is correctly detected`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertThat(call.locale!!.country, equalTo(""))
                    assertThat(call.locale!!.language, equalTo("de"))
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.AcceptLanguage, "de, de-DE, de-CH;q=0.7, *;q=0.5")
            }
        }
    }
}
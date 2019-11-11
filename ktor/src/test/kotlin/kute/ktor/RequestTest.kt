// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.ktor

import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RequestTest {
    @Test
    fun `DNT unset`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertNull(call.request.dnt, "Expected DNT to be unset")
                }
            }

            handleRequest(HttpMethod.Get, "/") {}
        }
    }

    @Test
    fun `DNT enabled`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertTrue(call.request.dnt!!, "Expected DNT to be set")
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.DNT, "1")
            }
        }
    }

    // See: https://www.w3.org/TR/tracking-dnt/#dnt-extensions
    @Test
    fun `DNT enabled with extensions`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertTrue(call.request.dnt!!, "Expected DNT to be set")
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.DNT, "1xyz")
            }
        }
    }

    @Test
    fun `DNT disabled`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertFalse(call.request.dnt!!, "Expected DNT to be disabled")
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.DNT, "0")
            }
        }
    }

    @Test
    fun `Bots are properly detected`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertTrue(call.request.isBot, "Expected bot to be detected")
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(HttpHeaders.UserAgent, "Googlebot/2.1 (+http://www.google.com/bot.html)")
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "SAMSUNG-SGH-E250/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0 (compatible; Googlebot-Mobile/2.1; +http://www.google.com/bot.html)"
                )
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (seoanalyzer; compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)"
                )
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html)"
                )
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "LinkedInBot/1.0 (compatible; Mozilla/5.0; Apache-HttpClient +http://www.linkedin.com)"
                )
            }
        }
    }

    @Test
    fun `Normal user agents aren't detected as bots`() {
        withTestApplication {
            application.routing {
                get("/") {
                    assertFalse(call.request.isBot, "Normal useragents shouldn't be detected as bots")
                }
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.92 Safari/537.36 Vivaldi/2.9.1705.38"
                )
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Linux; U; Android 2.2) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"
                )
            }

            handleRequest(HttpMethod.Get, "/") {
                addHeader(
                    HttpHeaders.UserAgent,
                    "Opera/9.80 (Windows NT 6.1; WOW64) Presto/2.12.388 Version/12.18"
                )
            }
        }
    }
}
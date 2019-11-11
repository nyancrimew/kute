// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

dependencies {
    implementation("io.ktor:ktor-server-core:1.2.4")
    implementation(project(":core"))

    testImplementation("io.ktor:ktor-server-tests:1.2.4")
}
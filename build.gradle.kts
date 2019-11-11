import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    maven
    kotlin("jvm") version "1.3.50"
    id("org.jetbrains.dokka") version "0.10.0"
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.dokka")
    group = "de.letescape.kute"
    version = "0.0.1"

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))

        testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
        testImplementation("com.natpryce:hamkrest:1.7.0.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }

        testLogging {
            events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    tasks {
        val sourcesJar by creating(Jar::class) {
            dependsOn(JavaPlugin.CLASSES_TASK_NAME)
            classifier = "sources"
            from(sourceSets["main"].allSource)
        }

        val dokkaJavadoc by creating(DokkaTask::class) {
            outputFormat = "javadoc"
            outputDirectory = "$buildDir/javadoc"
        }

        val javadocJar by creating(Jar::class) {
            dependsOn(dokkaJavadoc)
            classifier = "javadoc"
            from("$buildDir/javadoc")
        }

        artifacts {
            add("archives", sourcesJar)
            add("archives", javadocJar)
        }
    }
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
        subProjects = listOf("core", "ktor")
    }
}

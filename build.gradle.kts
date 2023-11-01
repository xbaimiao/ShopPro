plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.54"
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
}

taboolib {
    description {
        dependencies {
            name("PlaceholderAPI").optional(true)
            name("Vault").optional(true)
            name("MMOItems").optional(true)
            name("Zaphkiel").optional(true)
        }
        contributors {
            name("小白")
        }
    }
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("module-database")
    install("module-lang")
    install("module-ui")
    install("module-nms-util")
    install("module-nms")
    install("module-kether")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.12-34"
    options("skit-kotlin")
    relocate("kotlin", "libs.kotlin171")
    relocate("com.xbaimiao.ktor", "libs.kotlin171.ktor")
}

repositories {
    mavenCentral()
    maven("https://maven.xbaimiao.com/repository/maven-public/")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("public:MythicMobs:4.14.1")
    compileOnly("public:papi:1.0.0")
    compileOnly("public:vault:1.0.0")
    compileOnly("public:points:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("public:Zaphkiel:1.0.0")
    compileOnly("public:MMOItems:6.9.4")
    taboo("com.xbaimiao.ktor:ktor-plugins-bukkit:1.0.8")
    taboo(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}
plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("de.mobanisto.pinpit")
    id("org.jlleitschuh.gradle.ktlint")
    id("de.topobyte.version-access-gradle-plugin")
}

generateVersionAccessSource {
    packageName = "com.example.template"
}

val attributeUsage = Attribute.of("org.gradle.usage", String::class.java)

val currentOs: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
    attributes { attribute(attributeUsage, "java-runtime") }
}

val windowsX64: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
    attributes { attribute(attributeUsage, "java-runtime") }
}

val linuxX64: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
    attributes { attribute(attributeUsage, "java-runtime") }
}

val macosX64: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
    attributes { attribute(attributeUsage, "java-runtime") }
}

val macosArm64: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
    attributes { attribute(attributeUsage, "java-runtime") }
}

sourceSets {
    main {
        java {
            compileClasspath = currentOs
            runtimeClasspath = currentOs
        }
    }
    test {
        java {
            compileClasspath += currentOs
            runtimeClasspath += currentOs
        }
    }
}

dependencies {
    implementation(project(":template-project-compose"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    currentOs(compose.desktop.currentOs)
    windowsX64(compose.desktop.windows_x64)
    linuxX64(compose.desktop.linux_x64)
    macosX64(compose.desktop.macos_x64)
    macosArm64(compose.desktop.macos_arm64)
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("de.topobyte:shared-preferences:0.1.0")
}

val versionCode by extra("0.1.0")
version = versionCode

pinpit.desktop {
    application {
        mainClass = "com.example.template.TemplateProjectKt"

        nativeDistributions {
            jvmVendor = "adoptium"
            jvmVersion = "17.0.8.1+1"

            packageName = "Template Project"
            packageVersion = versionCode
            description = "Template Project - a template project"
            vendor = "Template Vendor"
            copyright = "2023 Template Vendor"
            licenseFile.set(project.file("src/main/packaging/LICENSE.txt"))
            modules("java.naming")
            linux {
                packageName = "template-project"
                debMaintainer = "jon@example.com"
                debPackageVersion = versionCode
                appCategory = "utils"
                menuGroup = "Utility"
                iconFile.set(project.file("src/main/packaging/linux/icon.png"))
                debPreInst.set(project.file("src/main/packaging/linux/deb/preinst"))
                debPostInst.set(project.file("src/main/packaging/linux/deb/postinst"))
                debPreRm.set(project.file("src/main/packaging/linux/deb/prerm"))
                debCopyright.set(project.file("src/main/packaging/linux/deb/copyright"))
                debLauncher.set(project.file("src/main/packaging/linux/deb/launcher.desktop"))
                deb("UbuntuFocalX64") {
                    qualifier = "ubuntu-20.04"
                    arch = "x64"
                    depends(
                        "libc6", "libexpat1", "libgcc-s1", "libpcre3", "libuuid1", "xdg-utils",
                        "zlib1g", "libnotify4"
                    )
                }
                distributableArchive {
                    format = "tar.gz"
                    arch = "x64"
                }
            }
            windows {
                dirChooser = true
                shortcut = true
                menuGroup = "Template Vendor"
                upgradeUuid = "1889DD1C-CD5F-4B43-AC0E-880EC17D5593"
                packageVersion = versionCode
                iconFile.set(project.file("src/main/packaging/windows/icon.ico"))
                aumid = "Vendor.Template.Project"
                msi {
                    arch = "x64"
                    bitmapBanner.set(project.file("src/main/packaging/windows/banner.bmp"))
                    bitmapDialog.set(project.file("src/main/packaging/windows/dialog.bmp"))
                }
                distributableArchive {
                    format = "zip"
                    arch = "x64"
                }
            }
            macOS {
                iconFile.set(project.file("src/main/packaging/macos/icon.icns"))
                bundleID = "com.example.template.project"
                appCategory = "public.app-category.productivity"
                distributableArchive {
                    format = "zip"
                    arch = "x64"
                }
                distributableArchive {
                    format = "zip"
                    arch = "arm64"
                }
            }
        }
    }
}

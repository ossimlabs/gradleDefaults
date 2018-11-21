package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test


class GrailsCommonConfig {

    static void apply(Project project) {
        configureAllSubprojects(project)
    }

    static void configureAllSubprojects(Project project){
        for (Project subproject : project.subprojects){
            boolean isGrailsApp = subproject.name.endsWith('-app')
            boolean isGrailsPlugin = subproject.name.endsWith('-plugin')

            subproject.version = project.version
            subproject.group = project.group

            if (isGrailsApp || isGrailsPlugin){
                configureSubproject(subproject, isGrailsApp, isGrailsPlugin)
            }
        }
    }

    static void configureSubproject(Project subproject,
                                    boolean isGrailsApp,
                                    boolean isGrailsPlugin) {

        Variables.setVersionVariables(subproject)
        Variables.setAdditionalVariables(subproject)

        subproject.repositories {
            mavenLocal()
            mavenCentral()
            maven { url "https://repo.grails.org/grails/core" }
        }

        // Apply plugins
        subproject.apply plugin: "eclipse"
        subproject.apply plugin: "idea"
        if (isGrailsApp) {
            subproject.apply plugin: "org.grails.grails-web"
        }
        if (isGrailsPlugin) {
            subproject.apply plugin: "org.grails.grails-plugin"
            subproject.apply plugin: "org.grails.grails-plugin-publish"
        }
        subproject.apply plugin: "org.grails.grails-gsp"
        subproject.apply plugin: "asset-pipeline"
        subproject.apply plugin: "java-base"

        // Apply dependencies
        subproject.with {
            dependencies {
                compile "org.springframework.boot:spring-boot-starter-logging"
                compile "org.springframework.boot:spring-boot-autoconfigure"
                compile "org.grails:grails-core"
                compile "org.springframework.boot:spring-boot-starter-actuator"
                compile "org.springframework.boot:spring-boot-starter-tomcat"
                compile "org.grails:grails-web-boot"
                compile "org.grails:grails-logging"
                compile "org.grails:grails-plugin-rest"
                compile "org.grails:grails-plugin-databinding"
                compile "org.grails:grails-plugin-i18n"
                compile "org.grails:grails-plugin-services"
                compile "org.grails:grails-plugin-url-mappings"
                compile "org.grails:grails-plugin-interceptors"
                compile "org.grails.plugins:cache"
                compile "org.grails.plugins:async"
                compile "org.grails.plugins:scaffolding"
                compile "org.grails.plugins:gsp"
                console "org.grails:grails-console"
                runtime "com.bertramlabs.plugins:asset-pipeline-grails:${assetPipelineGrailsV}"
                testCompile "org.grails:grails-web-testing-support"
                testCompile "org.grails.plugins:geb:1.1.2"


                if (isGrailsApp) {
                    compile "org.grails.plugins:events"
                    compile "org.grails.plugins:hibernate5"
                    compile "org.hibernate:hibernate-core:5.1.5.Final"
                    profile "org.grails.profiles:web"
                    runtime "org.glassfish.web:el-impl:${elImplV}"
                    runtime "com.h2database:h2"
                    runtime "org.apache.tomcat:tomcat-jdbc"
                    testCompile "org.grails:grails-gorm-testing-support"
                    testRuntime "org.seleniumhq.selenium:selenium-chrome-driver:${seleniumHtmlUnitDriverV}"
                    testRuntime "org.seleniumhq.selenium:selenium-htmlunit-driver:${seleniumHtmlUnitDriverV}"
                    testRuntime "net.sourceforge.htmlunit:htmlunit:${htmlUnitV}"
                } else if (isGrailsPlugin) {

                    profile "org.grails.profiles:web-plugin"
                    provided "org.grails:grails-plugin-services"
                    provided "org.grails:grails-plugin-domain-class"
                    testCompile "org.grails:grails-gorm-testing-support"
                    testCompile "org.grails:grails-plugin-testing"
                }
            }
        }

        if (isGrailsApp) {
            subproject.tasks.withType(Test) {
                systemProperty "geb.env", System.getProperty('geb.env')
                systemProperty "geb.build.reportsDir", subproject.file("geb/integrationTest")
                systemProperty "webdriver.chrome.driver", System.getProperty('webdriver.chrome.driver')
                systemProperty "webdriver.gecko.driver", System.getProperty('webdriver.gecko.driver')
            }
        } else if (isGrailsPlugin) {
            // enable if you wish to package this plugin as a standalone application
//            subproject.bootRepackage.enabled = false
//            subproject.grailsPublish {
//                // TODO: Provide values here
//                user = 'user'
//                key = 'key'
//                githubSlug = 'foo/bar'
//                license {
//                    name = 'Apache-2.0'
//                }
//                title = "My Plugin"
//                desc = "Full plugin description"
//                developers = [johndoe:"John Doe"]
//            }
        }

        AddPublications.addPublication(subproject)

        subproject.assets {
            if (isGrailsApp) {
                minifyJs = true
                minifyCss = true
            } else if (isGrailsPlugin) {
                packagePlugin = true
            }
        }

        subproject.bootRun {
            jvmArgs('-Dspring.output.ansi.enabled=always')
            addResources = true
            String springProfilesActive = 'spring.profiles.active'
            systemProperty springProfilesActive, System.getProperty(springProfilesActive)
        }
    }
}

package io.ossim.gradleDefaults


import asset.pipeline.gradle.AssetPipelinePlugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import org.gradle.tooling.provider.model.internal.DefaultToolingModelBuilderRegistry
import org.grails.gradle.plugin.core.GrailsPluginGradlePlugin
import org.grails.gradle.plugin.publishing.GrailsCentralPublishGradlePlugin
import org.grails.gradle.plugin.web.GrailsWebGradlePlugin
import org.grails.gradle.plugin.web.gsp.GroovyPagePlugin


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

        ToolingModelBuilderRegistry reg = new DefaultToolingModelBuilderRegistry()
        if (isGrailsApp) {
            new GrailsWebGradlePlugin(reg).apply(subproject)
        } else if (isGrailsPlugin){
            new GrailsPluginGradlePlugin(reg).apply(subproject)
            new GrailsCentralPublishGradlePlugin().apply(subproject)
        }
        new AssetPipelinePlugin().apply(subproject)
        new GroovyPagePlugin().apply(subproject)

        subproject.repositories{
            mavenLocal()
            maven { url "https://repo.grails.org/grails/core" }
        }

        subproject.dependencies {
            compile "org.springframework.boot:spring-boot-starter-logging"
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
            runtime "com.bertramlabs.plugins:asset-pipeline-grails:2.14.8"
            testCompile "org.grails:grails-web-testing-support"


            if (isGrailsApp) {
                compile "org.grails.plugins:events"
                compile "org.grails.plugins:hibernate5"
                compile "org.hibernate:hibernate-core:5.1.5.Final"
                profile "org.grails.profiles:web"
                runtime "org.glassfish.web:el-impl:2.1.2-b03"
                runtime "com.h2database:h2"
                runtime "org.apache.tomcat:tomcat-jdbc"
                testCompile "org.grails:grails-gorm-testing-support"
                testCompile "org.grails.plugins:geb:1.1.2"
                testRuntime "org.seleniumhq.selenium:selenium-chrome-driver:2.47.1"
                testRuntime "org.seleniumhq.selenium:selenium-htmlunit-driver:2.47.1"
                testRuntime "net.sourceforge.htmlunit:htmlunit:2.18"
            } else if (isGrailsPlugin) {

                profile "org.grails.profiles:web-plugin"
                provided "org.grails:grails-plugin-services"
                provided "org.grails:grails-plugin-domain-class"
                testCompile "org.grails:grails-gorm-testing-support"
                testCompile "org.grails:grails-plugin-testing"
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
            subproject.bootRepackage.enabled = false
            subproject.grailsPublish {
                // TODO: Provide values here
                user = 'user'
                key = 'key'
                githubSlug = 'foo/bar'
                license {
                    name = 'Apache-2.0'
                }
                title = "My Plugin"
                desc = "Full plugin description"
                developers = [johndoe:"John Doe"]
            }
        }

        subproject.assets {
            if (isGrailsApp) {
                subproject.getProperties().put('minifyJs', true)
                subproject.getProperties().put('minifyCss', true)
            } else if (isGrailsPlugin) {
                subproject.getProperties().put('packagePlugin', true)
            }
        }
    }
}

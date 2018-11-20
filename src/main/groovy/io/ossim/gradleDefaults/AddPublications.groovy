package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class AddPublications {
    static void addPublication(Project project) {
        project.apply plugin: "maven-publish"
        project.with {
            publishing {
                publications {
                    "${name}" (MavenPublication) {
                        println(project.name)
                        println(project.groupName)
                        println(project.version)
                        println("${buildDir}/libs/${project.name}-${project.version}.jar")
                        println("=====")
                        artifactId = project.name
                        groupId = project.groupName
                        if (project.buildVersionTag == "SNAPSHOT") {
                            version = "${project.version}-SNAPSHOT"
                            println("${project.version}-SNAPSHOT")
                        } else{
                            version = "${project.version}"
                            println("${project.version}")
                        }
                        artifact(file("${buildDir}/libs/${project.name}-${project.version}.jar"))
                    }
                }
                repositories {
                    maven {
                        credentials {
                            username System.getenv('MAVEN_REPOSITORY_USERNAME')
                            password System.getenv('MAVEN_REPOSITORY_PASSWORD')
                        }
                        url = "${System.getenv('MAVEN_REPOSITORY_URL')}/o2-${project.buildVersionTag}"
                    }
                }
            }
        }
    }
}

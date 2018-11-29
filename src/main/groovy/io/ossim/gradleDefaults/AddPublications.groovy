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
                        artifactId = project.name

                        // Use the default io.ossim.omar group name unless specified otherwise
                        String groupName
                        if (project.hasProperty('groupName')) {
                            groupName = project.groupName
                        } else {
                            groupName = "io.ossim.omar"
                        }

                        group groupName
                        groupId = groupName
                        if (project.buildVersionTag == "SNAPSHOT") {
                            version = "${project.version}-SNAPSHOT"
                        } else{
                            version = "${project.version}"
                        }
//                        println("Producing: ${groupId}:${artifactId}:${version}")
                        artifact(file("${buildDir}/libs/${project.name}-${project.version}.jar"))
                    }
                }
                repositories {
                    maven {
                        credentials {
                            username System.getenv('MAVEN_REPOSITORY_USERNAME')
                            password System.getenv('MAVEN_REPOSITORY_PASSWORD')
                        }
                        url = "${System.getenv('MAVEN_REPOSITORY_URL')}/o2-${project.buildVersionTag}/"
                    }
                }
            }
        }
    }
}

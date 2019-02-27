package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class Publications {
    static void addPublication(Project project, String groupName, String artifactName, String versionNumber) {
        project.with {
            publishing {
                publications {
                    "${artifactName}" (MavenPublication) {
                        group groupName
                        groupId = groupName
                        version versionNumber
                        from components.java
                    }
                }
            }

            jar.onlyIf {
                !file(jarDestination).exists()
            }
        }
    }

    static void addRepository(Project project, String repositoryUrl, String repositoryUsername, String repositoryPassword) {
        project.apply plugin: "maven-publish"
        project.with {
            publishing {
                repositories {
                    maven {
                        credentials {
                            username repositoryUsername
                            password repositoryPassword
                        }
                        url = repositoryUrl.toLowerCase()
                    }
                }
            }
        }
    }
}

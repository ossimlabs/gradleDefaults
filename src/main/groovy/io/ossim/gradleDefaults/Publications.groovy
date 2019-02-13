package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class Publications {
    static void addPublication(Project project, String groupName, String artifactName, String versionNumber) {
        project.apply plugin: "maven-publish"
        project.with {
            publishing {
                publications {
                    "${artifactName}" (MavenPublication) {
                        group groupName
                        groupId = groupName
                        version versionNumber
                        artifact(file("${buildDir}/libs/${artifactName}-${versionNumber}.jar"))
                    }
                }
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

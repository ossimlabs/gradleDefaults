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
                        artifactId = project.name
                        groupId = project.groupName
                        version = project.version
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

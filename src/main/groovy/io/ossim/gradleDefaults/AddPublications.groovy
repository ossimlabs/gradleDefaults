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
                        from components.java
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

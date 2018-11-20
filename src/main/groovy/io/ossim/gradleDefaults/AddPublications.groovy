package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class AddPublications {
    static void addPublication(Project project) {
        project.apply plugin: "maven-publish"
        def publishingClosure = {
            publications {
                "${name}" ( MavenPublication ) {
                    artifact( file( "${ project.buildDir }/libs/${ project.name }-${ project.version }.jar" ) )
                }
            }
            repositories {
                maven {
                    credentials {
                        username System.getenv( 'MAVEN_REPOSITORY_USERNAME' )
                        password System.getenv( 'MAVEN_REPOSITORY_PASSWORD' )
                    }
                    url = "${ System.getenv( 'MAVEN_REPOSITORY_URL' ) }/o2-${project.buildReleaseTag}"
                }
            }
        }
        publishingClosure.setDelegate(project)
        project.publishing(publishingClosure)
    }
}

package io.ossim.gradleDefaults

import org.gradle.api.Project

class AddPublications {
    static void addPublication(Project project) {
        project.publishing {
            publications {
                bootJava( MavenPublication ) {
                    artifact( file( "${ buildDir }/libs/${ project.name }-${ project.version }.jar" ) )
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
    }
}

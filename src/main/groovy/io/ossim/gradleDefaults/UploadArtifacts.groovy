package io.ossim.gradleDefaults

import org.gradle.api.Project

class UploadArtifacts {
    static void addUploadArtifact(Project project) {
        uploadArchives {
            repositories {
                mavenDeployer {
                    repository(url: "${System.env.MAVEN_REPOSITORY_URL}/omar-local-${buildVersionTag.toLowerCase()}") {
                        authentication(userName: "${System.env.MAVEN_REPOSITORY_USER}", password: "${System.env.MAVEN_REPOSITORY_PASSWORD}")
                    }
                }
            }
        }
    }
}

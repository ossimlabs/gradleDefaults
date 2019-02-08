package io.ossim.gradleDefaults

import org.gradle.api.Project

class OpenShift {
    static void addDockerTasks(Project project,
                               String openshiftUrl,
                               String openshiftUsername,
                               String openshiftPassword,
                               String openshiftProjectName,
                               String openshiftServiceName,
                               String dockerRegistryUrl,
                               String dockerImageName,
                               String dockerImageTag) {

        project.task('openshiftLogin') {
            doLast {
                exec {
                    commandLine 'oc',
                            'login',
                            openshiftUrl,
                            '-u', openshiftUsername,
                            '-p', openshiftPassword
                }
            }
        }

        if (! dockerRegistryUrl.endsWith('/')){
            dockerRegistryUrl += '/'
        }

        project.task('openshiftTagImage', dependsOn: 'openshiftLogin'){
            doLast {
                exec {
                    commandLine 'oc',
                            'tag',
                            '--source=docker',
                            "${dockerRegistryUrl}${dockerImageName}",
                            "${openshiftProjectName}/${openshiftServiceName}:${dockerImageTag}",
                            '--scheduled=true'
                }
            }
        }
    }
}

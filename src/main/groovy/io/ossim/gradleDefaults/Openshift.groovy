package io.ossim.gradleDefaults

import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class Openshift {
    static void addOpenshiftTasks(Project project,
                               String openshiftUrl,
                               String openshiftUsername,
                               String openshiftPassword,
                               String openshiftProjectName,
                               String openshiftServiceName,
                               String dockerRegistryUrl,
                               String dockerImageName,
                               String dockerImageTag) {

        project.task('openshiftLogin', type: Exec) {
            commandLine 'oc',
                    'login',
                    openshiftUrl,
                    '-u', openshiftUsername,
                    '-p', openshiftPassword
        }

        if (! dockerRegistryUrl?.endsWith('/')){
            dockerRegistryUrl += '/'
        }

        project.task('openshiftTagImage', type: Exec, dependsOn: 'openshiftLogin'){
            commandLine 'oc',
                    'tag',
                    '--source=docker',
                    "${dockerRegistryUrl}${dockerImageName}:${dockerImageTag}",
                    "${openshiftProjectName}/${openshiftServiceName}:${dockerImageTag}",
                    '--scheduled=true'
        }
    }
}

package io.ossim.gradleDefaults

import org.gradle.api.Plugin
import org.gradle.api.Project

class Base implements Plugin<Project> {


    @Override
    void apply(Project project) {
//        if (project.findProperty('grailsCommonConfig')) {
//            new GrailsCommonConfig().apply(project)
//        }

        Variables.setVersionVariables(project)
        Variables.setAdditionalVariables(project)
        UploadArtifacts.addUploadArtifact(project)

        if (project.findProperty('includeDocker')) {
            Docker docker = new Docker()
            docker.apply(project)
        }

    }

}

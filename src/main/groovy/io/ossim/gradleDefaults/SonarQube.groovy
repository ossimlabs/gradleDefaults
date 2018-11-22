package io.ossim.gradleDefaults

import org.gradle.api.Project

class SonarQube {
    static void addSonarQube(Project project){
        project.apply plugin: "org.sonarqube"
    }
}

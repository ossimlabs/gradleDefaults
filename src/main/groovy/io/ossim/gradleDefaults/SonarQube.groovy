package io.ossim.gradleDefaults

import org.gradle.api.Project

class SonarQube {
    static void addSonarQubeDependency(Project project){
        project.dependencies {
            classpath "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.5"
        }
    }
}

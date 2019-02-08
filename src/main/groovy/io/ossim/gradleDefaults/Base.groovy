package io.ossim.gradleDefaults

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.diagnostics.DependencyReportTask

class Base implements Plugin<Project> {


    @Override
    void apply(Project project) {

    }

    static void addMiscHelperTasks(Project project) {
        project.task('printAllDependencies', type: DependencyReportTask) {}
    }
}

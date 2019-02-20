package io.ossim.gradleDefaults

import com.bmuschko.gradle.docker.DockerRegistryCredentials
import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerTagImage
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class Docker {
    static void addDockerTasks(Project project,
                               String dockerRegistryUrl,
                               String dockerRegistryUsername,
                               String dockerRegistryPassword,
                               String dockerImageName,
                               String dockerBuildTag,
                               String dockerBuildDir,
                               String jarPath,
                               Map buildArguments) {

        new DockerRemoteApiPlugin().apply(project)

        DockerRegistryCredentials dockerRegistryCredentials = new DockerRegistryCredentials()
        dockerRegistryCredentials.username = dockerRegistryUsername
        dockerRegistryCredentials.password = dockerRegistryPassword
        dockerRegistryCredentials.url = dockerRegistryUrl



        // Add each subproject assemble task as a dependency for the copyTask
        for (Project subProject : project.getAllprojects()){
            if (subProject.tasks.findByName('assemble')) {
                def assembleTask = subProject.tasks.findByName('assemble')
                project.copyJarToDockerDir.dependsOn assembleTask
            }
        }

        project.task('buildDockerImage', type: DockerBuildImage) {
            inputDir = project.file(dockerBuildDir)
            tag = "${dockerImageName}:${dockerBuildTag}"
            buildArgs = buildArguments
            registryCredentials = dockerRegistryCredentials
        }

        if (jarPath != null && jarPath != '') {
            // Copy the built jar to the docker directory
            project.task('copyJarToDockerDir', type: Copy) {
                doFirst {
                    println "Copying ${jarPath} to ${dockerBuildDir}"
                }
                from jarPath
                into dockerBuildDir
            }

            def copyJarToDockerDirTask = project.tasks.findByName('copyJarToDockerDir')
            project.buildDockerImage.dependsOn copyJarToDockerDirTask
        }

        project.task('tagDockerImage', type: DockerTagImage, dependsOn: 'buildDockerImage'){
            imageId "${dockerImageName}:${dockerBuildTag}"
            tag dockerBuildTag
            repository "${dockerRegistryUrl}/${dockerImageName}"
        }

        project.task('pushDockerImage', type: DockerPushImage, dependsOn: 'tagDockerImage') {
            imageName "${dockerRegistryUrl}/${dockerImageName}"
            tag dockerBuildTag
            registryCredentials = dockerRegistryCredentials
        }

//        // Add an action to remove the jar file from the docker dir to the clean task
//        project.clean.doFirst{
//            println("Removing ${dockerBuildDir}/${jarFile}")
//            project.file("${dockerBuildDir}/${jarFile}").delete()
//        }

    }
}
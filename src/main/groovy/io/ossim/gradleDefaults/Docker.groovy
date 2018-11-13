package io.ossim.gradleDefaults

import com.bmuschko.gradle.docker.DockerRegistryCredentials
import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import com.bmuschko.gradle.docker.tasks.image.DockerTagImage
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

import com.bmuschko.gradle.docker.tasks.image.DockerPullImage

class Docker {

    void apply(Project project) {

        new DockerRemoteApiPlugin().apply(project)

        // Variables pulled from Environment variables
        String dockerRegistryUrl = System.getenv("DOCKER_REGISTRY_URL")

        DockerRegistryCredentials dockerRegistryCredentials = new DockerRegistryCredentials()
        dockerRegistryCredentials.username = System.getenv("DOCKER_USERNAME")
        dockerRegistryCredentials.password = System.getenv("DOCKER_PASSWORD")
        dockerRegistryCredentials.url = dockerRegistryUrl

        // Variables pulled from gradle.properties or other locations
        String versionNumber = project.findProperty('versionNumber')
        String dockerBaseImage = project.findProperty('dockerBaseImage')

        String jarDir
        String jarFile
        String dockerDir

        Boolean buildDocker

        String dockerAppTag
        String copyFile

        // Variables constructed from other variables
        if (project.hasProperty('jarDir')) {
            jarDir = project.jarDir
        } else {
            jarDir = "${project.rootDir}/apps/${project.name}-app/build/libs/"
        }

        if (project.hasProperty('jarFile')) {
            jarFile = project.jarFile
        } else {
            jarFile = "${project.name}-app-${versionNumber}.jar"
        }

        if (project.hasProperty('jarFile')) {
            dockerDir = project.dockerDir
        } else {
            dockerDir = "${project.rootDir}/docker"
        }

        if (project.hasProperty('buildDocker')) {
            buildDocker = project.buildDocker
        } else {
            buildDocker = true
        }

        if (project.hasProperty('dockerTag')) {
            dockerAppTag = project.dockerTag
        } else {
            dockerAppTag = System.getenv("DOCKER_TAG") ?: "latest"
        }

        if (project.hasProperty('copyFile')) {
            copyFile = project.copyFile
        } else {
            copyFile = jarFile
        }

        String image = "${project.name}:${dockerAppTag}"

        // Copy the built jar to the docker directory
        project.task('copyJarToDockerDir', type: Copy) {
            doFirst {
                println "Copying ${jarDir}/${jarFile} to ${dockerDir}"
            }
            from "${jarDir}/${jarFile}"
            into "${dockerDir}"
            onlyIf { return buildDocker }
        }

        // Add each subproject assemble task as a dependency for the copyTask
        for (Project subProject : project.getAllprojects()){
            project.copyJarToDockerDir.dependsOn.add(":${subProject.name}:assemble")
        }

        // Pull the base docker image from the remote repo
        project.task('pullBaseDockerImage', type: DockerPullImage) {
            repository "${dockerRegistryUrl}/${dockerBaseImage}"
            tag "latest"
            onlyIf { return buildDocker }
        }

        //
        project.task('buildDockerImage', type: DockerBuildImage, dependsOn: ['copyJarToDockerDir', 'pullBaseDockerImage']) {
            inputDir = project.file(dockerDir)
            tag = "${image}"
            buildArgs = ["BASE_IMAGE": "${dockerRegistryUrl}/${dockerBaseImage}",
                         "COPY_FILE": "${copyFile}"]
            registryCredentials = dockerRegistryCredentials
            onlyIf { return buildDocker }
        }

        project.task('tagDockerImage', type: DockerTagImage, dependsOn: 'buildDockerImage'){
            imageId "${image}"
            tag "${dockerAppTag}"
            repository "${dockerRegistryUrl}/${project.name}"
            onlyIf { return buildDocker }
        }

        project.task('pushDockerImage', type: DockerPushImage, dependsOn: 'tagDockerImage') {
            imageName "${dockerRegistryUrl}/${project.name}"
            tag "${dockerAppTag}"
            registryCredentials = dockerRegistryCredentials
            onlyIf { return buildDocker }
        }

        // Add an action to remove the jar file from the docker dir to the clean task
        project.clean.doFirst{
            println("Removing ${dockerDir}/${jarFile}")
            project.file("${dockerDir}/${jarFile}").delete()
        }

        project.task('removeLocalDockerImage', type:DockerRemoveImage){
            imageId "${image}"
        }

        project.task('removeRemoteDockerImage', type:DockerRemoveImage){
            imageId "${dockerRegistryUrl}/${project.name}"
        }

        project.task('cleanDockerImages', dependsOn: ['removeLocalDockerImage', 'removeRemoteDockerImage', 'clean'])

        project.task('doAllDocker', dependsOn: ['pushDockerImage', 'cleanDockerImages']){
            project.tasks.getByName('cleanDockerImages')
                    .mustRunAfter(project.tasks.getByName('pushDockerImage'))
            project.tasks.getByName('clean')
                    .mustRunAfter(project.tasks.getByName('pushDockerImage'))
        }

        // Add each subproject clean task as a dependency for the doAllDocker task
        for (Project subProject : project.getSubprojects()){
            project.doAllDocker.dependsOn.add(subProject.tasks.getByName("clean"))
            subProject.tasks.getByName("clean").mustRunAfter(project.tasks.getByName('clean'))
        }

    }
}
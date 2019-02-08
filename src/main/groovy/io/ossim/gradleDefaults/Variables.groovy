package io.ossim.gradleDefaults;

import org.gradle.api.Project;

class Variables {

    static void setAdditionalVariables(Project project){
        project.ext {
            nexusContextUrl = "${System.env.REPOSITORY_MANAGER_URL}"
            ossimMavenProxy = System.env.OSSIM_MAVEN_PROXY ?: "${mavenRepoUrl}/ossim-deps"
            omarMavenProxy = System.env.OMAR_MAVEN_PROXY ?: "${mavenRepoUrl}/omar-deps"

            gitBranch = Git.getCurrentGitBranch(project)
            buildVersionTag = gitBranch == "master" ? "RELEASE" : "SNAPSHOT"
            latestGradleIdentifier = gitBranch == "master" ? "latest.release" : "latest.integration"

            mavenPublishUrl = System.env.MAVEN_PUBLISH_URL ?: "${mavenRepoUrl}/omar-local-${buildVersionTag.toLowerCase()}"

            openShiftUrl = "${System.env.OPENSHIFT_URL}"
            yumTag = (buildVersionTag == "SNAPSHOT" ? "dev" : "master")
            dockerAppTag = "${System.env.DOCKER_TAG}"
            dockerRegistryUrl = "${System.env.DOCKER_REGISTRY_URL}"
            registryProjectName = "${System.env.REGISTRY_PROJECT_NAME}"
            openShiftUserName = "${System.env.OPENSHIFT_USERNAME}"
            openShiftPassword = "${System.env.OPENSHIFT_PASSWORD}"
            dockerNamespaceUrl = "${dockerRegistryUrl}/"
        }
    }

    static void setVersionVariables(Project project) {
        project.ext {
            springBootV = "1.5.10.RELEASE"
            springCloudDependencyV = "Dalston.SR5"
            benManesV = "0.17.0"
            assetPipelineGradleV = "2.14.10"
            assetPipelineGrailsV = "2.14.10"
            springBootAdminStarterClientV = "1.5.7"
            bootstrapSelectV = "2.0.0-beta1"
            slickCarouselV = "1.8.1"
            angularSlickCarouselV = "3.1.7"
            selectizeV = "0.12.4"
            seiyriaBootstrapSliderV = "9.7.2"
            babelAssetPipelineV = "2.1.1"
            hibernateCoreV = "5.1.9.Final"
            elImplV = "2.2.1-b05"
            seleniumHtmlUnitDriverV = "2.52.0"
            htmlUnitV = "2.29"
            javaSdkDynamodbV = "1.11.279"
            springDataDynamodbV = "4.5.0"
            gradleDockerPluginV = "3.2.4"
            jodaTimeV = "2.9.9"
            webjarsSwaggerUIV = "3.10.0"
            postgressqlV = "42.2.1"
            hibernateSpatialV = "5.3.0.CR1"
            hibernate5V = "6.1.8"
            httpBuilderNgCoreV = "1.0.3"
            quartzV = "2.0.13"
            httpBuilderV = "0.7.2"
            awsJavaSDKV = "1.11.281"
            commonsIoV = "2.6"
            springSecurityCoreV = "3.3.0.M1"
            npmCesiumV = "1.38.0"
            hibernateEhcacheV = "5.3.0.CR1"
        }
    }
}

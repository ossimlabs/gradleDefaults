package io.ossim.gradleDefaults;

import org.gradle.api.Project;

class Variables {

    static void setAdditionalVariables(Project project){

        setVariable(project, 'gitBranch', 'BRANCH_NAME', Git.getCurrentGitBranch(project))

        project.ext {
            buildVersionTag = gitBranch == "master" ? "RELEASE" : "SNAPSHOT"
            latestGradleIdentifier = gitBranch == "master" ? "latest.release" : "latest.integration"
            dockerFromTag = gitBranch == "master" ? "master" : "dev"
            yumTag = (buildVersionTag == "SNAPSHOT" ? "dev" : "master")
        }

        setVariable(project, 'mavenRepoUrl', 'MAVEN_REPO_URL', null)
        setVariable(project, 'mavenRepoUsername', 'MAVEN_REPO_USERNAME', null)
        setVariable(project, 'mavenRepoPassword', 'MAVEN_REPO_PASSWORD', null)
        setVariable(project, 'ossimMavenProxy', 'OSSIM_MAVEN_PROXY', "${project.mavenRepoUrl}/ossim-deps")
        setVariable(project, 'omarMavenProxy', 'OMAR_MAVEN_PROXY', "${project.mavenRepoUrl}/omar-deps")
        setVariable(project, 'mavenPublishUrl', 'MAVEN_PUBLISH_URL', "${project.mavenRepoUrl}/ossimlabs")

        setVariable(project, 'dockerImageTag', 'DOCKER_TAG', "${project.gitBranch}")
        setVariable(project, 'dockerRegistryUrl', 'DOCKER_REGISTRY_URL', null)
        setVariable(project, 'dockerRegistryUsername', 'DOCKER_REGISTRY_USERNAME', null)
        setVariable(project, 'dockerRegistryPassword', 'DOCKER_REGISTRY_PASSWORD', null)

        setVariable(project, 'openshiftUrl', 'OPENSHIFT_URL', null)
        setVariable(project, 'openshiftUsername', 'OPENSHIFT_USERNAME', null)
        setVariable(project, 'openshiftPassword', 'OPENSHIFT_PASSWORD', null)
        setVariable(project, 'openshiftProjectName', 'OPENSHIFT_PROJECT_NAME', null)
    }

    /**
     * If the property with name propName does not exist or is null, it should be overwritten
     * by the environment variable or the default
     * @param project
     * @param propName
     * @param envName
     * @param defaultValue
     */
    static void setVariable(Project project, String propName, String envName, String defaultValue) {
        String finalValue

        String envValue = System.getenv(envName)
        if (envValue) {
            finalValue = envValue
        } else {
            if (project.hasProperty(propName) &&
                    project.property(propName) != null &&
                    project.property(propName).toString() != '') {
                return
            } else {
                finalValue = defaultValue
            }
        }

        project.ext {
            setProperty(propName, finalValue)
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

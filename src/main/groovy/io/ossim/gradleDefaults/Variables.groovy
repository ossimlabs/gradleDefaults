package io.ossim.gradleDefaults;

import org.gradle.api.Project;

class Variables {

    String getCurrentGitBranch() {
        def gitBranch = "Unknown branch"
        try {
            println "${project.projectDir}"
            def workingDir = new File("${project.projectDir}")
            def result = 'git rev-parse --abbrev-ref HEAD'.execute(null, workingDir)
            result.waitFor()
            if (result.exitValue() == 0) {
                gitBranch = result.text.trim()
            }
        } catch (e) {
            e.printStackTrace()
        }
        return gitBranch
    }

    static void setAdditionalVariables(Project project){
        project.ext {
            mavenRepoUrl = System.getenv('MAVEN_REPOSITORY_URL')
            gradleOffline = System.getenv('GRADLE_OFFLINE')
            buildVersionTag = getCurrentGitBranch() == "master" ? "RELEASE" : "SNAPSHOT"
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

properties([
    parameters ([
        string(name: 'BUILD_NODE', defaultValue: 'omar-build', description: 'The build node to run on'),
        booleanParam(name: 'CLEAN_WORKSPACE', defaultValue: true, description: 'Clean the workspace at the end of the run')
    ]),
    pipelineTriggers([
            [$class: "GitHubPushTrigger"]
    ]),
    [$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/ossimlabs/gradleDefaults'],
    disableConcurrentBuilds(),
    buildDiscarder( logRotator( numToKeepStr: '5' ) )
])

node("${BUILD_NODE}"){

    stage("Checkout branch $BRANCH_NAME")
    {
        checkout(scm)
    }

    stage("Load Variables")
    {
        withCredentials([string(credentialsId: 'o2-artifact-project', variable: 'o2ArtifactProject')]) {
            step ([$class: "CopyArtifact",
                projectName: o2ArtifactProject,
                filter: "common-variables.groovy",
                flatten: true])
        }

        load "common-variables.groovy"
    }

    stage ("Assemble") {
        sh """
        gradle assemble \
            -PossimMavenProxy=${OSSIM_MAVEN_PROXY}
        """
    }

    stage ("Publish Nexus")
    {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                        credentialsId: 'nexusCredentials',
                        usernameVariable: 'MAVEN_REPO_USERNAME',
                        passwordVariable: 'MAVEN_REPO_PASSWORD']])
        {
            sh """
            gradle publish \
                -PossimMavenProxy=${OSSIM_MAVEN_PROXY}
            """
        }
    }

   stage("Clean Workspace")
   {
      if ("${CLEAN_WORKSPACE}" == "true")
        step([$class: 'WsCleanup'])
   }
}

#!/usr/bin/env groovy

def call() {

  def aProjectCICDDef = projectCICDDef.getProjectInfo()


  def pipelineType1StagesDef = [
    10: ['Type 1 - Stage 1', { stageOne() }],
    20: ['Type 1 - Stage 2', { stageTwoTypeOne() }]
  ]

  def pipelineType2StagesDef = [
    10: ['Type 2 - Stage 1', { stageOne() }],
    20: ['Type 2 - Stage 2', { stageTwoTypeTwo() }],
    30: ['Type 2 - Stage 3', { stageThree() }]
  ]

  def pipelineStagesDefCollection = [
    projectType1: pipelineType1StagesDef,
    projectType2: pipelineType2StagesDef
  ]

  def mainPipelineStagesDefByType = pipelineStagesDefCollection.get(aProjectCICDDef.get('projectType'))

  def basePipelineStagesDef = [:] //This is empty but could have a base set of stages.
  def addedPipelineStagesDef = basePipelineStagesDef


  addedPipelineStagesDef.putAll(mainPipelineStagesDefByType)
  addedPipelineStagesDef.putAll(aProjectCICDDef.get('projectPipelineStagesDef'))
  addedPipelineStagesDef.sort()

  pipeline {
    agent any

    options {
      timeout(time: 60, unit: 'MINUTES')
      timestamps()
      buildDiscarder(logRotator(daysToKeepStr: '10', numToKeepStr: '10', artifactNumToKeepStr: '10'))
      parallelsAlwaysFailFast()
    }

    environment {
      AN_ENVIRONMENT_VARIABLE = 'ExampleEnvVar'
    }

    stages {
      stage ("Pipeline Assembly"){
        steps {
          script {
            addedPipelineStagesDef.each { entry ->
              if (entry.value.size() == 2) {
                stage("${entry.value[0]}") {
                  echoStageInitEnd()
                  entry.value[1]()
                  echoStageInitEnd()
                }
              } else {
                error message: "The pipeline configuration is wrong about number of elements: Stage Name and Stage Code."
              }
            }
          }
        }
      }
    }
    post {
      always {
        postAlwaysStep()
      }
      unsuccessful {
        postUnsuccesfulStep()
        // script {
        //   echo "PIPELINE_FINISHED:  UNSUCCESSFUL"
        //   if(esSMCanAbandonChange()) {
        //     env.ABANDONED = inputUser('Do you want change status to abandoned in SM?', env.QUESTION_TIMEOUT, InputType.CHOICE, ['yes', 'no'])

        //     if ('yes'.equals(env.ABANDONED)) {
        //       esSMAbandonChange(env.PROJECT)
        //       echo "ABANDON_SM: ABANDON_UNSUCCESS"
        //     }
        //   }
        // }
      }
      failure {
        postFailureStep()
        // echo "PIPELINE_FINISHED:  FAILURE"
        // echo '--failure--'
        // script {
        //   GIT_COMMIT_EMAIL = sh(
        //           script: 'git --no-pager show -s --format=\'%ae\'',
        //           returnStdout: true
        //   ).trim()
        //   echo "DESTINATARIO ${GIT_COMMIT_EMAIL}"
        //   if (isDevOpsTestingRepo()) {
        //   printlnVerbose('################################################################################################################################################################################################ Esta ejecución corresponde a una aplicación de prueba y, por lo tanto, se simula el envío de email de error.', verbose:'ON')
        //   } else {
        //     sendFailureEmail GIT_COMMIT_EMAIL
        //   }
        // }
      }
      cleanup {
        postCleanupStep()
      }
    }
  }
}

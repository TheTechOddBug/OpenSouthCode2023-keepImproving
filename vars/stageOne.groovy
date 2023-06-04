#!/usr/bin/env groovy

def call() {
  git 'https://github.com/TheTechOddBug/OpenSouthCode2023-Type1ProjectA.git'
  echo "This is the ${env.STAGE_NAME} stage."
}

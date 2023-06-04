#!/usr/bin/env groovy

def call() {
  git env.REPO_TO_USE
  echo "This is the ${env.STAGE_NAME} stage."
}

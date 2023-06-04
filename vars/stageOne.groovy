#!/usr/bin/env groovy

def call() {
  echo "This is the ${env.STAGE_NAME} stage."
}

#!/usr/bin/env groovy

def call() {
  CHECK_FLAG = env.FLAG ?: 'empty'
  if (CHECK_FLAG == 'empty') {
    println("->->->->->->->->->->->->->->->->->->->->->->->->->-> Init stage ${env.STAGE_NAME}")
    env.FLAG = env.STAGE_NAME
  } else {
    println("->->->->->->->->->->->->->->->->->->->->->->->->->-> End stage ${env.STAGE_NAME}")
    env.FLAG = ''
  }
}

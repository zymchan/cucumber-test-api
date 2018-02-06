pipeline {
  agent any
  stages {
    stage('sleep') {
      parallel {
        stage('sleep') {
          steps {
            sleep 1
          }
        }
        stage('sleep2') {
          steps {
            sleep 2
          }
        }
      }
    }
    stage('last one sleep') {
      steps {
        sleep 3
      }
    }
  }
  environment {
    env = 'qa1'
  }
}
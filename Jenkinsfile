pipeline {
  agent any
  stages {
    stage('td1') {
      steps {
        build 'TestDemo1'
      }
    }
    stage('td2') {
      steps {
        build 'TestDemo2'
      }
    }
  }
  environment {
    env = 'qa1'
  }
}
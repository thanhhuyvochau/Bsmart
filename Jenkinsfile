pipeline {
  agent {
    docker {
      image 'maven:3.8.3-jdk-11'
      args '-v $HOME/.m2:/root/.m2'
    }
  }
  environment {
    IMAGE_NAME = "bsmart-app:${BUILD_NUMBER}"
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean package'
        sh 'docker build -t $IMAGE_NAME .'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }
    stage('Deploy') {
      steps {
        sh "docker run -p 8080:8080 $IMAGE_NAME"
      }
    }
  }
}

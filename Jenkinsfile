pipeline {
    agent any

    environment {
        IMAGE_NAME = "company001-backend"
        CONTAINER_NAME = "company001-backend"
        PORT = "3000"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh "docker build -t ${IMAGE_NAME} ."
                    } else {
                        bat "docker build -t ${IMAGE_NAME} ."
                    }
                }
            }
        }

        stage('Stop old container') {
            steps {
                script {
                    if (isUnix()) {
                        sh "docker rm -f ${CONTAINER_NAME} || true"
                    } else {
                        bat "docker rm -f ${CONTAINER_NAME} || exit 0"
                    }
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    if (isUnix()) {
                        sh "docker run -d --name ${CONTAINER_NAME} -p 8081:8080 ${IMAGE_NAME}"
                    } else {
                        bat "docker run -d --name ${CONTAINER_NAME} -p 8081:8080 ${IMAGE_NAME}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✔ Backend rodando com sucesso!"
        }
        failure {
            echo "❌ Falha no build/deploy"
        }
    }
}

pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "dradmin/company001-backend:latest"
        CONTAINER_NAME = "company001-backend-container"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/MarcianoPasa/company001.git'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Iniciando o Build da Imagem Docker...'
                bat "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Stop Old Container') {
            steps {
                script {
                    // Para e remove o container antigo se ele existir para evitar conflito de porta
                    bat "docker stop ${CONTAINER_NAME} || ver > nul"
                    bat "docker rm ${CONTAINER_NAME} || ver > nul"
                }
            }
        }

        stage('Deploy (Docker Run)') {
            steps {
                echo 'Subindo o novo container...'
                // Rodando em modo detached (-d) e mapeando a porta 8081
                bat "docker run -d --name ${CONTAINER_NAME} -p 8081:8081 ${DOCKER_IMAGE}"
            }
        }
    }

    post {
        success {
            echo 'Deployment concluído com sucesso!'
        }
        failure {
            echo 'Falha no processo de CI/CD. Verifique os logs.'
        }
    }
}

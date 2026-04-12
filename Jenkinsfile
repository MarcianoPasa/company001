pipeline {
    agent any

    stages {

        stage('Build Docker') {
            steps {
                sh 'docker build -t backend-app .'
            }
        }

        stage('Run Container') {
            steps {
                sh 'docker run -d -p 3000:3000 backend-app'
            }
        }

    }
}

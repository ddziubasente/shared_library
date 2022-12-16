@Library('my-shared-library@master') _
// Declarative //
pipeline {
    agent {
        node 'slave01-ubuntu'
    }
    options {
        timestamps()
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    gitCheckout(
                        branch: "master",
                        url: "https://github.com/jenkins-docs/simple-java-maven-app"
                    )
                }
            }
        }
        stage('Check branch') { 
            steps {
                script {
                    gitCheckout.check_branch()
                }
            }
        }
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') {
            steps {
                sh './jenkins/scripts/deliver.sh'
            }
        }
    }
}

def call (Map params ) {
    pipeline {
        agent {
            node params.node
        }
        options {
            timestamps()
        }
        stages {
            stage('Checkout') {
                steps {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name:  "${params.branch}" ]],
                        userRemoteConfigs: [[ url: "${params.gitUrl}" ]]
                    ])
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
                    sh "${params.deliverScriptPath}"
                }
            }
        }
    }
}
def call() {
    stage('Validate Parameters') {
        script {
            echo "Deploying to master: ${env.TARGET_HOST}"
        }
    }
}
return this

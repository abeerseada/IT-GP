def run() {
    stage('Validate Parameters') {
        script {
            if (!params.TARGET_HOST?.trim()) {
                error "TARGET_HOST parameter is required."
            }
        }
    }
}
return this

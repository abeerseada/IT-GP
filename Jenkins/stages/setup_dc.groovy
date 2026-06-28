def call() {
    stage('Create OVS Bridges') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${env.TARGET_HOST} '
                    set -e
                    cd ${env.REPO_DIR}
                    echo "Creating OVS bridges..."
                    sudo bash setup-dc.sh
                '
            """
        }
    }
}
return this

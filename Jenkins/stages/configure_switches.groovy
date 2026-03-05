def call() {
    stage('Configure OVS Switches') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${params.TARGET_HOST} '
                    set -e
                    cd ${env.REPO_DIR}

                    echo "Setting up OVS spine-leaf topology..."
                    sudo bash setup-dc.sh

                    echo "Setting port numbers..."
                    sudo bash num-ports.sh
                '
            """
        }
    }
}
return this

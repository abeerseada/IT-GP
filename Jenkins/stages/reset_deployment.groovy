def call() {
    stage('Reset Previous Deployment') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${params.TARGET_HOST} '
                    set -e
                    cd ${env.REPO_DIR}

                    echo "Destroying any existing ContainerLab topology..."
                    sudo clab destroy -t sdn-dcn.clab.yml --cleanup 2>/dev/null || true

                    echo "Removing OVS bridges..."
                    bash reset-dc.sh 2>/dev/null || true
                '
            """
        }
    }
}
return this

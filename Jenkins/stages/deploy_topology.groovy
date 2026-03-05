def call() {
    stage('Deploy ContainerLab Topology') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${params.TARGET_HOST} '
                    set -e
                    cd ${env.REPO_DIR}
                    echo "Deploying ContainerLab topology..."
                    sudo clab deploy -t sdn-dcn.clab.yml
                '
            """
        }
    }
}
return this

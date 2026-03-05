def run() {
    stage('Verify Deployment') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${params.TARGET_HOST} '
                    echo "=== ContainerLab nodes ==="
                    sudo clab inspect -t ${env.REPO_DIR}/sdn-dcn.clab.yml

                    echo ""
                    echo "=== OVS bridges ==="
                    sudo ovs-vsctl show
                '
            """
        }
    }
}
return this

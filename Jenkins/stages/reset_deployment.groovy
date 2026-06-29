def call() {
    stage('Reset Previous Deployment') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${env.TARGET_HOST} '
                    set -e
                    cd ${env.REPO_DIR}

                    echo "Destroying any existing ContainerLab topology..."
                    sudo clab destroy --all --cleanup 2>/dev/null || true

                    echo "Removing OVS bridges..."
                    sudo bash reset-dc.sh 2>/dev/null || true

                    echo "Removing leftover veth interfaces..."
                    for iface in \$(ip link show | grep -oP "(?<=\\d: )[a-z][a-z0-9]+" | grep -E "^(a|c|e)[0-9]"); do
                        sudo ip link delete \$iface 2>/dev/null || true
                    done
                '
            """
        }
    }
}
return this

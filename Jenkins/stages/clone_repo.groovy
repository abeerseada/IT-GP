def call() {
    stage('Clone / Update Repo') {
        sshagent(credentials: [env.SSH_CREDS]) {
            sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${params.TARGET_HOST} '
                    set -e
                    if [ -d "${env.REPO_DIR}/.git" ]; then
                        echo "Pulling latest changes..."
                        cd ${env.REPO_DIR} && git pull
                    else
                        echo "Cloning repo..."
                        sudo git clone ${env.REPO_URL} ${env.REPO_DIR}
                        sudo chown -R \$USER:\$USER ${env.REPO_DIR}
                    fi
                '
            """
        }
    }
}
return this

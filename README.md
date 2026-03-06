# IT-GP — Infrastructure & CI/CD Pipeline

This repository contains the complete infrastructure-as-code and CI/CD configuration for deploying and managing an SDN-based Data Center Network on AWS.

## Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                      AWS Cloud                      │
│                                                     │
│  ┌──────────────┐        ┌───────────────────────┐  │
│  │   Terraform  │───────▶│  EC2 (t3.medium)      │  │
│  │  Provisions  │        │  ├── Jenkins (8080)   │  │
│  └──────────────┘        │  ├── Prometheus (9090)│  │
│                          │  ├── Grafana (3000)   │  │
│  ┌──────────────┐        │  ├── Alertmanager     │  │
│  │    Ansible   │───────▶│  ├── OVS Switches     │  │
│  │  Configures  │        │  └── ContainerLab     │  │
│  └──────────────┘        └───────────────────────┘  │
│                                                     │
│  ┌──────────────┐                                   │
│  │   Jenkins    │──── GitHub Webhook ──▶ sdn repo   │
│  │  Automates   │                                   │
│  └──────────────┘                                   │
└─────────────────────────────────────────────────────┘
```

## Repository Structure

```
IT-GP/
├── Terraform/               # AWS infrastructure provisioning
│   ├── vpc.tf               # VPC, subnets, internet gateway
│   ├── compute.tf           # EC2, ASG, launch template (Spot instances)
│   ├── security_groups.tf   # Inbound/outbound rules
│   ├── networking.tf        # Route tables, associations
│   ├── efs.tf               # Elastic File System (shared storage)
│   ├── route53.tf           # ALB, target groups, internal DNS
│   ├── backend.tf           # S3 remote state
│   ├── variables.tf
│   └── outputs.tf
│
├── Ansible/                 # Configuration management
│   ├── site.yml             # Master playbook
│   ├── hosts.ini            # Inventory
│   ├── jenkins_master.yml   # Jenkins installation & setup
│   ├── sdn_node.yml         # OVS + ContainerLab installation
│   ├── node-exporter.yml    # Prometheus Node Exporter
│   └── apply_monitoring.yml # Prometheus + Grafana + Alertmanager
│
├── Jenkins/                 # CI/CD pipeline definitions
│   ├── Jenkinsfile          # Main pipeline
│   └── stages/
│       ├── validate_parameters.groovy
│       ├── clone_repo.groovy
│       ├── reset_deployment.groovy
│       ├── deploy_topology.groovy
│       ├── configure_switches.groovy
│       └── verify_deployment.groovy
│
└── monitoring/              # Observability stack
    ├── docker-compose.yml   # Prometheus + Grafana + Alertmanager
    ├── config/
    │   ├── prometheus.yml   # Scrape configs (Jenkins, Node Exporter)
    │   ├── alert.rules.yml  # CPU, memory, disk, container alerts
    │   └── alertmanager.yml # Slack webhook notification routing
    └── grafana/
        └── provisioning/    # Auto-provisioned dashboards & datasources
```

## Tech Stack

| Layer | Technology |
|---|---|
| Cloud Provider | AWS (eu-central-1) |
| Infrastructure | Terraform, S3 remote state |
| Compute | EC2 t3.medium, Spot Instances, Auto Scaling Group |
| Configuration Management | Ansible |
| CI/CD | Jenkins (GitHub webhook-triggered) |
| Monitoring | Prometheus + Grafana + Alertmanager |
| Alerting | Slack |
| SDN | Open vSwitch, ContainerLab, Ryu |

## Prerequisites

- AWS CLI configured with appropriate permissions
- Terraform >= 1.0
- Ansible >= 2.12
- SSH key pair

## Deployment

### 1. Provision Infrastructure

```bash
cd Terraform/
terraform init
terraform plan
terraform apply
```

### 2. Configure the Server

```bash
cd Ansible/
ansible-playbook -i hosts.ini site.yml
```

Installs and configures:
- Jenkins with required plugins
- Open vSwitch
- ContainerLab
- Prometheus, Grafana, Alertmanager via Docker Compose
- Prometheus Node Exporter

### 3. Jenkins Pipeline Setup

- Create a Pipeline job in Jenkins
- Set SCM to: `https://github.com/abeerseada/sdn`
- Script Path: `Jenkinsfile`
- Add GitHub webhook: `http://<EC2_IP>:8080/github-webhook/`

## Services & Access

| Service | URL | Description |
|---|---|---|
| Jenkins | `http://<EC2_IP>:8080` | CI/CD pipeline |
| Prometheus | `http://<EC2_IP>:9090` | Metrics collection |
| Grafana | `http://<EC2_IP>:3000` | Dashboards |
| Alertmanager | `http://<EC2_IP>:9093` | Alert routing |
| Ryu FlowManager | `http://<EC2_IP>:8081/home/index.html` | SDN controller UI |

## Alerts

Alerts are triggered for:
- Instance down
- CPU usage > 85%
- Memory usage > 85%
- Disk usage > 85%
- Container down

Notifications are delivered to Slack (`#alerts` channel).

## Related Repository

- **SDN Project**: [https://github.com/abeerseada/sdn](https://github.com/abeerseada/sdn)

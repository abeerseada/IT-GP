terraform {
  required_version = ">= 1.0"

  backend "s3" {
    bucket  = "fcis-terraform-state-2026-it"
    key     = "infra/terraform.tfstate"
    region  = "eu-central-1"
    encrypt = true
  }
}
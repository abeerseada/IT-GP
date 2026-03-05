output "vpc_id" {
  value = aws_vpc.main.id
}

output "public_subnet_1_id" {
  value = aws_subnet.public_1.id
}

output "public_subnet_2_id" {
  value = aws_subnet.public_2.id
}

output "efs_id" {
  value = aws_efs_file_system.efs.id
}

output "autoscaling_group" {
  value = aws_autoscaling_group.asg.name
}

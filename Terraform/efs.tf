resource "aws_efs_file_system" "efs" {
  creation_token = "fcis-efs"

  tags = {
    Name = "fcis-efs"
  }
}

resource "aws_efs_mount_target" "efs_mount_1" {
  file_system_id  = aws_efs_file_system.efs.id
  subnet_id       = aws_subnet.public_1.id
  security_groups = [aws_security_group.main_sg.id]
}

resource "aws_efs_mount_target" "efs_mount_2" {
  file_system_id  = aws_efs_file_system.efs.id
  subnet_id       = aws_subnet.public_2.id
  security_groups = [aws_security_group.main_sg.id]
}
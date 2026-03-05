data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

resource "aws_launch_template" "spot_template" {
  name_prefix   = "fcis-template"
  image_id      = data.aws_ami.ubuntu.id
  instance_type = "t3.medium"

  key_name = var.key_name

  vpc_security_group_ids = [aws_security_group.main_sg.id]

  instance_market_options {
    market_type = "spot"
  }

  tag_specifications {
    resource_type = "instance"

    tags = {
      Name = "fcis-instance"
    }
  }
}

resource "aws_autoscaling_group" "asg" {
  desired_capacity    = 2
  max_size            = 2
  min_size            = 2
  vpc_zone_identifier = [
    aws_subnet.public_1.id,
    aws_subnet.public_2.id
  ]

  launch_template {
    id      = aws_launch_template.spot_template.id
    version = "$Latest"
  }

  tag {
    key                 = "Name"
    value               = "fcis-asg"
    propagate_at_launch = true
  }
}
aws s3api create-bucket \
--bucket fcis-terraform-state-2026-it \
--region eu-central-1 \
--create-bucket-configuration LocationConstraint=eu-central-1

sudo docker exec clab-fat-tree-k4-h011 ping -c 3 10.0.1.3


cd /opt/sdn && git pull
sudo docker restart clab-fat-tree-k4-ctrl
sleep 5

# تيست: pod1 → pod2 
sudo docker exec clab-fat-tree-k4-h111 ping -c 2 10.2.1.1

# تيست: pod0 → pod3 
sudo docker exec clab-fat-tree-k4-h011 ping -c 2 10.3.1.1
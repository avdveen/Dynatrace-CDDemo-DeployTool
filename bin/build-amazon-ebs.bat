@echo off
call settings\set-amazon-ec2-env.bat
packer.exe build -only=Debian-7-amd64-amazon-ebs -var "ansible_role=easytravel-cd-debian" packer.json
#packer.exe build -debug -only=Debian-7-amd64-amazon-ebs -var "ansible_role=easytravel-cd-debian" packer.json

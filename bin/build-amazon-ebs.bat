@echo off
call settings\set-amazon-ec2-env.bat
packer.exe build -only=Dynatrace-CDDemo-AWS -var "ansible_role=easytravel-cd-debian disable_password_authentication=yes" packer.json
#packer.exe build -debug -only=Dynatrace-CDDemo-AWS -var "ansible_role=easytravel-cd-debian disable_password_authentication=yes" packer.json

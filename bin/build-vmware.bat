@echo off
call settings\vm-settings.bat
packer.exe build -only=Debian-7-amd64-vmware -var "ansible_role=easytravel-cd-debian" packer.json

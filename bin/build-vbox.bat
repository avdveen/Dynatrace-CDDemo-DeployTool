@echo off
call settings\vm-settings.bat
packer.exe build -only=Debian-7-amd64-vbox -var "ansible_role=easytravel-cd-debian disable_password_authentication=no" packer.json

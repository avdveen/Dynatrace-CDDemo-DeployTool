@echo off
call settings\vm-settings.bat
packer.exe build -only=Dynatrace-CDDemo-VBox -var "ansible_role=easytravel-cd-debian disable_password_authentication=no" packer.json

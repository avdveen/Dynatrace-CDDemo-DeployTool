vagrant.exe plugin install vagrant-aws

..\..\settings\set-amazon-ec2-env.bat
vagrant.exe up --provider=aws

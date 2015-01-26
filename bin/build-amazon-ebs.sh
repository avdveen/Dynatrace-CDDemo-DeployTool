#!/bin/sh

ANSIBLE_ROLE=easytravel-cd-debian

if [ "$1" != "" ]; then
	ANSIBLE_ROLE=$1
fi

. ./settings/set-amazon-ec2-env.sh
packer build -only=Debian-7-amd64-amazon-ebs -var "ansible_role=$ANSIBLE_ROLE disable_password_authentication=yes" packer.json
#packer build -debug -only=Debian-7-amd64-amazon-ebs -var "ansible_role=$ANSIBLE_ROLE disable_password_authentication=yes" packer.json

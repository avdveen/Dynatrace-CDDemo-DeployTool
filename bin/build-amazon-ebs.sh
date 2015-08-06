#!/bin/sh

ANSIBLE_ROLE=easytravel-cd-debian

if [ "$1" != "" ]; then
	ANSIBLE_ROLE=$1
fi

. ./settings/set-amazon-ec2-env.sh
packer build -only=Dynatrace-CDDemo-AWS -var "ansible_role=$ANSIBLE_ROLE disable_password_authentication=yes" packer.json
#packer build -debug -only=Dynatrace-CDDemo-AWS -var "ansible_role=$ANSIBLE_ROLE disable_password_authentication=yes" packer.json

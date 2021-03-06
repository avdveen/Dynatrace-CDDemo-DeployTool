#!/bin/sh

ANSIBLE_ROLE=easytravel-cd-debian

if [ "$1" != "" ]; then
	ANSIBLE_ROLE=$1
fi

. ./settings/vm-settings.sh
packer build -only=Dynatrace-CDDemo-VMware -var "ansible_role=$ANSIBLE_ROLE disable_password_authentication=no" packer.json

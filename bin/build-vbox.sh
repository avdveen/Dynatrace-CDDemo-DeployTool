#!/bin/sh

ANSIBLE_ROLE=easytravel-cd-debian

if [ "$1" != "" ]; then
	ANSIBLE_ROLE=$1
fi

. ./settings/vm-settings.sh
packer build -only=Debian-7-amd64-vbox -var "ansible_role=$ANSIBLE_ROLE" packer.json

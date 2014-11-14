#!/bin/bash
sudo -E ansible-playbook -i'localhost,' --connect=local {{ item }}/playbook.yml -e "artifact_directory=$1"

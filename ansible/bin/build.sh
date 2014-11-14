#!/bin/sh

./bin/AnsiblePlaybookDistribute.groovy -r easytravel-cd-deploy
mv playbook.zip roles/easytravel-cd/files/easytravel-cd-deploy-combined-playbook.zip 

./bin/AnsiblePlaybookDistribute.groovy -r easytravel-cd-deploy-customer-frontend
mv playbook.zip roles/easytravel-cd/files/easytravel-cd-deploy-customer-frontend-playbook.zip 

./bin/AnsiblePlaybookDistribute.groovy -r easytravel-cd-deploy-business-backend
mv playbook.zip roles/easytravel-cd/files/easytravel-cd-deploy-business-backend-playbook.zip 

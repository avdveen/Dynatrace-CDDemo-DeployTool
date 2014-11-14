# README for Ansible Role: dynatrace

- Installs the Collector, Server and latest PostgreSQL as Performance Warehouse
- Automatically determines and sets the Dynatrace Server deployment size upon boot
- Sets Dynatrace settings (Performance Warehouse, Session Storage) upon each start
- Installs a fix pack if it is available in 'files/dynatrace.dtf'
- Installs a license if it is available in 'files/dtlicense.key, otherwise: BYOL!

## Ports

- Dynatrace Client: 2020/tcp
- Dynatrace Client: 2021/tcp
- Dynatrace Collector: 6698/tcp
- Dynatrace Collector: 6699/tcp
- Dynatrace Server: 8020/tcp
- Dynatrace Server: 8021/tcp
- Dynatrace Agents: 9998/tcp

## Environment Vars

- DYNATRACE_STORED_SESSIONS_MAX_SIZE_MB

## Requirements

- A Dynatrace full installer for 64-bit Linux must be provided in 'files/dynatrace.jar'.
  Can be obtained here: https://downloads.compuwareapm.com/downloads/download.aspx?p=DT

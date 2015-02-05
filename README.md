# Deployment Tool for the "Continuous Delivery with Dynatrace Demo Environment" 

This framework automates the provisioning of demo environments that show the capabilities around Continuous Delivery and DevOps with [Dynatrace](http://bit.ly/dttrial).

The framework is based on [Packer](https://packer.io/), [Vagrant](https://www.vagrantup.com/) and [Ansible](http://www.ansible.com/) and runs on Linux and Windows.

Currently, the framework is able to create a [demo environment](https://community.compuwareapm.com/community/display/COE/Continuous+Delivery+with+Dynatrace+Demo+Environment) inside a single virtual machine (running a recent Debian Linux) for the following platforms:

- [VirtualBox](http://virtualbox.org/)
- [VMware Fusion](http://vmware.com/products/fusion/) or [Workstation](http://vmware.com/products/workstation/)
- [Amazon EC2](http://aws.amazon.com/ec2/) (you'll need to bring your own account!)
    - Creation of an AMI (backed by EBS)
    - Launching an Instance from an AMI

Please see the [Project Homepage](https://community.compuwareapm.com/community/pages/viewpage.action?pageId=179737118) for detailed instructions.

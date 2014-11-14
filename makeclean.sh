find . -name .DS_Store -exec rm -rf {} \;

rm -rf keys/* ansible/keys/*
rm -rf output
rm -rf packer_cache

cp settings/set-amazon-ec2-env.sh.orig settings/set-amazon-ec2-env.sh

find vagrant -name \*.box -exec rm -rf {} \;
find vagrant -name .vagrant -exec rm -rf {} \;

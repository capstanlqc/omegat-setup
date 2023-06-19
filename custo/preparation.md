# How to update the hash file

git clone ORG/omegat-user-config
cd omegat-user-config
find * -type f -exec md5sum {} \; > SHA1SUM

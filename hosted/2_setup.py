sudo apt -y update

# apt install -y software-properties-common
# sudo add-apt-repository -y ppa:deadsnakes/ppa
# sudo apt -y update
# sudo apt -y install python3.10
# sudo apt -y install python3.10-venv
#sudo apt -y install python3
#sudo apt-get -y install python3-venv

## -- install android sdk
sudo apt -y install android-sdk

## -- install docker
for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do sudo apt-get remove $pkg; done

# Add Docker's official GPG key:
sudo apt-get -y update
sudo apt-get -y install ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Add the repository to Apt sources:
echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get -y update

sudo apt-get -y install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker

docker run hello-world

## -- install virtualization
egrep -c '(vmx|svm)' /proc/cpuinfo
sudo apt-get -y install qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils
sudo apt install cpu-checker
kvm-ok

# ## -- install node
# sudo apt -y install nodejs npm
#
# ## -- install scripts
#
# git clone https://github.com/google/android-emulator-container-scripts.git
# cd android-emulator-container-scripts/
# source ./configure.sh
# emu-docker -h
# emu-docker licenses
# emu-docker interactive --start


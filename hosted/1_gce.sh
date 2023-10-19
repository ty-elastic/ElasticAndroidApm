export LABEL_DIVISION=""
export LABEL_ORG=""
export LABEL_TEAM=""
export LABEL_PROJECT=""
export SERVICE_ACCOUNT=""

export GCE_PROJECT=""
export GCE_ZONE=""

gcloud compute instances create android-apm \
    --project=$GCE_PROJECT \
    --zone=$GCE_ZONE \
    --machine-type=n2-standard-4 \
    --network-interface=network-tier=PREMIUM,stack-type=IPV4_ONLY,subnet=default \
    --maintenance-policy=MIGRATE \
    --provisioning-model=STANDARD \
    --service-account=$SERVICE_ACCOUNT \
    --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append \
    --min-cpu-platform=Intel\ \
Cascade\ Lake \
    --tags=http-server,https-server \
    --create-disk=auto-delete=yes,boot=yes,device-name=android-apm,image=projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20230918,mode=rw,size=250,type=projects/$GCE_PROJECT/zones/$GCE_ZONE/diskTypes/pd-balanced \
    --no-shielded-secure-boot \
    --shielded-vtpm \
    --shielded-integrity-monitoring \
    --labels=division=$LABEL_DIVISION,org=$LABEL_ORG,team=$LABEL_TEAM,project=$LABEL_PROJECT,goog-ec-src=vm_add-gcloud \
    --reservation-affinity=any \
    --enable-nested-virtualization

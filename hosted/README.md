These are scripts intended to help you run/test this Android App headless in GCE.

# Setup

1. modify `1_gce.sh` with appropriate env variables (top)
2. execute `1_gce.sh` on your local machine (with appropriate GCE permissions) to create a suitable VM (note `--enable-nested-virtualization`)
3. scp `2_setup.sh` and `3_run.sh` to your VM
4. ssh into VM and execute `2_setup.sh` to install dependencies

# Run
1. build apk on your local machine (Android Studio > Build > Build APK)
2. scp `app/build/outputs/apk/debug/app-debug.apk` to your VM
3. ssh into VM and execute `3_run.sh` to launch emulator, install apk, and run android app
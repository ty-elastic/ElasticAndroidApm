docker run -d \
  -e ADBKEY="$(cat ~/.android/adbkey)" \
  --device /dev/kvm \
  --publish 8554:8554/tcp \
  --publish 5555:5555/tcp  \
  us-docker.pkg.dev/android-emulator-268719/images/30-google-x64:30.1.2
adb connect localhost:5555
adb wait-for-device
adb install app-debug.apk
adb shell am start -n com.example.elasticapm/com.example.elasticapm.MainActivity
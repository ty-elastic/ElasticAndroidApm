docker kill demo1
docker rm demo1
docker run -d \
  --name "demo1" \
  -e ADBKEY="$(cat ~/.android/adbkey)" \
  --device /dev/kvm \
  --publish 5555:5555/tcp  \
  us-docker.pkg.dev/android-emulator-268719/images/30-google-x64:30.1.2
adb connect localhost:5555
adb -s localhost:5555 wait-for-device
adb -s localhost:5555 install demo1.apk
adb -s localhost:5555 shell am start -n com.example.elasticapm/com.example.elasticapm.MainActivity

docker kill demo2
docker rm demo2
docker run -d \
  --name "demo2" \
  -e ADBKEY="$(cat ~/.android/adbkey)" \
  --device /dev/kvm \
  --publish 5556:5555/tcp  \
  us-docker.pkg.dev/android-emulator-268719/images/30-google-x64:30.1.2
adb connect localhost:5556
adb -s localhost:5556 wait-for-device
adb -s localhost:5556 install demo2.apk
adb -s localhost:5556 shell am start -n com.example.elasticapm/com.example.elasticapm.MainActivity
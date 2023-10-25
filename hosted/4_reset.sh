adb -s localhost:5555 shell su root date $(date +%m%d%H%M%Y.%S)
adb -s localhost:5555 shell am force-stop com.example.elasticapm
adb -s localhost:5555 shell am start -n com.example.elasticapm/com.example.elasticapm.MainActivity

adb -s localhost:5556 shell su root date $(date +%m%d%H%M%Y.%S)
adb -s localhost:5556 shell am force-stop com.example.elasticapm
adb -s localhost:5556 shell am start -n com.example.elasticapm/com.example.elasticapm.MainActivity
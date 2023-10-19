# Elastic Android APM Example using OTLP/HTTP

This is an exemplary project which demonstrates using the Elastic Android APM Agent to export:
* traces (manual and automatic)
* metrics (manual)
* logs (hooked into Logback)
* exceptions
using protobuf/HTTP1.1 (OTLP/HTTP1.1) or gRPC/HTTP2.

## Building

Create a file `app/local.properties` which contains
```
elastic.apm.serverUrl=(your Elastic APM ingest URL)
elastic.apm.secretToken=(your Elastic APM secret token)
```

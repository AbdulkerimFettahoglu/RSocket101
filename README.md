# RSocket101
We will walk through all types of communication between agents that is provided by RSocket. There is a presentation about RSocket, feel free to download and examine it.

### How To Test "rsocket-server"
You should use "rsocket-cli". You can find the commands in this document to test your server. To see more details about RSocket CLI visit its [GitHub page.](https://github.com/making/rsc)

Test endpoint of your "request response" model.
```
java -jar rsc.jar --debug --request --data "{\"reflector\":\"value\"}" --route request-response tcp://localhost:7000
```
Test endpoint of your "fire and forget" model.
```
java -jar rsc.jar --debug --fnf --data "{\"reflector\":\"value\"}" --route fire-and-forget tcp://localhost:7000
```
Test endpoint of your "stream" model.
```
java -jar rsc.jar --debug --stream --data "{\"reflector\":\"value\"}" --route stream tcp://localhost:7000
```
Test endpoint of your "channel" model.
```
java -jar rsc.jar --debug --channel --data - --route channel tcp://localhost:7000
```
package io.pivotal.rsocketserver;

import io.pivotal.rsocketserver.data.Message;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class RSocketController {
	
	/**
	 * To send a example message via RSocket CLI use following command:
	 * java -jar rsc.jar --debug --request --data "{\"reflector\":\"value\"}" --route request-response tcp://localhost:7000
	 * To download rsc.jar: https://github.com/making/rsc/releases
	 * 
     * @param request
     * @return Message
     */
    @MessageMapping("request-response")
    Message requestResponse(Message request) {
        log.info("Received request-response request: {}", request);
        // create a single Message and return it
        return new Message(request.getReflector() + " reflected.");
    }
    
	/**
	 * To send a example message via RSocket CLI use following command:
	 * java -jar rsc.jar --debug --fnf --data "{\"reflector\":\"value\"}" --route fire-and-forget tcp://localhost:7000
	 * To download rsc.jar: https://github.com/making/rsc/releases
	 * 
     * @param request
     * @return Message
     */
    @MessageMapping("fire-and-forget")
    void fireAndForget(Message request) {
        log.info("Received fire-and-forget request: {}", request);
    }

	/**
	 * To send a example message via RSocket CLI use following command:
	 * java -jar rsc.jar --debug --stream --data "{\"reflector\":\"value\"}" --route stream tcp://localhost:7000
	 * To download rsc.jar: https://github.com/making/rsc/releases
	 * 
     * @param request
     * @return Message
     */
    @MessageMapping("stream")
    Flux<Message> stream(Message request) {
        log.info("Received stream request: {}", request);
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(index -> new Message(index.toString()))
                .log();
    }
    
	/**
	 * To send a example message via RSocket CLI use following command:
	 * java -jar rsc.jar --debug --channel --data - --route channel tcp://localhost:7000
	 * To download rsc.jar: https://github.com/making/rsc/releases
	 * 
     * @param request
     * @return Message
     */
    @MessageMapping("channel")
    Flux<Message> channel(final Flux<Integer> settings) {
        return settings
                    .doOnNext(setting -> log.info("\nFrequency setting is {} second(s).\n", setting))
                    .doOnCancel(() -> log.warn("client canceled the chanel"))
                    .switchMap(setting -> Flux.interval(Duration.ofSeconds(setting))
                                                   .map(index -> new Message(index.toString())))
                                                   .log();
    }
}

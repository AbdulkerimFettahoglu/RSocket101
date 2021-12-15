package io.pivotal.rsocketserver;

import io.pivotal.rsocketserver.data.Message;
import lombok.extern.slf4j.Slf4j;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Calendar;

import javax.xml.datatype.DatatypeConstants.Field;

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
    @MessageMapping("channel")
    Flux<Message> channel(final Flux<Duration> settings) {
        return settings
                    .doOnNext(setting -> log.info("\nFrequency setting is {} second(s).\n", setting.getSeconds()))
                    .switchMap(setting -> Flux.interval(setting)
                                                   .map(index -> new Message(index.toString())))
                                                   .log();
    }

}

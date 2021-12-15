package io.pivotal.rsocketserver;

import io.pivotal.rsocketserver.data.Message;
import lombok.extern.slf4j.Slf4j;
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

}

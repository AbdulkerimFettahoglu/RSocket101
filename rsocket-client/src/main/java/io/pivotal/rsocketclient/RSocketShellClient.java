package io.pivotal.rsocketclient;


import io.pivotal.rsocketclient.data.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
public class RSocketShellClient {

	// Add a global class variable for the RSocketRequester
	private final RSocketRequester rsocketRequester;

	// Use an Autowired constructor to customize the RSocketRequester and store a reference to it in the global variable
	@Autowired
	public RSocketShellClient(RSocketRequester.Builder rsocketRequesterBuilder) {
	    this.rsocketRequester = rsocketRequesterBuilder
	            .connectTcp("localhost", 7000).block();
	}
	
    @ShellMethod("Send one request. One response will be printed.")
    public void requestResponse() throws InterruptedException {
        log.info("\nSending one request. Waiting for one response...");
        Message message = this.rsocketRequester
                .route("request-response")
                .data(new Message("message from eclipse client"))
                .retrieveMono(Message.class)
                .block();
        log.info("\nResponse was: {}", message);
    }
}

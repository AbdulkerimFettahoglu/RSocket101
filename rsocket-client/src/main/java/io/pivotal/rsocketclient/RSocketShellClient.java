package io.pivotal.rsocketclient;


import io.pivotal.rsocketclient.data.Message;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
public class RSocketShellClient {

	// Add a global class variable for the RSocketRequester
	private final RSocketRequester rsocketRequester;
	
	private static Disposable disposable;

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
    
    @ShellMethod("Send one request. No response will be returned.")
    public void fireAndForget() throws InterruptedException {
            log.info("\nFire-And-Forget. Sending one request. Expect no response (check server console log)...");
            this.rsocketRequester
                    .route("fire-and-forget")
                    .data(new Message("message from eclipse client (fnf)"))
                    .send()
                    .block();
    }
    
    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void stream() {
        log.info("\nRequest-Stream. Sending one request. Waiting for unlimited responses (Stop process to quit)...");
        this.disposable = this.rsocketRequester
                .route("stream")
                .data(new Message("message from eclipse client (stream)"))
                .retrieveFlux(Message.class)
                .subscribe(er -> log.info("Response received: {}", er));
    }
    
    @ShellMethod("Stream some settings to the server. Stream of responses will be printed.")
	public void channel() {
		Mono<Integer> setting1 = Mono.just(1);
		Mono<Integer> setting2 = Mono.just(3).delayElement(Duration.ofSeconds(5));
		Mono<Integer> setting3 = Mono.just(5).delayElement(Duration.ofSeconds(15));

		Flux<Integer> settings = Flux.concat(setting1, setting2, setting3)
				.doOnNext(d -> log.info("\nSending setting for {}-second interval.\n", Duration.ofSeconds(d)));
		
		disposable = this.rsocketRequester
                .route("channel")
                .data(settings)
                .retrieveFlux(Message.class)
                .subscribe(message -> log.info("Received: {} (Type 's' to stop.)", message));
	}
    
    @ShellMethod("Stop streaming messages from the server.")
    public void s(){
        if(null != disposable){
            disposable.dispose();
        }
    }
}

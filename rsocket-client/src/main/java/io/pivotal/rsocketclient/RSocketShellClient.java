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
    
	@ShellMethod("Stops Streams or Channels.")
	public void s() {
		if (disposable != null ) {
			log.info("Stopping the current stream.");
			disposable.dispose();
			log.info("Stream stopped.");
		}
	}
}

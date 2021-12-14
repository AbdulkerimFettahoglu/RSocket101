package io.pivotal.rsocketserver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String reflector;
    private long created = Instant.now().getEpochSecond();

    public Message(String origin) {
        this.reflector = origin;
    }
}
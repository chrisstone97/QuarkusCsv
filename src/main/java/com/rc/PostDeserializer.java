package com.rc;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PostDeserializer extends ObjectMapperDeserializer<User> {
    public PostDeserializer() {
        super(User.class);
    }
}

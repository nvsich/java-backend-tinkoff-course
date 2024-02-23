package edu.java.scrapper.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class UnixEpochTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        long time = jsonParser.getLongValue();
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneOffset.UTC);
    }
}

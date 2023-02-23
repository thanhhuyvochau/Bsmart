package fpt.project.bsmart.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class JsonDateDeserializer extends JsonDeserializer<Instant> {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            String date = jsonParser.getText();
            return LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }
}
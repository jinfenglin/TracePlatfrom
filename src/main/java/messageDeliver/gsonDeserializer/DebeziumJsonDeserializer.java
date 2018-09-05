package messageDeliver.gsonDeserializer;

import com.google.gson.*;
import messageDeliver.DebeziumEvent;

import java.lang.reflect.Type;

/**
 * Gson deserializer for Debezium Json response.
 */
public class DebeziumJsonDeserializer implements JsonDeserializer<DebeziumEvent> {
    @Override
    public DebeziumEvent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jobject = jsonElement.getAsJsonObject();
        JsonElement payload = jobject.get("payload");
        JsonObject payloadObj = payload.getAsJsonObject();
        String before = payloadObj.get("before").getAsJsonObject().toString();
        String after = payloadObj.get("after").getAsJsonObject().toString();

        JsonObject sourceObj = payloadObj.getAsJsonObject("source");
        String db = sourceObj.get("db").getAsString();
        String table = sourceObj.get("table").getAsString();
        String brokerId = sourceObj.get("server_id").getAsString();
        String connectorName = sourceObj.get("name").getAsString();

        String operationCode = payloadObj.get("op").getAsString();

        DebeziumEvent event = new DebeziumEvent();
        event.setBefore(before);
        event.setAfter(after);
        event.setDbName(db);
        event.setKafkaBrokerId(brokerId);
        event.setOpCode(operationCode);
        event.setTableName(table);
        event.setConnectorName(connectorName);
        return event;
    }
}

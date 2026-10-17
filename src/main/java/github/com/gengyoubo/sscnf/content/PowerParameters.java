package github.com.gengyoubo.sscnf.content;
import com.google.gson.*;
/** Java declaration helpers for immutable gameplay configuration trees. */
public final class PowerParameters {
    private PowerParameters() {}
    public static JsonObject object(Object... entries) {
        if (entries.length % 2 != 0) throw new IllegalArgumentException("Expected key/value pairs");
        JsonObject result = new JsonObject();
        for (int i = 0; i < entries.length; i += 2) result.add((String) entries[i], value(entries[i + 1]));
        return result;
    }
    public static JsonArray array(Object... entries) {
        JsonArray result = new JsonArray(); for (Object entry : entries) result.add(value(entry)); return result;
    }
    private static JsonElement value(Object value) {
        if (value == null) return JsonNull.INSTANCE;
        if (value instanceof JsonElement json) return json;
        if (value instanceof Boolean b) return new JsonPrimitive(b);
        if (value instanceof Number n) return new JsonPrimitive(n);
        return new JsonPrimitive((String) value);
    }
}

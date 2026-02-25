package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonUtil {

    public static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    public static ObjectNode getEmpty() {
        return DEFAULT_OBJECT_MAPPER.createObjectNode();
    }

    private JacksonUtil() {
    }

}

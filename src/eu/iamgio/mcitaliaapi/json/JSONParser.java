package eu.iamgio.mcitaliaapi.json;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Gio
 */
public class JSONParser {

    private HttpConnection connection;

    public JSONParser(String url) {
        this.connection = new HttpConnection(url);
    }

    public JSONObject parse() {
        try {
            return (JSONObject) JSONValue.parseWithException(connection.read());
        } catch(Exception e) {
            return null;
        }
    }
}

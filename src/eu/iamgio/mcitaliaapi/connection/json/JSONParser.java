package eu.iamgio.mcitaliaapi.connection.json;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.nodes.Document;

/**
 * @author Gio
 */
public class JSONParser {

    private String json;

    public JSONParser(String url) throws RuntimeException {
        try {
            this.json = new HttpConnection(url).read();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    public JSONParser(Document document) {
        this.json = document.body().html();
    }

    public JSONParser(String json, /*Unused parameter*/ boolean isJson) {
        this.json = json;
    }

    public JSONObject parse() {
        try {
            return (JSONObject) JSONValue.parseWithException(json);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray parseArray() {
        try {
            return (JSONArray) JSONValue.parseWithException(json);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

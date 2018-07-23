package eu.iamgio.mcitaliaapi.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class
 * @author Gio
 */
public class Utils {

    private Utils() {}

    public static Date getDateByTimestamp(String timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp + "000"));
        return calendar.getTime();
    }

    public static long[] longJsonArrayToLongArray(JSONArray array) {
        long[] arr = new long[array.size()];
        for(int i = 0; i < array.size(); i++) {
            long value;
            Object obj = array.get(i);
            if(obj instanceof Long) {
                value = (long) obj;
            } else {
                value = Long.parseLong(obj.toString());
            }
            arr[i] = value;
        }
        return arr;
    }

    public static String retrieveErrorFromJson(JSONObject object) {
        try {
            JSONArray errors = (JSONArray) object.get("errors");
            if(errors != null) {
                String error = errors.get(0).toString();
                return error.startsWith("[\"") && error.endsWith("\"]") ? error.substring(2, error.length() - 2) : error;
            }
        } catch(NullPointerException e) {
            return null;
        }
        return null;
    }
}

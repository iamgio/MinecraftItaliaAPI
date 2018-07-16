package eu.iamgio.mcitaliaapi.util;

import org.json.simple.JSONArray;

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
}

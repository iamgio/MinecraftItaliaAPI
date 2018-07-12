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
            arr[i] = (long) array.get(i);
        }
        return arr;
    }
}

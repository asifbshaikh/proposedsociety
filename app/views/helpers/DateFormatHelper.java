package views.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormatHelper {
    private static final Map<String, ThreadLocal<SimpleDateFormat>> formatters = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    public static String formatDate(final String format, Date date) {
        ThreadLocal<SimpleDateFormat> tl = formatters.get(format);
        
        if (tl == null) {
            tl = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(format);
                }
            };
            
            formatters.put(format, tl);
        }
        
        return tl.get().format(date);
    }
}

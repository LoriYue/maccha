package org.maccha.spring;

import java.util.Locale;

/**
 * Created by Lori Yue on 16-3-3.
 */
public class MessageUtils {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    public static String getMessage(String key, Object[] arguments) {
        return getMessage(key, arguments, DEFAULT_LOCALE);
    }

    public static String getMessage(String key, Object[] arguments, Locale locale) {
        return SpringManager.getApplicationContext().getMessage(key, new Object[0], locale);
    }

    public static String getMessage(String key)
    {
        return getMessage(key, null);
    }
}

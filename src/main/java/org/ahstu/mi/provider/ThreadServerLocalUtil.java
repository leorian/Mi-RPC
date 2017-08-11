package org.ahstu.mi.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/18.
 */
public class ThreadServerLocalUtil {


    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    @SuppressWarnings("unchecked")
    public static <T> T get(final String key) {
        Map<String, Object> map = ThreadServerLocalUtil.threadLocal.get();
        if (map == null) {
            return null;
        }

        return (T) map.get(key);
    }

    public static void set(final String key, final Object value) {
        Map<String, Object> map = ThreadServerLocalUtil.threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            ThreadServerLocalUtil.threadLocal.set(map);
        }

        map.put(key, value);
    }

    public static void remove() {
        ThreadServerLocalUtil.threadLocal.remove();
    }

    public static void remove(final String key) {
        Map<String, Object> map = ThreadServerLocalUtil.threadLocal.get();
        if(map == null){
            return;
        }

        map.remove(key);
    }
}

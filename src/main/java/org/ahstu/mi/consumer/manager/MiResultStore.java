package org.ahstu.mi.consumer.manager;

import org.ahstu.mi.common.MiResult;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/17.
 */
public class MiResultStore {

    private static Map<String,MiResult> miResultMap =new Hashtable<String, MiResult>();


    public static void add(MiResult miResult){
        miResultMap.put(miResult.getRequestId(),miResult);
    }

    public static MiResult getAndRemove(String requestId){
        MiResult result = miResultMap.get(requestId);
        miResultMap.remove(requestId);
        return result;
    }

    public static void remove(String requestId){
        miResultMap.remove(requestId);
    }

}

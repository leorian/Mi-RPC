package org.ahstu.mi.consumer.manager;

import org.ahstu.mi.common.MiResult;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/17.
 */
public class MiResultStore {

    private static Map<String,MiResult> insistResultMap =new Hashtable<String, MiResult>();


    public static void add(MiResult insistResult){
        insistResultMap.put(insistResult.getRequestId(),insistResult);
    }

    public static MiResult getAndRemove(String requestId){
        MiResult result = insistResultMap.get(requestId);
        insistResultMap.remove(requestId);
        return result;
    }

    public static void remove(String requestId){
        insistResultMap.remove(requestId);
    }

}

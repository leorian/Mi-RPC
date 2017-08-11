package org.ahstu.mi.consumer;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiUtil;

import java.util.*;

/**
 * Created by renyueliang on 17/5/15.
 */
public class InsistConsumerStore {

    private  static  Map<String,MiConsumerMeta> consumerStore = new Hashtable<String, MiConsumerMeta>();

    public static void add(MiConsumerMeta meta){
        add(MiUtil.serviceGroupVersionCreateKey(meta.getInterfaceName(),
                meta.getGroup(),
                meta.getVersion()),meta);
    }

    public static void  add(String key,MiConsumerMeta meta){
        if(StringUtil.isBlank(key) || meta==null){
            return;
        }
        consumerStore.put(key,meta);

    }

    public static Collection<MiConsumerMeta> getAll(){
        return consumerStore.values();
    }

}

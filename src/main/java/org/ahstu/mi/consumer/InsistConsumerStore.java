package org.ahstu.mi.consumer;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiUtil;

import java.util.*;

/**
 * Created by renyueliang on 17/5/15.
 */
public class InsistConsumerStore {

    private  static  Map<String,InsistConsumerMeta> consumerStore = new Hashtable<String, InsistConsumerMeta>();

    public static void add(InsistConsumerMeta meta){
        add(MiUtil.serviceGroupVersionCreateKey(meta.getInterfaceName(),
                meta.getGroup(),
                meta.getVersion()),meta);
    }

    public static void  add(String key,InsistConsumerMeta meta){
        if(StringUtil.isBlank(key) || meta==null){
            return;
        }
        consumerStore.put(key,meta);

    }

    public static Collection<InsistConsumerMeta> getAll(){
        return consumerStore.values();
    }

}

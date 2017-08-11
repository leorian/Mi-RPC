package org.ahstu.mi.provider;

import org.ahstu.mi.common.MiUtil;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/15.
 */
public class InsistProviderStore  {

    private static Map<String,InsistProviderMeta> providerMetaMap =new Hashtable<String, InsistProviderMeta>();




    public static void add(InsistProviderMeta insistProviderMeta){
        String serviceNameGroupVersion= MiUtil.serviceGroupVersionCreateKey(
                insistProviderMeta.getInterfaceName(),
                insistProviderMeta.getGroup(),
                insistProviderMeta.getVersion());

        add(serviceNameGroupVersion,insistProviderMeta);
    }

    public static void add(String serviceNameGroupVersion,InsistProviderMeta insistProviderMeta){
        providerMetaMap.put(serviceNameGroupVersion,insistProviderMeta);
    }

    public static InsistProviderMeta get(String serviceNameGroupVersion){
        return providerMetaMap.get(serviceNameGroupVersion);
    }

    public static Collection<InsistProviderMeta> getAll(){
        return providerMetaMap.values();
    }


}

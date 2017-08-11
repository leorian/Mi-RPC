package org.ahstu.mi.consumer.manager;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.MiException;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.lock.MiLock;
import org.ahstu.mi.module.ServiceMeta;

import java.util.*;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiServiceList {

    MiLock lock;

    List<ServiceMeta> providers = new Vector<ServiceMeta>();

    Map<String, ServiceMeta> providersMap = new Hashtable<String, ServiceMeta>();

    MiAtomicInteger index = new MiAtomicInteger(0);

    MiAtomicInteger indexReset = new MiAtomicInteger(0);

    int max;

    int indexMax = 1000 * 100;

    private String serviceNameGroupVersion;

    public MiServiceList(List<ServiceMeta> providers, String serviceNameGroupVersion) {
        this.max = providers.size();
        this.providers = providers;
        this.serviceNameGroupVersion = serviceNameGroupVersion;
        lock = new MiLock(serviceNameGroupVersion);
        index = new MiAtomicInteger(0);


        if (this.max == 0) {
            return;
        }

        for (ServiceMeta serviceMeta : providers) {
            providersMap.put(MiUtil.ipAndPortCreateKey(serviceMeta), serviceMeta);
            MiServiceStore.addByIpAndPort(serviceMeta);
        }
    }


    public String getServiceNameGroupVersion() {
        return serviceNameGroupVersion;
    }

    public synchronized void delService(String key) {

        ServiceMeta serviceMeta =  providersMap.get(key);
        if(serviceMeta==null){
            return ;
        }
        for(ServiceMeta p : providers){

            if(p.getId().equals(serviceMeta.getId())){
                providers.remove(p);
                break;
            }

        }

        providersMap.remove(key);

    }

    public synchronized void addService(ServiceMeta serviceMeta) {

        ServiceMeta p =  providersMap.get(MiUtil.ipAndPortCreateKey(serviceMeta));

        if(p==null){
            providersMap.put(MiUtil.ipAndPortCreateKey(serviceMeta),p);
            providers.add(p);
        }


    }

    private void resetIndex() {

       int threadNum = indexReset.addAndGetNew(1);

       if(threadNum==1){
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   index.set(0);
                   indexReset.set(0);
               }
           });
           thread.start();
       }

    }

    public ServiceMeta getService() {

        if(providers.isEmpty()){
            throw new MiException(MiError.NOT_FIND_SERVICE);
        }

        try {
            int nowIndex = index.addAndGetNew(1);
            int currentIndex = nowIndex <= 0 ? 0 : (nowIndex - 1) % this.max;

            if(providers.size()<=currentIndex){
                resetIndex();
                return getService();
            }

            ServiceMeta serviceMeta = providers.get(currentIndex);

            if (serviceMeta == null && currentIndex <= 0) {
                throw new MiException(MiError.NOT_FIND_SERVICE);
            }

            if (serviceMeta == null && currentIndex > 0) {
                resetIndex();
                return getService();
            }

            //
            if (nowIndex > this.indexMax) {
                resetIndex();
            }

            return serviceMeta;
        }catch (IndexOutOfBoundsException iobe){

            if(providers.isEmpty()){
                throw new MiException(MiError.NOT_FIND_SERVICE);
            }

            resetIndex();
            return getService();
        }catch (Throwable e){
            MiLogger.record(StringUtil.format("MiServiceList.getService error ! serviceNameGroupVersion%s index:%s ",serviceNameGroupVersion,index.get()),e);
            throw new MiException(e.getMessage(),e);
        }
    }

    public static void main(String[] args){
        System.out.println(0%10);
    }



}

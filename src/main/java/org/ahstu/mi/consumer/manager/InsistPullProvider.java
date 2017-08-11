package org.ahstu.mi.consumer.manager;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiConstants;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.consumer.MiConsumerStore;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.zk.MiZkClient;
import org.ahstu.mi.zk.ZkChildrenWatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by renyueliang on 17/5/15.
 */
public class InsistPullProvider  {


    private static Set<String> syncPathStore = new HashSet<String>();

    private static boolean addPath(String path){
        if(StringUtil.isBlank(path)){
            return false;
        }

        if(syncPathStore.contains(path)){
            return false;
        }

        synchronized (InsistPullProvider.class){

            if(syncPathStore.contains(path)){
                return false;
            }

            syncPathStore.add(path);

            return true;

        }


    }

    private static void removePath(String path){

        if(StringUtil.isBlank(path)){
            return ;
        }

        syncPathStore.remove(path);
    }

    public static void pull(MiConsumerMeta meta){
       String path = MiUtil.getServiceNameGroupVersionZkPath(meta.getInterfaceName(),meta.getGroup(),meta.getVersion());
        pull(path);
    }

    public static void pull(String path){

        if(StringUtil.isBlank(path)){
            return ;
        }



        MiLogger.record(StringUtil.format("************* InsistPullProvider.pull start ! path:%s ****************",path));
        if(!addPath(path)){
            MiLogger.record(StringUtil.format("************* InsistPullProvider.pull repeat so stop this pull ! path:%s ****************",path));
            return ;
        }
        try {
            List<String> list = MiZkClient.getInstance().getNodeChildren(path);

            String[] pathArr = path.split(MiConstants.INSIST_ZK_SLASH);

            String group =pathArr[pathArr.length-3];
            String serviceName = pathArr[pathArr.length-2];
            String version = pathArr[pathArr.length-1];


            MiUtil.createAllProviderPathNode(serviceName,group,version);

            List<ServiceMeta> serviceMetas=new ArrayList<ServiceMeta>();

            if(list==null || list.size()==0){
                MiLogger.record(StringUtil.format("************* InsistPullProvider.pull error not find node ! path:%s ****************",path));
                return ;
            }else {

                for (String ipAndPort : list) {
                    String json = MiZkClient.getInstance().getDataForStr(path + MiConstants.INSIST_ZK_SLASH + ipAndPort, -1);
                    if (StringUtil.isBlank(json)) {
                        MiLogger.record(StringUtil.format("InsistPullProvider.pull path:%s json is null", path + MiConstants.INSIST_ZK_SLASH + ipAndPort));
                        continue;
                    }
                    ServiceMeta serviceMeta = MiUtil.jsonToServiceMeta(json);
                    serviceMetas.add(serviceMeta);
                }
            }

            InsistServiceList insistServiceList =new InsistServiceList(serviceMetas, MiUtil.serviceGroupVersionCreateKey(
                    serviceName,
                    group,
                    version));

            MiServiceStore.add(insistServiceList);

            MiLogger.record(StringUtil.format("************* InsistPullProvider.pull success ! path:%s ****************",path));

        }catch (Throwable e){
            MiLogger.record(StringUtil.format("InsistPullProvider.pull error ! path:%s errorCode:%s",path,e.getMessage()),e);
        }finally {
            try {
                removePath(path);
                MiLogger.record(StringUtil.format("************* InsistPullProvider.pull removePath sucucess ! path:%s ****************",path));
                //addChildWatcher
                MiLogger.record(StringUtil.format("************* InsistPullProvider.pull addChildWatcher start ! path:%s ****************",path));
                MiZkClient.getInstance().addChildWatcher(path, ZkChildrenWatcher.getInstance());
                MiLogger.record(StringUtil.format("************* InsistPullProvider.pull addChildWatcher success ! path:%s ****************",path));
            }catch (Throwable e1){
                MiLogger.record("InsistPullProvider.pull path:"+path+" errorCode:"+e1.getMessage(),e1);
            }
        }


    }

    public static void  pullAll(){

        for(MiConsumerMeta meta : MiConsumerStore.getAll() ){
            pull(meta);
        }

    }
}

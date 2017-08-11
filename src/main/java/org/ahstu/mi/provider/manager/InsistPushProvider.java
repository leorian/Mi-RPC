package org.ahstu.mi.provider.manager;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiConstants;
import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.InsistUtil;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.provider.InsistProviderMeta;
import org.ahstu.mi.provider.InsistProviderStore;
import org.ahstu.mi.provider.factory.ProviderFactory;
import org.ahstu.mi.zk.InsistZkClient;
import org.ahstu.mi.zk.api.IZkClient;

/**
 * Created by renyueliang on 17/5/22.
 */
public class InsistPushProvider {

    public static void push(ServiceMeta serviceMeta){

        MiLogger.record(StringUtil.format("InsistPushProvider.push start ! json:"+ JSON.toJSONString(serviceMeta)));

        IZkClient zkClient =  InsistZkClient.getInstance();

        //--/insist/consumer/forservice/group/com.xxx.service/version/ip
        //--/insist/prodiver/forservice/group/com.xxx.service/version/ip

        String groupPath = InsistUtil.getProviderZkPath()+ MiConstants.INSIST_ZK_SLASH+serviceMeta.getGroup();
        String serviceGroupPath =groupPath+ MiConstants.INSIST_ZK_SLASH+serviceMeta.getInterfaceName();
        String versionServiceGroupPath = serviceGroupPath+ MiConstants.INSIST_ZK_SLASH+serviceMeta.getVersion();
        String versionServiceGroupPathAndIpPort=versionServiceGroupPath+ MiConstants.INSIST_ZK_SLASH
                +serviceMeta.getIp()
                + MiConstants.LOWER_HORIZONTAL_LINE
                +serviceMeta.getPort();

        try {

            if (!zkClient.has(groupPath)) {
                zkClient.addNode(groupPath, false);
            }
            if (!zkClient.has(serviceGroupPath)) {
                zkClient.addNode(serviceGroupPath, false);
            }
            if (!zkClient.has(versionServiceGroupPath)) {
                zkClient.addNode(versionServiceGroupPath, false);
            }
            if(!zkClient.has(versionServiceGroupPathAndIpPort)){
                zkClient.addNode(versionServiceGroupPathAndIpPort,true);
            }

            zkClient.setDataForStr(versionServiceGroupPathAndIpPort,InsistUtil.serviceMetaToJson(serviceMeta),-1);

            MiLogger.record(StringUtil.format("InsistSpringProviderBean.push success ! path:%s json:%s",versionServiceGroupPathAndIpPort,JSON.toJSONString(serviceMeta)));


        }catch (Throwable e){

            MiLogger.record(StringUtil.format("InsistSpringProviderBean.push error ! versionServiceGroupPath:%s %s errorCode:%s"
                    ,versionServiceGroupPath,
                    MiError.SERVICE_META_REGISTER_EXCEPTION.getErrorCode(),
                    e.getMessage()
            ),e);

        }

    }

    public static void pushAll(){

        for(InsistProviderMeta providerMeta : InsistProviderStore.getAll()){
            push(ProviderFactory.insistProviderMetaToServiceMeta(providerMeta));
        }

    }

}

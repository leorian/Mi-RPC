package org.ahstu.mi.consumer.manager;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiConstants;
import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.consumer.MiConsumerStore;
import org.ahstu.mi.zk.InsistZkClient;
import org.ahstu.mi.zk.api.IZkClient;

/**
 * Created by renyueliang on 17/5/22.
 */
public class InsistPushConsumer {

    public static void push(MiConsumerMeta clientMeta){


        MiLogger.record(StringUtil.format("InsistPushConsumer.push start ! json:"+ JSON.toJSONString(clientMeta)));

        IZkClient zkClient =  InsistZkClient.getInstance();

        //--/insist/consumer/forservice/group/com.xxx.service/version/ip
        //--/insist/prodiver/forservice/group/com.xxx.service/version/ip

        String groupPath = MiUtil.getConsumerZkPath()+ MiConstants.INSIST_ZK_SLASH+clientMeta.getGroup();
        String serviceGroupPath =groupPath+ MiConstants.INSIST_ZK_SLASH+clientMeta.getInterfaceName();
        String versionServiceGroupPath = serviceGroupPath+ MiConstants.INSIST_ZK_SLASH+clientMeta.getVersion();
        String versionServiceGroupPathAndIpPort=versionServiceGroupPath+ MiConstants.INSIST_ZK_SLASH
                +clientMeta.getIp()
                + MiConstants.LOWER_HORIZONTAL_LINE
                +clientMeta.getPort();
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

            zkClient.setDataForStr(versionServiceGroupPathAndIpPort,MiUtil.clientMetaToJson(clientMeta),-1);

            MiLogger.record(StringUtil.format("InsistPushConsumer.push success ! json:"+ MiUtil.clientMetaToJson(clientMeta)));


        }catch (Throwable e){

            MiLogger.record(StringUtil.format("InsistPushConsumer.push error ! json:%s %s errorCode:%s"
                    ,MiUtil.clientMetaToJson(clientMeta),
                    MiError.CLIENT_META_REGISTER_EXCEPTION.getErrorCode(),
                    e.getMessage()
            ),e);

        }

    }

    public static void pushAll() {
        for (MiConsumerMeta meta : MiConsumerStore.getAll()) {
            push(meta);
        }
    }


}

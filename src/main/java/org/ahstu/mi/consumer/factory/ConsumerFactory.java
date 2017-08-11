package org.ahstu.mi.consumer.factory;

import org.ahstu.mi.common.MiException;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.consumer.MiSpringConsumerBean;
import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/15.
 */
public class ConsumerFactory {

    public static MiConsumerMeta springConsumerToConsumerMeta(MiSpringConsumerBean bean){

        MiConsumerMeta miConsumerMeta  = new MiConsumerMeta();

        if(bean!=null){

            miConsumerMeta.setId(bean.getId());
            miConsumerMeta.setGroup(bean.getGroup());
            miConsumerMeta.setVersion(bean.getVersion());
            miConsumerMeta.setInterfaceName(bean.getInterfaceName());
            try {
                miConsumerMeta.setInterfaceClass(Class.forName(bean.getInterfaceName()));
//              暂时不用
//                java.lang.reflect.Method[] methods = miConsumerMeta.getInterfaceClass().getMethods();
//                for(Method method : methods){
//                    miConsumerMeta.getClassMethodsName().add(method.getName());
//                }
            }catch (Throwable e){
                throw new MiException(e.getMessage(),e);
            }

            miConsumerMeta.setClientTimeout(bean.getClientTimeout());
            miConsumerMeta.setConnectionNum(bean.getConnectionNum());
            //this is clientip and clientport
            miConsumerMeta.setIp(NettyServer.getInstance().getServerIp());
            miConsumerMeta.setPort(NettyServer.getInstance().getServerPort());

            miConsumerMeta.verification();

        }


        return miConsumerMeta;
    }


}

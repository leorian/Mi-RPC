package org.ahstu.mi.consumer.factory;

import org.ahstu.mi.common.MiException;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.consumer.InsistSpringConsumerBean;
import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/15.
 */
public class ConsumerFactory {

    public static MiConsumerMeta springConsumerToConsumerMeta(InsistSpringConsumerBean bean){

        MiConsumerMeta insistConsumerMeta  = new MiConsumerMeta();

        if(bean!=null){

            insistConsumerMeta.setId(bean.getId());
            insistConsumerMeta.setGroup(bean.getGroup());
            insistConsumerMeta.setVersion(bean.getVersion());
            insistConsumerMeta.setInterfaceName(bean.getInterfaceName());
            try {
                insistConsumerMeta.setInterfaceClass(Class.forName(bean.getInterfaceName()));
//              暂时不用
//                java.lang.reflect.Method[] methods = insistConsumerMeta.getInterfaceClass().getMethods();
//                for(Method method : methods){
//                    insistConsumerMeta.getClassMethodsName().add(method.getName());
//                }
            }catch (Throwable e){
                throw new MiException(e.getMessage(),e);
            }

            insistConsumerMeta.setClientTimeout(bean.getClientTimeout());
            insistConsumerMeta.setConnectionNum(bean.getConnectionNum());
            //this is clientip and clientport
            insistConsumerMeta.setIp(NettyServer.getInstance().getServerIp());
            insistConsumerMeta.setPort(NettyServer.getInstance().getServerPort());

            insistConsumerMeta.verification();

        }


        return insistConsumerMeta;
    }


}

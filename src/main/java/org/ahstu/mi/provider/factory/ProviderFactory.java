package org.ahstu.mi.provider.factory;

import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.provider.MiProviderMeta;
import org.ahstu.mi.provider.MiSpringProviderBean;
import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/18.
 */
public class ProviderFactory {

    public static MiProviderMeta springProviderToProviderMeta(MiSpringProviderBean miSpringProviderBean){

        MiProviderMeta miProviderMeta = new MiProviderMeta();

        miProviderMeta.setId(miSpringProviderBean.getId());
        miProviderMeta.setInterfaceName(miSpringProviderBean.getInterfaceName());
        miProviderMeta.setVersion(miSpringProviderBean.getVersion());
        miProviderMeta.setGroup(miSpringProviderBean.getGroup());
        miProviderMeta.setRef(miSpringProviderBean.getTarget());
        miProviderMeta.setServiceDesc(miSpringProviderBean.getServiceDesc());
        miProviderMeta.setClientTimeout(miSpringProviderBean.getClientTimeout());


        miProviderMeta.verification();

        return miProviderMeta;


    }

    public static ServiceMeta miProviderMetaToServiceMeta(MiProviderMeta miProviderMeta){
        ServiceMeta serviceMeta =new ServiceMeta();
        serviceMeta.setPort(NettyServer.getInstance().getServerPort());
        serviceMeta.setIp(NettyServer.getInstance().getServerIp());

        serviceMeta.setGroup(miProviderMeta.getGroup());
        serviceMeta.setId(miProviderMeta.getId());
        serviceMeta.setServiceDesc(miProviderMeta.getServiceDesc());
        serviceMeta.setInterfaceName(miProviderMeta.getInterfaceName());
        serviceMeta.setVersion(miProviderMeta.getVersion());
        if(miProviderMeta.getClientTimeout()==0){
            serviceMeta.setClientTimeout(3000L);
        }else {
            serviceMeta.setClientTimeout(miProviderMeta.getClientTimeout());
        }

        return serviceMeta;
    }

    public static ServiceMeta springProviderToServiceMeta(MiSpringProviderBean miSpringProviderBean){
        ServiceMeta serviceMeta =new ServiceMeta();

        serviceMeta.setPort(NettyServer.getInstance().getServerPort());
        serviceMeta.setIp(NettyServer.getInstance().getServerIp());
        serviceMeta.setGroup(miSpringProviderBean.getGroup());
        serviceMeta.setId(miSpringProviderBean.getId());
        serviceMeta.setServiceDesc(miSpringProviderBean.getServiceDesc());
        serviceMeta.setInterfaceName(miSpringProviderBean.getInterfaceName());
        serviceMeta.setVersion(miSpringProviderBean.getVersion());
        if(miSpringProviderBean.getClientTimeout()==0){
            serviceMeta.setClientTimeout(3000L);
        }else {
            serviceMeta.setClientTimeout(miSpringProviderBean.getClientTimeout());
        }

        return serviceMeta;

    }

}

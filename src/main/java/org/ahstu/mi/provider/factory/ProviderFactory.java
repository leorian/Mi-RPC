package org.ahstu.mi.provider.factory;

import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.provider.MiProviderMeta;
import org.ahstu.mi.provider.MiSpringProviderBean;
import org.ahstu.mi.rpc.netty.server.NettyServer;

/**
 * Created by renyueliang on 17/5/18.
 */
public class ProviderFactory {

    public static MiProviderMeta springProviderToProviderMeta(MiSpringProviderBean insistSpringProviderBean){

        MiProviderMeta insistProviderMeta = new MiProviderMeta();

        insistProviderMeta.setId(insistSpringProviderBean.getId());
        insistProviderMeta.setInterfaceName(insistSpringProviderBean.getInterfaceName());
        insistProviderMeta.setVersion(insistSpringProviderBean.getVersion());
        insistProviderMeta.setGroup(insistSpringProviderBean.getGroup());
        insistProviderMeta.setRef(insistSpringProviderBean.getTarget());
        insistProviderMeta.setServiceDesc(insistSpringProviderBean.getServiceDesc());
        insistProviderMeta.setClientTimeout(insistSpringProviderBean.getClientTimeout());


        insistProviderMeta.verification();

        return insistProviderMeta;


    }

    public static ServiceMeta insistProviderMetaToServiceMeta(MiProviderMeta insistProviderMeta){
        ServiceMeta serviceMeta =new ServiceMeta();
        serviceMeta.setPort(NettyServer.getInstance().getServerPort());
        serviceMeta.setIp(NettyServer.getInstance().getServerIp());

        serviceMeta.setGroup(insistProviderMeta.getGroup());
        serviceMeta.setId(insistProviderMeta.getId());
        serviceMeta.setServiceDesc(insistProviderMeta.getServiceDesc());
        serviceMeta.setInterfaceName(insistProviderMeta.getInterfaceName());
        serviceMeta.setVersion(insistProviderMeta.getVersion());
        if(insistProviderMeta.getClientTimeout()==0){
            serviceMeta.setClientTimeout(3000L);
        }else {
            serviceMeta.setClientTimeout(insistProviderMeta.getClientTimeout());
        }

        return serviceMeta;
    }

    public static ServiceMeta springProviderToServiceMeta(MiSpringProviderBean insistSpringProviderBean){
        ServiceMeta serviceMeta =new ServiceMeta();

        serviceMeta.setPort(NettyServer.getInstance().getServerPort());
        serviceMeta.setIp(NettyServer.getInstance().getServerIp());
        serviceMeta.setGroup(insistSpringProviderBean.getGroup());
        serviceMeta.setId(insistSpringProviderBean.getId());
        serviceMeta.setServiceDesc(insistSpringProviderBean.getServiceDesc());
        serviceMeta.setInterfaceName(insistSpringProviderBean.getInterfaceName());
        serviceMeta.setVersion(insistSpringProviderBean.getVersion());
        if(insistSpringProviderBean.getClientTimeout()==0){
            serviceMeta.setClientTimeout(3000L);
        }else {
            serviceMeta.setClientTimeout(insistSpringProviderBean.getClientTimeout());
        }

        return serviceMeta;

    }

}

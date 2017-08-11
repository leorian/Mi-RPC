package org.ahstu.mi.provider;

import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.provider.factory.ProviderFactory;
import org.ahstu.mi.provider.manager.MiPushProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by renyueliang on 17/5/18.
 */
public class InsistSpringProviderBean implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {
    private String id;
    private String version;
    private String group;
    private Object target;
    private String serviceDesc;
    private long clientTimeout;
    private String interfaceName;
    private ServiceMeta serviceMeta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public long getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void afterPropertiesSet() throws Exception {
        MiUtil.insistStartUp();

        InsistProviderMeta insistProviderMeta = ProviderFactory.springProviderToProviderMeta(this);
        InsistProviderStore.add(insistProviderMeta);

        this.serviceMeta = ProviderFactory.springProviderToServiceMeta(this);
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        //call zk serviceMetas
        MiPushProvider.push(this.serviceMeta);
    }
}

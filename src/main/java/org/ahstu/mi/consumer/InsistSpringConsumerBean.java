package org.ahstu.mi.consumer;

import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.consumer.factory.ConsumerFactory;
import org.ahstu.mi.consumer.manager.InsistPullProvider;
import org.ahstu.mi.consumer.manager.InsistPushConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * Created by renyueliang on 17/5/18.
 */
public class InsistSpringConsumerBean implements FactoryBean,ApplicationContextAware, InitializingBean{


    private String id;
    private String version;
    private String group;
    private long clientTimeout;
    private int connectionNum;
    private String interfaceName;
    private Class serviceClass;
    private Object proxy;
    private MiConsumerMeta meta;

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

    public long getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public int getConnectionNum() {
        return connectionNum;
    }

    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object getObject() throws Exception {
        return this.proxy;
    }

    public void init(){

        this.meta = ConsumerFactory.springConsumerToConsumerMeta(this);
        InsistConsumerStore.add(this.meta);
        this.serviceClass = this.meta.getInterfaceClass();

        this.proxy = Proxy.newProxyInstance(this.serviceClass.getClassLoader(), new Class[]{this.serviceClass},
                new InsistConsumerHandler(this.meta));
    }

    public Class<?> getObjectType() {
        return this.serviceClass;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        MiUtil.insistStartUp();
        init();
        InsistPullProvider.pull(this.meta);
        InsistPushConsumer.push(this.meta);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}

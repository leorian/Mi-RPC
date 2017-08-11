package org.ahstu.mi.schema;

import org.ahstu.mi.consumer.MiSpringConsumerBean;
import org.ahstu.mi.provider.MiSpringProviderBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by sky on 2017/5/16.
 */
public class MiNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("provider",new MiBeanDefinitionParser(MiSpringProviderBean.class));
        registerBeanDefinitionParser("consumer",new MiBeanDefinitionParser(MiSpringConsumerBean.class));

    }
}

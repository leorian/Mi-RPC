package org.ahstu.mi.schema;

import org.ahstu.mi.consumer.InsistSpringConsumerBean;
import org.ahstu.mi.provider.InsistSpringProviderBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by sky on 2017/5/16.
 */
public class InsistNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("provider",new InsistBeanDefinitionParser(InsistSpringProviderBean.class));
        registerBeanDefinitionParser("consumer",new InsistBeanDefinitionParser(InsistSpringConsumerBean.class));

    }
}

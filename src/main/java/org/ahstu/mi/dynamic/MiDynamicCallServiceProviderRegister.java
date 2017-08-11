package org.ahstu.mi.dynamic;

import org.ahstu.mi.provider.MiSpringProviderBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.concurrent.locks.ReentrantLock;

public class MiDynamicCallServiceProviderRegister implements BeanFactoryPostProcessor {

    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private static volatile boolean MI_START_SUCCESS = false;
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        startDynamicPipeline(this.beanFactory);
    }

    public static void startDynamicPipeline(DefaultListableBeanFactory beanFactory) {
        if (MI_START_SUCCESS) {
            return;
        }

        try {
            reentrantLock.lock();
            if (MI_START_SUCCESS) {
                return;
            }
            BeanDefinitionBuilder insistDynamicCallServiceBuilder = BeanDefinitionBuilder.
                    rootBeanDefinition(MiDynamicCallServiceImpl.class.getName());
            beanFactory.registerBeanDefinition("insistDynamicCallService",
                    insistDynamicCallServiceBuilder.getBeanDefinition());
            BeanDefinitionBuilder InsistDynamicCallServiceProviderRegisterBuilder = BeanDefinitionBuilder.
                    rootBeanDefinition(MiSpringProviderBean.class.getName());
            InsistDynamicCallServiceProviderRegisterBuilder.addPropertyValue("group", MiDynamicCallConstants.GROUP);
            InsistDynamicCallServiceProviderRegisterBuilder.addPropertyValue("version", MiDynamicCallConstants.VERSION);
            InsistDynamicCallServiceProviderRegisterBuilder.addPropertyValue("interfaceName", MiDynamicCallService.class.getName());
            InsistDynamicCallServiceProviderRegisterBuilder.addPropertyValue("id", "insistDynamicCallServiceProviderRegister");
            InsistDynamicCallServiceProviderRegisterBuilder.addPropertyReference("target", "insistDynamicCallService");
            beanFactory.registerBeanDefinition("insistDynamicCallServiceProviderRegister",
                    InsistDynamicCallServiceProviderRegisterBuilder.getBeanDefinition());
            System.out.println("XXXXXXXXXX");
            MI_START_SUCCESS = true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if (beanFactory.getBean("insistDynamicCallServiceProviderRegister") == null) {
                startDynamicPipeline(beanFactory);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

}   
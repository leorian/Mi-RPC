package org.ahstu.mi.test.provider;

import org.ahstu.mi.test.service.TradeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.UUID;

/**
 * Created by renyueliang on 17/5/25.
 */
public class ProviderTwoTest {

    static ApplicationContext factory;

    static void init(){
        factory = new ClassPathXmlApplicationContext("spring-service-two.xml");
    }

    static <T> T getBean(String beanName){
        return (T)factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable{

        init();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = new Date().getTime();

                TradeService tradeService = getBean("tradeServiceClient");
                try {
                    String result = tradeService.findTrade("ProviderTreeTest" + UUID.randomUUID().toString(), null);
                    System.out.println("tradeService.findTrade result -> " + result);
                }catch (Throwable e1){
                    e1.printStackTrace();
                }

                long endTime = new Date().getTime();

                System.out.println("endTime-startTime : " + (endTime - startTime));

                try {
                    Thread.sleep(3000l);
                    startTime = new Date().getTime();

                    String result = tradeService.findTrade("ProviderTreeTest" + UUID.randomUUID().toString(), null);
                    System.out.println("tradeService.findTrade result -> " + result);

                    endTime = new Date().getTime();
                    System.out.println("endTime-startTime : " + (endTime - startTime));

                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();

        System.in.read();

    }
}

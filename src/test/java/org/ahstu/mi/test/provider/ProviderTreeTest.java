package org.ahstu.mi.test.provider;

import org.ahstu.mi.test.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by renyueliang on 17/5/25.
 */
public class ProviderTreeTest {

    static ApplicationContext factory;

    static void init() {
        factory = new ClassPathXmlApplicationContext("spring-service-three.xml");
    }

    static <T> T getBean(String beanName) {
        return (T) factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable {

        init();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = new Date().getTime();

                UserService userService = getBean("userServiceClient");
                try {
                    String result = userService.getUser(8888888);
                    System.out.println("tradeService.findTrade result -> " + result);
                }catch (Throwable e1){
                    e1.printStackTrace();
                }

                long endTime = new Date().getTime();

                System.out.println("endTime-startTime : " + (endTime - startTime));

                while(true) {
                    try {
                        Thread.sleep(3000l);
                        startTime = new Date().getTime();
                        String result = userService.getUser(8888888);
                        ;
                        System.out.println("client-tradeService.findTrade result -> " + result);

                        endTime = new Date().getTime();
                        System.out.println("client-endTime-startTime : " + (endTime - startTime));

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        thread.start();

        System.in.read();
    }
}

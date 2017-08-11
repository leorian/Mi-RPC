package org.ahstu.mi.test.consumer;

import com.alibaba.fastjson.JSON;
import org.ahstu.mi.test.module.CourseModule;
import org.ahstu.mi.test.service.CourseCenterService;
import org.ahstu.mi.test.service.TradeService;
import org.ahstu.mi.test.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by renyueliang on 17/5/25.
 */
public class ConsumerTwoTest {

    static ApplicationContext factory;

    static void init(){
        factory = new ClassPathXmlApplicationContext("spring-client-two.xml");
    }

    static <T> T getBean(String beanName){
        return (T)factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable{
        init();

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {

                long startTime = new Date().getTime();

                UserService userService = getBean("userServiceClient");
                TradeService tradeService = getBean("tradeServiceClient");
                CourseCenterService courseCenterService = getBean("courseCenterClient");

                try {

                    System.out.println(userService.getUser(101000l));
                    System.out.println(tradeService.findTrade(UUID.randomUUID().toString(), null));

                    CourseModule courseModule = courseCenterService.find(UUID.randomUUID().toString());

                    System.out.println("JSON:" + JSON.toJSONString(courseModule));

                    List<CourseModule> list = courseCenterService.query(courseModule);

                    System.out.println("JSON:" + JSON.toJSONString(list));


                }catch (Throwable e1){
                    e1.printStackTrace();
                }

                long endTime = new Date().getTime();
                System.out.println("endTime-startTime:" + (endTime - startTime));

                while(true){
                    try{

                        Thread.sleep(3000l);

                        startTime = new Date().getTime();


                        System.out.println(userService.getUser(101000l));
                        System.out.println(tradeService.findTrade(UUID.randomUUID().toString(), null));

                        CourseModule ncourseModule = courseCenterService.find(UUID.randomUUID().toString());

                        System.out.println("JSON:"+ JSON.toJSONString(ncourseModule));

                        List<CourseModule> nlist = courseCenterService.query(ncourseModule);

                        System.out.println("JSON:"+ JSON.toJSONString(nlist));

                        endTime = new Date().getTime();

                        System.out.println("endTime-startTime:"+(endTime-startTime));

                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }


            }
        });

        thread.start();

        System.in.read();

    }
}

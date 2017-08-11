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
 * Created by renyueliang on 17/7/14.
 */
public class MoreThreadConsumerTest {

    static boolean run=true;

    static ApplicationContext factory;

    static void init(){
        factory = new ClassPathXmlApplicationContext("spring-client-one.xml");
    }

    static <T> T getBean(String beanName){
        return (T)factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable{

        init();


        System.out.println(" MoreThreadConsumerTest start !");
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60 * 10 * 1000);
                    System.out.println("stop all threads !");
                    run=false;
                }catch (Throwable e){

                }
            }
        });

        thread1.start();

        for(int i=100;i>0;i--){
            Thread thread =new Thread(new ThreadTest());
            thread.start();
        }


        System.in.read();

    }
}


class ThreadTest implements Runnable{
    @Override
    public void run() {

            long startTime =0;
            long endTime = 0;

            UserService userService = MoreThreadConsumerTest.getBean("userServiceClient");
            TradeService tradeService = MoreThreadConsumerTest.getBean("tradeServiceClient");
            CourseCenterService courseCenterService = MoreThreadConsumerTest.getBean("courseCenterClient");

            int i=1;
            int success= 1;
            while(MoreThreadConsumerTest.run){

                try{

                    Thread.sleep(10l);

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
                    success++;
                }finally {
                    i++;
                }


        }


        System.out.println("i:"+i+"  success:"+success);
    }
}

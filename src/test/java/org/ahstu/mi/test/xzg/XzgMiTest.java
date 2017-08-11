package org.ahstu.mi.test.xzg;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by renyueliang on 17/5/23.
 */
public class XzgMiTest {

   static ApplicationContext factory;

   static void init(){
        factory = new ClassPathXmlApplicationContext("xzg-spring-service.xml");
   }

   static <T> T getBean(String beanName){
       return (T)factory.getBean(beanName);
   }

   public static void main(String[] args) throws Throwable{
       init();

       System.in.read();
   }

}

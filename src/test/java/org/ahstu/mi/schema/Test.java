package org.ahstu.mi.schema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by renyueliang on 17/5/19.
 */
public class Test {


    public static void main(String[] args){


        ApplicationContext factory = new ClassPathXmlApplicationContext("abc-service-spring.xml");

    }
}

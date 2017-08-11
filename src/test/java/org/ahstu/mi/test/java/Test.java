package org.ahstu.mi.test.java;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by xiezg@317hu.com on 2017/6/15 0015.
 */
public class Test {
    public static void main(String args[]) {
        Method[] methods = HelloService.class.getDeclaredMethods();
        for (Method method : methods) {
            Type[] types = method.getGenericParameterTypes();
            for (Type type : types) {
                System.out.println(type.toString());
            }
        }
//        System.out.println(String.class);
    }
    //java.util.Set<java.lang.String>
}

package org.ahstu.mi.test.xzg;

import org.ahstu.mi.test.service.TradeService;

import java.lang.reflect.Method;

/**
 * Created by xiezg@317hu.com on 2017/6/9 0009.
 */
public class JsParamterTest {
    public static void main(String args[]) {
        Class c = TradeService.class;
        for (Method method : c.getDeclaredMethods()) {
             System.out.println(method);
        }
    }
}

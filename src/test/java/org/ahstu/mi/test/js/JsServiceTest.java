package org.ahstu.mi.test.js;

import com.alibaba.fastjson.JSON;
import org.ahstu.mi.test.xzg.XzgServiceImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/6/8 0008.
 */
public class JsServiceTest {
    public static void main(String args[]) {
        for (Method method : JsService.class.getMethods()) {
            System.out.println(method.getName());
            for (Type type :method.getGenericParameterTypes()){
                System.out.print("" +type.toString() + " ");
            }
            System.out.println();
        }

        for (Method method : XzgServiceImpl.class.getDeclaredMethods()) {
            LocalVariableTableParameterNameDiscoverer   parameterNameDiscoverer=  new LocalVariableTableParameterNameDiscoverer();
            String[] strings = parameterNameDiscoverer.getParameterNames(method);
            System.out.println(JSON.toJSONString(strings));
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(int.class.toString());
        System.out.println(int[].class.toString());
        System.out.println(Integer.class.toString());
        System.out.println(Integer[].class.toString());
        System.out.println(Class.class.toString());
        System.out.println(List.class.toString());
        try {
            Class c = Class.forName("java.lang.String");
            System.out.println(c.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

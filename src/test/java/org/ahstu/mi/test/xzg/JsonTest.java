package org.ahstu.mi.test.xzg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/6/7 0007.
 */
public class JsonTest {
    public static void main(String args[]) throws Exception {
        List<Map> list= new ArrayList<Map>(0);
        System.out.println(list.getClass().getMethod("get", int.class).getReturnType());

        String ha = "hello";
        Object o = JSON.parse(JSON.toJSONString(ha));
        System.out.println(o instanceof String );

        double iii = 1.1;
        Object o1 = JSON.parse(JSON.toJSONString(iii));
        System.out.println(o1 instanceof Integer);

        String string = "[\n" +
                "    [\n" +
                "        {\n" +
                "            \"id\": 1000,\n" +
                "            \"name\": \"hello\",\n" +
                "            \"xzgSex\": \"MALE\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 1001,\n" +
                "            \"name\": \"world\",\n" +
                "            \"xzgSex\": \"FEMALE\"\n" +
                "        }\n" +
                "    ]\n" +
                "]";
        JSONArray jsonArray = JSON.parseArray(string);
        System.out.println(jsonArray.get(0) instanceof JSONArray);//集合测试
        List<XzgUser> xzgUserList = JSON.parseArray(jsonArray.get(0).toString(), XzgUser.class);
        String s1 = "hello";
        String s2 = "world";
        Object[] object = {s1, s2};
        String s12 = JSON.toJSONString(object);
        System.out.println(s12);
        JSONArray jsonArray1 = JSON.parseArray(s12);
        System.out.println(jsonArray1.get(0) instanceof String);//字符串测试

        int i1 = 1;
        int i2 = 2;
        Object[] Object2 = {i1, i2};
        String i12 = JSON.toJSONString(Object2);
        System.out.println(i12);
        JSONArray jsonArray2 = JSON.parseArray(i12);
        System.out.println(jsonArray2.get(0) instanceof  Integer);//整型测试

        System.out.println(xzgUserList.getClass());
        Method[] methodArray = XzgService.class.getDeclaredMethods();
        for (Method method : methodArray) {

            System.out.println(method.getName() + " = " + JSON.toJSONString(method.getParameterTypes()) + " = " + method.getGenericParameterTypes());
            for (Type type : method.getGenericParameterTypes()) {
                System.out.println(type.toString());
            }
        }
        System.in.read();
    }
}

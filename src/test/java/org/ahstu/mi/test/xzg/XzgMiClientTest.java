package org.ahstu.mi.test.xzg;

import com.alibaba.fastjson.JSON;
import org.ahstu.mi.common.MiConstants;
import org.ahstu.mi.zk.MiZkClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by renyueliang on 17/5/23.
 */
public class XzgMiClientTest {

    static ApplicationContext factory;

    static void init() {
        factory = new ClassPathXmlApplicationContext("xzg-spring-client.xml");
    }

    static <T> T getBean(String beanName) {
        return (T) factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable {
        init();

        //list对象元素测试
        XzgService xzgService = getBean("xzgService");
        XzgUser xzgUser = new XzgUser();
        xzgUser.setId(1000);
        xzgUser.setName("hello");
        xzgUser.setXzgSex(XzgSex.MALE);
        XzgUser xzgUser1 = new XzgUser();
        xzgUser1.setId(1001);
        xzgUser1.setName("world");
        xzgUser1.setXzgSex(XzgSex.FEMALE);
        List<XzgUser> xzgUserList = new ArrayList<XzgUser>();
        xzgUserList.add(xzgUser);
        xzgUserList.add(xzgUser1);
//        xzgService.listArgumentMethod(xzgUserList);

        //Class对象测试

       // xzgService.classArgumentMethod(XzgUser.class);

        //多个参数测试
      //  xzgService.listAndClassArgumentMethod(xzgUserList, XzgUser.class);

        //map对象测试
        Map<String, XzgUser> map = new HashMap<String, XzgUser>();
        map.put("hello", xzgUser);
        map.put("world", xzgUser1);
        Map<String, XzgUser> hello  = JSON.parseObject(JSON.toJSONString(map), Map.class);
      //  xzgService.mapArgumentMethod(map);
//        String name = hello.get(0).getName();

        Map<XzgUser, XzgUser> map1 = new HashMap<XzgUser, XzgUser>();
        map1.put(xzgUser,xzgUser);
        map1.put(xzgUser1, xzgUser1);
        Map<XzgUser, XzgUser> hello1 = JSON.parseObject(JSON.toJSONString(map1), Map.class);

        System.out.println(MiZkClient.getInstance().getTreeForList(MiConstants.MI_ROOT_PATH));

        System.in.read();


    }

}

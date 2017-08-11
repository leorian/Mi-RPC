package org.ahstu.mi.test.xzg;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiezhonggui on 2017/6/7.
 */
public class JSTest {
    public static void main(String args[]) {
        XzgUser xzgUser = new XzgUser();
        xzgUser.setId(1);
        xzgUser.setName("xzgui01");
        xzgUser.setXzgSex(XzgSex.FEMALE);

        XzgUser xzgUser1 = new XzgUser();
        xzgUser1.setId(2);
        xzgUser1.setName("xzgui02");
        xzgUser1.setXzgSex(XzgSex.MALE);
        List<XzgUser> xzgUserList = new ArrayList<XzgUser>();
        xzgUserList.add(xzgUser);
        xzgUserList.add(xzgUser1);
        System.out.println(JSON.toJSONString(xzgUserList));

        Map<XzgUser, XzgUser> xzgUserXzgUserMap = new HashMap<XzgUser, XzgUser>();
        xzgUserXzgUserMap.put(xzgUser,xzgUser);
        xzgUserXzgUserMap.put(xzgUser1,xzgUser1);
        System.out.println(JSON.toJSON(xzgUserXzgUserMap).toString());

        Map<XzgUser, XzgUser> xzgUserXzgUserMap1 =
                JSON.parseObject(JSON.toJSON(xzgUserXzgUserMap).toString(), Map.class);

        String hello = "{\"id\":1,\"name\":\"xzgui01\",\"xzgSex\":\"FEMALE\"}";

        XzgUser xzgUser2 = JSON.parseObject(hello, XzgUser.class);
        System.out.println(xzgUser2.getName());
        System.out.println("over!!!");
    }
}

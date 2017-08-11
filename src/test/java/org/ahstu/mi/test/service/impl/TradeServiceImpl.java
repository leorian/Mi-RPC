package org.ahstu.mi.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.ahstu.mi.dynamic.XzgUser;
import org.ahstu.mi.test.service.TradeService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by renyueliang on 17/5/24.
 */
public class TradeServiceImpl implements TradeService {

    @Override
    public String classMethod2(Class cls1, Class cls2) {
        System.out.println("cls1: " + cls1 + ",cls2" + cls2);
        return "fdafdasfs";
    }

    @Override
    public String classMethod(Class cls) {
        System.out.println(cls);
        return "fdasfdasfdasf";
    }

    @Override
    public String intMethodTest(int i) {
        System.out.println("接收的值是：" + i);
        return "HelloWorld";
    }

    @Override
    public String findTrade(String tradeId, List<XzgUser> xzgUserList) {

        System.out.println("TradeServiceImpl.findTrade tradeId:" + tradeId);

        if (!CollectionUtils.isEmpty(xzgUserList)) {
            System.out.println(JSON.toJSONString(xzgUserList));
            System.out.println(xzgUserList.get(0).getName());
        }

        return "Hello renyl ! tradeId:" + tradeId;
    }

    @Override
    public String findListString(List<String> list) {
        System.out.println("list集合是：" + JSON.toJSONString(list));
        return "Hello ";
    }

    @Override
    public String findXzgUser(XzgUser xzgUser, String hi) {
        System.out.println(JSON.toJSONString(xzgUser) + " " + hi);
        return "hahaxxx";
    }

    @Override
    public String findXzgUserArray(XzgUser[] xzgUsers) {
        System.out.println(JSON.toJSONString(xzgUsers) + "findXzgUserArray");
        return "xxxxxxdddd";
    }

    @Override
    public String findSimpleMap(Map<String, String> map) {
        System.out.println(JSON.toJSONString(map) + "findSimpleMap");
        return "dafdasdf";
    }

    @Override
    public String findSimpleIntegerMap(Map<Integer, Integer> map) {
        System.out.println(JSON.toJSONString(map) + "findSimpleIntegerMap");
        return "dafdasgagageadgsa";
    }

    @Override
    public String findObjectMap(Map<String, XzgUser> map) {
        System.out.println(JSON.toJSONString(map) + "fdafdasdgagdas");
        return "xfdafdasgdasd";
    }

    @Override
    public String findComplexMap(Map<XzgUser, XzgUser> map) {
        try {
            System.out.println(JSON.toJSON(map).toString() + "findComplexMap");
        } catch (Throwable e) {
        } finally {
        }
        return "gdasgdasdgasdg";
    }

    @Override
    public String nullParameter() {
        System.out.println("nullParameter");
        return "nullParameter";
    }

    @Override
    public String simpleSet(Set<String> stringSet) {
        System.out.println("simpleSet:" + JSONObject.toJSONString(stringSet));
        return "dfafdasf";
    }

    @Override
    public String complexSet(Set<XzgUser> xzgUsers) {
        System.out.println("complexSet: " + JSON.toJSONString(xzgUsers));
        return "dafdasfda";
    }

    @Override
    public String intArray(int[] iArray) {
        System.out.println(iArray);
        return "fafdagdasgdasdg";
    }

    @Override
    public String singleObj(XzgUser xzgUser) {
        System.out.println(JSON.toJSONString(xzgUser));
        return "dfafdasfdasf";
    }
}

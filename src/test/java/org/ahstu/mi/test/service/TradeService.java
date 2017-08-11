package org.ahstu.mi.test.service;


import org.ahstu.mi.dynamic.XzgUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by renyueliang on 17/5/24.
 */
public interface TradeService {

    String classMethod2(Class cls1, Class cls2);

    String classMethod(Class cls);

    String intMethodTest(int i);

    String findTrade(String tradeId, List<XzgUser> xzgUserList);

    String findListString(List<String> list);

    String findXzgUser(XzgUser xzgUser, String hi);

    String findXzgUserArray(XzgUser[] xzgUsers);

    String findSimpleMap(Map<String, String> map);

    String findSimpleIntegerMap(Map<Integer, Integer> map);

    String findObjectMap(Map<String, XzgUser> map);

    String findComplexMap(Map<XzgUser, XzgUser> map);

    String nullParameter();

    String simpleSet(Set<String> stringSet);

    String complexSet(Set<XzgUser> xzgUsers);

    String intArray(int[] iArray);

    String singleObj(XzgUser xzgUser);
}

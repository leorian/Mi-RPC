package org.ahstu.mi.test.xzg;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/5/25 0025.
 */
public class XzgServiceImpl implements XzgService {
    @Override
    public void listArgumentMethod(List<XzgUser> xzgUserList) {
        System.out.println(JSON.toJSONString(xzgUserList));
        System.out.println("-----------------------------------");
    }

    @Override
    public void classArgumentMethod(Class classObj) {
        System.out.println(classObj.getName());
        System.out.println("-----------------------------------");
    }

    @Override
    public void listAndClassArgumentMethod(List<XzgUser> xzgUserList, Class classObj) {
        System.out.println(JSON.toJSONString(xzgUserList));
        System.out.println(classObj.getName());
        System.out.println("-----------------------------------");
    }

    @Override
    public void mapArgumentMethod(Map<String, XzgUser> map) {
        System.out.println(JSON.toJSONString(map));
        System.out.println("-----------------------------------");
    }

    @Override
    public void XzgUserArgumentMethod(XzgUser xzgUser) {

    }

    @Override
    public void nullMethod() {

    }

    @Override
    public void StringMethod(XzgUser[] xzgUsers, String string, Integer integer,
                             int i, long l, String[] strings, int[] is, long[] longs) {

    }
}

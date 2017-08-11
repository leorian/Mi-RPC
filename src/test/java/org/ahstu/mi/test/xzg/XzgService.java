package org.ahstu.mi.test.xzg;

import java.util.List;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/5/25 0025.
 */
public interface XzgService {

    void listArgumentMethod(List<XzgUser> xzgUserList);

    void classArgumentMethod(Class classObj);

    void listAndClassArgumentMethod(List<XzgUser> xzgUserList, Class classObj);

    void mapArgumentMethod(Map<String, XzgUser> map);

    void XzgUserArgumentMethod(XzgUser xzgUser);

    void nullMethod();

    void StringMethod(XzgUser[] xzgUsers,String string, Integer integer, int i, long l, String[] strings, int[] is, long[] longs);
}

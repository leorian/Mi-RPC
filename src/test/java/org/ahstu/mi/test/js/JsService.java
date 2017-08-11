package org.ahstu.mi.test.js;

import org.ahstu.mi.test.xzg.XzgUser;

import java.util.List;
import java.util.Map;

/**
 * Created by xiezg@317hu.com on 2017/6/8 0008.
 */
public interface JsService {
    void jsMethod(int[] ints, long[] longs, double[] doubles, float[] floats, char[] chars, XzgUser[] xzgUsers, Integer[] integers);

    void jsMethod1(List<Integer> integerList, List<XzgUser> xzgUserList, Map<String, XzgUser> map);

    void jsMethod2(int ints, long longs, double doubles, float floats, char chars, Integer integer, XzgUser xzgUser);

    void jsMethod3();
}

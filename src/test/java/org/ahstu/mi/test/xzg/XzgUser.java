package org.ahstu.mi.test.xzg;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by xiezg@317hu.com on 2017/5/25 0025.
 */
public class XzgUser implements Serializable {
    private int id;
    private String name;
    private XzgSex xzgSex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XzgSex getXzgSex() {
        return xzgSex;
    }

    public void setXzgSex(XzgSex xzgSex) {
        this.xzgSex = xzgSex;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

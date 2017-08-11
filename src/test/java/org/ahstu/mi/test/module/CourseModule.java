package org.ahstu.mi.test.module;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by renyueliang on 17/5/25.
 */
public class CourseModule implements Serializable {

    private static final long serialVersionUID = -8361343597974956624L;

    private String id;
    private int price;
    private String desc;
    private String name;
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

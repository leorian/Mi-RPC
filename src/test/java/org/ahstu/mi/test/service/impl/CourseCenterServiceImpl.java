package org.ahstu.mi.test.service.impl;

import com.alibaba.fastjson.JSON;
import org.ahstu.mi.test.module.CourseModule;
import org.ahstu.mi.test.service.CourseCenterService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by renyueliang on 17/5/25.
 */
public class CourseCenterServiceImpl implements CourseCenterService {


    @Override
    public List<CourseModule> query(CourseModule courseModule) {

        System.out.println("CourseCenterServiceImpl.query param->"+JSON.toJSONString(courseModule));

        List<CourseModule> list =new ArrayList<CourseModule>();
        for(int i=0;i<10;i++){
            CourseModule courseModule1=new CourseModule();
            courseModule1.setDate(new Date());
            courseModule1.setDesc("lll kkk");
            courseModule1.setId(UUID.randomUUID().toString());
            courseModule1.setName("renyl");
            int price =(int)Math.random()*100;
            courseModule1.setPrice(price);


            list.add(courseModule1);
        }

        return list;
    }

    @Override
    public CourseModule find(String cmid) {

        System.out.println("CourseCenterServiceImpl.query cmid->"+cmid);

        CourseModule courseModule1=new CourseModule();
        courseModule1.setDate(new Date());
        courseModule1.setDesc("lll kkk");
        courseModule1.setId(UUID.randomUUID().toString());
        courseModule1.setName("renyl");
        int price =(int)Math.random()*100;
        courseModule1.setPrice(price);

        return courseModule1;
    }
}

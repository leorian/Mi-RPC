package org.ahstu.mi.test.service;

import org.ahstu.mi.test.module.CourseModule;

import java.util.List;

/**
 * Created by renyueliang on 17/5/25.
 */
public interface CourseCenterService {


    public List<CourseModule> query(CourseModule courseModule);


    public CourseModule find(String cmid);
}

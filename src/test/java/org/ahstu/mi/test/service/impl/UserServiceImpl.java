package org.ahstu.mi.test.service.impl;

import org.ahstu.mi.test.service.UserService;

/**
 * Created by renyueliang on 17/5/23.
 */
public class UserServiceImpl implements UserService {

    @Override
    public String getUser(long userId) {

        System.out.println("UserServiceImpl.getUser userId:"+userId);

        return "Ren yue liang , you are true genius ! userId:"+userId;
    }
}

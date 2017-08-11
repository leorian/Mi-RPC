package org.ahstu.mi.test.client.impl;

import org.ahstu.mi.test.client.DataCenterService;
import org.ahstu.mi.test.service.UserService;

/**
 * Created by renyueliang on 17/5/23.
 */
public class DataCenterServiceImpl implements DataCenterService {


   private UserService userServiceClient ;

    @Override
    public String find(String id) {

        System.out.println("DataCenterServiceImpl.find -- > id:"+id);

        return userServiceClient.getUser(10L);
    }

    public void setUserServiceClient(UserService userServiceClient) {
        this.userServiceClient = userServiceClient;
    }
}

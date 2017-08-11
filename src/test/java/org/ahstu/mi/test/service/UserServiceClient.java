package org.ahstu.mi.test.service;

/**
 * Created by renyueliang on 17/7/13.
 */
public class UserServiceClient implements UserService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getUser(long userId) {
        return userService.getUser(userId);
    }
}

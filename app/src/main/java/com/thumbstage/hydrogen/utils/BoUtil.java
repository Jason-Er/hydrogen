package com.thumbstage.hydrogen.utils;

import com.thumbstage.hydrogen.model.vo.User;

import java.util.List;

public class BoUtil {

    static public User findById(List<User> users, String userId) {
        User userFind = null;
        for(User user: users) {
            if(user.getId().equals(userId)) {
                userFind = user;
                break;
            }
        }
        return userFind;
    }

}

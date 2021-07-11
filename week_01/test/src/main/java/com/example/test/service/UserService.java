package com.example.test.service;

import com.example.test.bean.UserBean;

import java.util.List;

public interface UserService {
    //下面的是实现增删改查的
    List<UserBean> findByAll();
    UserBean findUserById(Integer id);
    UserBean findUserByEmail(String email);
    int saveUser(UserBean user);
    int deleteUser(Integer id);
    int updateUser(UserBean user);

    int Register(UserBean user);
    UserBean loginInManager(String name,String password);
    UserBean loginIn(String name,String password);


}
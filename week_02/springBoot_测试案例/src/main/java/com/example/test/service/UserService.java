package com.example.test.service;

import com.example.test.bean.User;

import java.util.List;

public interface UserService {
   // UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    //下面的是实现增删改查的
    List<User> findByAll();
    User findUserById(Integer id);
    int saveUser(User user);
    int deleteUser(Integer id);
    int updateUser(User user);

    int Register(User user);
    User loginIn(String name, String password);

}
package com.example.test.serviceImpl;

import com.example.test.bean.UserBean;
import com.example.test.mapper.UserMapper;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //将DAO注入Service层
    @Autowired(required=false)
    private UserMapper userMapper;

    @Override
    public List<UserBean> findByAll() {
        return userMapper.findByAll();
    }

    @Override
    public UserBean findUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public UserBean findUserByEmail(String email){
        return userMapper.findUserByEmail(email);
    }

    @Override
    public int saveUser(UserBean user){
        return userMapper.saveUser(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public int updateUser(UserBean user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int Register(UserBean user) {
        return userMapper.Register(user);
    }

    @Override
    public UserBean loginInManager(String name, String password) {
        return userMapper.getInfoManager(name,password);
    }

    @Override
    public UserBean loginIn(String name, String password) {
        return userMapper.getInfo(name,password);
    }
}
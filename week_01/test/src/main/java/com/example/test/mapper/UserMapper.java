package com.example.test.mapper;

import com.example.test.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserBean> findByAll();
    UserBean findUserByEmail(String email);
    UserBean findUserById(Integer id);
    int saveUser(UserBean user);
    int deleteUser(Integer id);
    int updateUser(UserBean user);
    int Register(UserBean user);

    UserBean getInfoManager(String name,String password);
    UserBean getInfo(String name,String password);

}
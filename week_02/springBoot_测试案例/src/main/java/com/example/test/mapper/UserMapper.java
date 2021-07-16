package com.example.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> findByAll();
    User findUserById(Integer id);
    int saveUser(User user);
    int deleteUser(Integer id);
    int updateUser(User user);
    int Register(User user);
    User getInfo(String name, String password);

}
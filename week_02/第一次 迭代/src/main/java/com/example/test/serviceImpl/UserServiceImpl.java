package com.example.test.serviceImpl;

import com.example.test.bean.CommitBean;
import com.example.test.bean.UserBean;
import com.example.test.bean.YanBean;
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
    public YanBean findYan(){
        return userMapper.findYan();
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
    public int commit(CommitBean commit){
        return userMapper.commit(commit);
    }

    @Override
    public List<CommitBean> findCommit(String userId) {
        return userMapper.findCommit(userId);
    }

    @Override
    public List<CommitBean> findAllCommit(){
        return userMapper.findAllCommit();
    }

    @Override
    public UserBean loginInManager(String name, String password) {
        return userMapper.getInfoManager(name,password);
    }

    @Override
    public UserBean loginIn(String name, String password) {
       return userMapper.getInfo(name,password);
    }
    @Override
    public int updateCommit(Integer id) {
        return userMapper.updateCommit(id);
    }

    @Override
    public int updateCommitUnPass(Integer id) {
        return userMapper.updateCommitUnPass(id);
    }

    @Override
    public int updateCommitFile(Integer id, String filename) {
        return userMapper.updateCommitFile(id,filename);
    }
}
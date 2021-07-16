package com.example.test.service;

import com.example.test.bean.CommitBean;
import com.example.test.bean.UserBean;
import com.example.test.bean.YanBean;
import java.util.List;

public interface UserService {
    //下面的是实现增删改查的
    List<UserBean> findByAll();
    UserBean findUserById(Integer id);
    UserBean findUserByEmail(String email);
    int saveUser(UserBean user);
    int deleteUser(Integer id);
    int updateUser(UserBean user);
    YanBean findYan();
    int Register(UserBean user);
    int commit(CommitBean commit);
    List<CommitBean> findCommit(String userId);
    List<CommitBean> findAllCommit();
    UserBean loginInManager(String name,String password);
    UserBean loginIn(String name, String password);
    int updateCommit(Integer id);
    int updateCommitUnPass(Integer id);
    int updateCommitFile(Integer id, String filename);
}
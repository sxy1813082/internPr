package com.example.test.mapper;

import com.example.test.bean.CommitBean;
import com.example.test.bean.UserBean;
import com.example.test.bean.YanBean;
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
    YanBean findYan();
    UserBean getInfoManager(String name,String password);
    UserBean getInfo(String name,String password);
    int commit(CommitBean commit);
    List<CommitBean> findCommit(String userId);
    List<CommitBean> findAllCommit();

    int updateCommit(Integer id);

    int updateCommitUnPass(Integer id);

    int updateCommitFile(Integer id, String filename);
}
package com.draymond.service;

import com.draymond.pojo.Users;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    /**
     * @Description: 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * @Description: 查询用户是否存在
     */
    public Users queryUserForLogin(String username, String pwd);

    /**
     * @Description: 用户注册
     */
    public Users saveUser(Users user);



}

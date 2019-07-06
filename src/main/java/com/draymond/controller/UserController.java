package com.draymond.controller;

import com.draymond.pojo.Users;
import com.draymond.service.UserService;
import com.draymond.utils.IMoocJSONResult;
import com.draymond.utils.JsonUtils;
import com.draymond.utils.MD5Utils;
import com.draymond.vo.UsersVO;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Description: 用户注册/登录
 */

@RestController
@RequestMapping("/u")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * @Description: 用户注册/登录
     */
    @PostMapping(value="/registOrLogin" )
    public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception {

        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名或密码不能为空...");
        }

        // 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            // 1.2 注册
            user.setId(UUID.randomUUID().toString());
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            userResult = userService.saveUser(user);
        }

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult, userVO);

        return IMoocJSONResult.ok(userVO);
    }


    git commit -m "first commit"
}

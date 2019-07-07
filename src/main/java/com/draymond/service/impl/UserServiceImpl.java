package com.draymond.service.impl;


import com.draymond.mapper.UsersMapper;
import com.draymond.pojo.Users;
import com.draymond.pojo.UsersExample;
import com.draymond.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper userMapper;



	/**
	 * @Description: 判断用户名是否存在
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {


		UsersExample usersExample=new UsersExample();
		usersExample.createCriteria().andUsernameEqualTo(username);

		List<Users> userList = userMapper.selectByExample(usersExample);

		if(userList!=null&userList.size()!=0){
			return true;
		}
		
		return  false;
	}


	/**
	 * @Description: 查询用户是否存在
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String pwd) {

		UsersExample usersExample=new UsersExample();
		usersExample.createCriteria()
					.andUsernameEqualTo(username)
					.andPasswordEqualTo(pwd);



		Users result = userMapper.selectByExample(usersExample).get(0);
		
		return result;
	}


	/**
	 * @Description: 用户注册
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users saveUser(Users user) {
		


		// 为每个用户生成一个唯一的二维码
		user.setQrcode("");

		userMapper.insert(user);
		
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users updateUserInfo(Users user) {
		userMapper.updateByPrimaryKeySelective(user);
		return queryUserById(user.getId());
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	public Users queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

}

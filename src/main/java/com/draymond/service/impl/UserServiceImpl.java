package com.draymond.service.impl;


import com.draymond.enums.MsgSignFlagEnum;
import com.draymond.enums.SearchFriendsStatusEnum;
import com.draymond.mapper.*;
import com.draymond.netty.ChatMsg;
import com.draymond.pojo.*;
import com.draymond.service.UserService;
import com.draymond.utils.FastDFSClient;
import com.draymond.utils.FileUtils;
import com.draymond.utils.QRCodeUtils;
import com.draymond.vo.FriendRequestVO;
import com.draymond.vo.MyFriendsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper userMapper;

	@Autowired
	private QRCodeUtils qrCodeUtils;

	@Autowired
	private FastDFSClient fastDFSClient;

	@Autowired
	private MyFriendsMapper myFriendsMapper;

	@Autowired
	private FriendsRequestMapper friendsRequestMapper;

	@Autowired
	private ChatMsgMapper chatMsgMapper;

	@Autowired
	private UsersExtMapper usersExtMapper;

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

		String userId = UUID.randomUUID().toString();

		// 为每个用户生成一个唯一的二维码
		String qrCodePath = "D:\\FastDFS\\" + userId + "qrcode.png";
		// wexin_qrcode:[username]
		qrCodeUtils.createQRCode(qrCodePath, "wexin_qrcode:" + user.getUsername());
		MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

		String qrCodeUrl = "";
		try {
			qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.setQrcode(qrCodeUrl);

		user.setId(userId);
		userMapper.insert(user);
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users updateUserInfo(Users user) {
		userMapper.updateByPrimaryKeySelective(user);
		return queryUserById(user.getId());
	}

	@Override
	public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
		Users user = queryUserInfoByUsername(friendUsername);

		// 1. 搜索的用户如果不存在，返回[无此用户]
		if (user == null) {
			return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
		}

		// 2. 搜索账号是你自己，返回[不能添加自己]
		if (user.getId().equals(myUserId)) {
			return SearchFriendsStatusEnum.NOT_YOURSELF.status;
		}

		// 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
		MyFriendsExample mfc =new MyFriendsExample();
		mfc.createCriteria().andMyUserIdEqualTo(myUserId).andMyFriendUserIdEqualTo(user.getId());

		List<MyFriends> myFriendsRel = myFriendsMapper.selectByExample(mfc);
		if (myFriendsRel != null&& myFriendsRel.size()>0) {
			return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
		}

		return SearchFriendsStatusEnum.SUCCESS.status;
	}

	@Override
	public Users queryUserInfoByUsername(String username) {
		UsersExample ue = new UsersExample();
		ue.createCriteria().andUsernameEqualTo(username);

		return userMapper.selectByExample(ue).get(0);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void sendFriendRequest(String myUserId, String friendUsername) {

		// 根据用户名把朋友信息查询出来
		Users friend = queryUserInfoByUsername(friendUsername);

		// 1. 查询发送好友请求记录表
		FriendsRequestExample fre = new FriendsRequestExample();
		 fre.createCriteria().andSendUserIdEqualTo(myUserId).andAcceptUserIdEqualTo(friend.getId());

		List<FriendsRequest> friendRequest = friendsRequestMapper.selectByExample(fre);
		if (friendRequest == null  || friendRequest.size() == 0) {
			// 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
			String requestId = UUID.randomUUID().toString();

			FriendsRequest request = new FriendsRequest();
			request.setId(requestId);
			request.setSendUserId(myUserId);
			request.setAcceptUserId(friend.getId());
			request.setRequestDateTime(new Date());
			friendsRequestMapper.insert(request);
		}
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
		return usersExtMapper.queryFriendRequestList(acceptUserId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteFriendRequest(String sendUserId, String acceptUserId) {
		FriendsRequestExample fre = new FriendsRequestExample();
		fre.createCriteria().andSendUserIdEqualTo(sendUserId).andAcceptUserIdEqualTo(acceptUserId);
		friendsRequestMapper.deleteByExample(fre);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passFriendRequest(String sendUserId, String acceptUserId) {
		saveFriends(sendUserId, acceptUserId);
		saveFriends(acceptUserId, sendUserId);
		deleteFriendRequest(sendUserId, acceptUserId);


	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveFriends(String sendUserId, String acceptUserId) {
		MyFriends myFriends = new MyFriends();
		String recordId = UUID.randomUUID().toString();
		myFriends.setId(recordId);
		myFriends.setMyFriendUserId(acceptUserId);
		myFriends.setMyUserId(sendUserId);
		myFriendsMapper.insert(myFriends);
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<MyFriendsVO> queryMyFriends(String userId) {
		List<MyFriendsVO> myFirends = usersExtMapper.queryMyFriends(userId);
		return myFirends;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveMsg(ChatMsg chatMsg) {

		com.draymond.pojo.ChatMsg msgDB = new com.draymond.pojo.ChatMsg();
		String msgId = UUID.randomUUID().toString();
		msgDB.setId(msgId);
		msgDB.setAcceptUserId(chatMsg.getReceiverId());
		msgDB.setSendUserId(chatMsg.getSenderId());
		msgDB.setCreateTime(new Date());
		msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
		msgDB.setMsg(chatMsg.getMsg());

		chatMsgMapper.insert(msgDB);

		return msgId;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public Users queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}




	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateMsgSigned(List<String> msgIdList) {
		usersExtMapper.batchUpdateMsgSigned(msgIdList);
	}

}

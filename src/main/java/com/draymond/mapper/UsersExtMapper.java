package com.draymond.mapper;


import com.draymond.vo.FriendRequestVO;
import com.draymond.vo.MyFriendsVO;

import java.util.List;

public interface UsersExtMapper {
	
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

	public List<MyFriendsVO> queryMyFriends(String userId);

	public void batchUpdateMsgSigned(List<String> msgIdList);
	
}
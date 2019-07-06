package com.draymond.mapper;

import com.draymond.pojo.ChatMsg;
import com.draymond.pojo.ChatMsgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatMsgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int countByExample(ChatMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int deleteByExample(ChatMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int insert(ChatMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int insertSelective(ChatMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    List<ChatMsg> selectByExample(ChatMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    ChatMsg selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int updateByExampleSelective(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int updateByExample(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int updateByPrimaryKeySelective(ChatMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table chat_msg
     *
     * @mbggenerated Sat Jul 06 16:41:13 CST 2019
     */
    int updateByPrimaryKey(ChatMsg record);
}
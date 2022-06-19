package com.yychat.model;

public interface MessageType {
    String LOGIN_VALIDATE_SUCCESS="1";
    String LOGIN_VALIDATE_FAILURE="2";
    //实验五：客户端发送聊天信息到服务器，步骤2：定义新的消息类型
    String COMMON_CHAT_MESSAGE="3";

    //实验七：激活在线好友图标，步骤3：新增请求消息类型
    String REQUEST_ONLINE_FRIEND="4";
    String RESPONSE_ONLINE_FRIEND="5";

    //实验八：激活新上线好友图标，步骤2：新增消息类型
    String NEW_ONLINE_FRIEND_TO_SERVER="6";
    String NEW_ONLINE_FRIEND="7";

    //下线好友
    String OFFLINE_FRIEND_TO_SERVER = "8";
    String OFFLINE_FRIEND = "9";

    //实验十一：在user表中注册新用户，步骤9：添加注册成功和注册失败两种消息类型
    String USER_REGISTER_SUCCESSS="10";
    String USER_REGISTER_FAILURE="11";

    //实验十二：添加好友和更新好友列表，步骤4：增加添加好友的消息类型
    String ADD_NEW_FRIEND="12";
    String ADD_NEW_FRIEND_SUCCESS="13";
    String ADD_NEW_FRIEND_FAILURE_NO_USER="14";
    String ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND="15";

    //删除好友
    String  DELETE_FRIEND = "16";
    String  DELETE_FRIEND_SUCCESS = "17";
    String  DELETE_FRIEND_FAILURE_NO_USER = "18";
    String Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND="19";

    //抖动
    String SHAKE="20";

    //在客户端增加用户退出的消息类型
    String USER_EXIT_SERVER_THREAD_CLOSE = "21";
    String USER_EXIT_CLIENT_THREAD_CLOSE = "22";

    //修改密码
    String UPDATE_PASSWORD = "23";
    String UPDATE_PASSWORD_SUCCESS = "24";

    String REQUEST_Stranger = "25";
    String REQUEST_Stranger_SUCCESS = "26";

    String MOVEE_STRANGER = "27";
    String MOVEE_STRANGER_SUCCESS = "28";
    String MOVE_STRANGER_FAILURE = "29";
    String MOVE_FRIEND_FAILURE_NO_USER = "30";
}

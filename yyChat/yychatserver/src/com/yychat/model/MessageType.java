package com.yychat.model;

public interface MessageType {
    String LOGIN_VALIDATE_SUCCESS="1";
    String LOGIN_VALIDATE_FAILURE="2";
    //ʵ���壺�ͻ��˷���������Ϣ��������������2�������µ���Ϣ����
    String COMMON_CHAT_MESSAGE="3";

    //ʵ���ߣ��������ߺ���ͼ�꣬����3������������Ϣ����
    String REQUEST_ONLINE_FRIEND="4";
    String RESPONSE_ONLINE_FRIEND="5";

    //ʵ��ˣ����������ߺ���ͼ�꣬����2��������Ϣ����
    String NEW_ONLINE_FRIEND_TO_SERVER="6";
    String NEW_ONLINE_FRIEND="7";

    //���ߺ���
    String OFFLINE_FRIEND_TO_SERVER = "8";
    String OFFLINE_FRIEND = "9";

    //ʵ��ʮһ����user����ע�����û�������9�����ע��ɹ���ע��ʧ��������Ϣ����
    String USER_REGISTER_SUCCESSS="10";
    String USER_REGISTER_FAILURE="11";

    //ʵ��ʮ������Ӻ��Ѻ͸��º����б�����4��������Ӻ��ѵ���Ϣ����
    String ADD_NEW_FRIEND="12";
    String ADD_NEW_FRIEND_SUCCESS="13";
    String ADD_NEW_FRIEND_FAILURE_NO_USER="14";
    String ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND="15";

    //ɾ������
    String  DELETE_FRIEND = "16";
    String  DELETE_FRIEND_SUCCESS = "17";
    String  DELETE_FRIEND_FAILURE_NO_USER = "18";
    String Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND="19";

    //����
    String SHAKE="20";

    //�ڿͻ��������û��˳�����Ϣ����
    String USER_EXIT_SERVER_THREAD_CLOSE = "21";
    String USER_EXIT_CLIENT_THREAD_CLOSE = "22";

    //�޸�����
    String UPDATE_PASSWORD = "23";
    String UPDATE_PASSWORD_SUCCESS = "24";

    String REQUEST_Stranger = "25";
    String REQUEST_Stranger_SUCCESS = "26";

    String MOVEE_STRANGER = "27";
    String MOVEE_STRANGER_SUCCESS = "28";
    String MOVE_STRANGER_FAILURE = "29";
    String MOVE_FRIEND_FAILURE_NO_USER = "30";
}

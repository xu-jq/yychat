package com.yychat.model;

import java.io.Serializable;

public class User implements Serializable,UserType{
        String userName;
        String password;
       //ʵ��ʮһ����user����ע�����û�������3����User������µ�����(��Ա)
        String userType;
        
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
        
}
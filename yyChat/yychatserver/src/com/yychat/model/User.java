package com.yychat.model;

import java.io.Serializable;

public class User implements Serializable,UserType{
        String userName;
        String password;
       //实验十一：在user表中注册新用户，步骤3：在User类添加新的属性(成员)
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
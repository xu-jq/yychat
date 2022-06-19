package com.yychatserver.control;

import com.util.MD5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//实验九：通过数据库完成用户登录验证，步骤5:封装数据库代码
public class DBUtil {

	public static final String URL="jdbc:mysql://localhost:3306/yychat?serverTimezone=GMT%2B8";
	public static final String USERNAME="root";
	public static final String PASSWORD="xu123..123";

	 public static Connection getConnection() {//静态方法是类方法
    	 Connection conn=null;
    	 try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	return conn;
     }

	public static void moveFromUserRelation(String sender, String moveFriend, String relationType) {
		Connection conn = getConnection();
		String delete_from_UserRelation_Sql = "UPDATE userrelation set relationtype = '2' where masteruser = ? and slaveuser = ? and relationtype = ?";
		PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(delete_from_UserRelation_Sql);
			ptmt.setString(1,sender);
			ptmt.setString(2,moveFriend);
			ptmt.setString(3,relationType);
			ptmt.executeUpdate();
			closeDB(conn,ptmt);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	//实验十二：添加好友和更新好友列表，步骤6：添加查找关系的方法。
	 public static boolean seekUseRelation(String masterUser,String slaveUser,String relationType){
		 boolean friendRelation=false;
		 Connection conn=getConnection();
		 String seek_User_Relation_Sql="select * from userrelation where masteruser=? and slaveuser=?";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(seek_User_Relation_Sql);
			ptmt.setString(1,masterUser);
			ptmt.setString(2,slaveUser);
		    ResultSet rs=ptmt.executeQuery();//返回一个结果集
		    friendRelation=rs.next();
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return friendRelation;
	 }

		//删除好友 DBUtil类
	    public static void deleteFromUserRelation(String masterUser,String slaveUser,String relationType){
	        Connection conn = getConnection();
	        String delete_from_UserRelation_Sql = "delete from userrelation where masteruser = ? and slaveuser = ?";
	        PreparedStatement ptmt;

	        try {
	            ptmt = conn.prepareStatement(delete_from_UserRelation_Sql);
	            ptmt.setString(1,masterUser);
	            ptmt.setString(2,slaveUser);

	            ptmt.executeUpdate();
	            closeDB(conn,ptmt);
	        } catch (SQLException throwables) {
	            throwables.printStackTrace();
	        }
	    }


	//实验十三：保存聊天信息到message表中，步骤4：保存聊天信息在数据库message表中
	 public static void saveMessage(String sender,String receiver,String content) {
		 Connection conn=getConnection();
		 String insert_Into_Message_Sql="insert into message(sender,receiver,content,sendtime) values(?,?,?,?)";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(insert_Into_Message_Sql);
			ptmt.setString(1,sender);
			ptmt.setString(2,receiver);
			ptmt.setString(3,content);
			Date date=new Date();
			ptmt.setTimestamp(4,new java.sql.Timestamp(date.getTime()));
			System.out.println("从1970-1-1 0时0分0秒到现在有多少毫秒："+date.getTime());
		    ptmt.executeUpdate();
		    closeDB(conn,ptmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }

	//实验十二：添加好友和更新好友列表，步骤7：添加插入用户之间关系的方法。
	 public static void insertIntoUserRelation(String masterUser,String slaveUser,String relationType){
		 Connection conn=getConnection();
		 String insert_Into_UserRelation_Sql="insert into userrelation(masteruser,slaveuser,relationtype) values(?,?,?)";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(insert_Into_UserRelation_Sql);
			ptmt.setString(1,masterUser);
			ptmt.setString(2,slaveUser);
			ptmt.setString(3,relationType);
		    ptmt.executeUpdate();
		    closeDB(conn,ptmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }

	//实验十一：在user表中注册新用户，步骤14：添加在user表中查询userName的方法
	 public static boolean seekUser(String userName) {
		 boolean seekSuccess=false;
		 Connection conn=getConnection();
		 String seek_User_Sql="select * from user where username=?";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(seek_User_Sql);
			ptmt.setString(1,userName);
		    ResultSet rs=ptmt.executeQuery();//返回一个结果集
		    seekSuccess=rs.next();
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		  return seekSuccess;
	 }

	//实验十一：在user表中注册新用户，步骤15：添加在user表中插入新纪录的方法
	 public static void insertIntoUser(String userName,String password){
		 Connection conn=getConnection();
		 String encrypt = MD5.encrypt(password);
		 String insert_Into_User_Sql="insert into user(username,password) values(?,?)";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(insert_Into_User_Sql);
			ptmt.setString(1,userName);
			ptmt.setString(2,encrypt);
		    ptmt.executeUpdate();
		    closeDB(conn,ptmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }

	public static String seekStranger(String userName) {
		String allFriend="";
		Connection conn=getConnection();
		String seek_Stranger_Sql="select slaveuser from userrelation where masteruser=? and relationtype='2'";
		PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(seek_Stranger_Sql);
			ptmt.setString(1,userName);
			ResultSet rs=ptmt.executeQuery();//返回一个结果集
			while(rs.next())
				allFriend=allFriend+" "+rs.getString(1);
			closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return allFriend;
	}


	 //实验十：利用数据库userrelation表来更新好友列表，步骤2：添加从数据库userrelation中读取好友名字
	   public static String seekAllFriend(String userName) {
		   String allFriend="";
		   Connection conn=getConnection();
			 String seek_All_Friend_Sql="select slaveuser from userrelation where masteruser=? and relationtype='1'";
	         PreparedStatement ptmt;
			try {
				ptmt = conn.prepareStatement(seek_All_Friend_Sql);
				ptmt.setString(1,userName);
			    ResultSet rs=ptmt.executeQuery();//返回一个结果集
			  while(rs.next())
				  allFriend=allFriend+" "+rs.getString(1);
			    closeDB(conn,ptmt,rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		   return allFriend;
	   }


	public static boolean loginValidate(String userName,String password){
		 boolean loginSuccess=false;
		 Connection conn=getConnection();
		String encrypt = MD5.encrypt(password);
		String user_Login_Sql="select * from user where username=? and password=?";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(user_Login_Sql);
			ptmt.setString(1,userName);
		    ptmt.setString(2,encrypt);
		    ResultSet rs=ptmt.executeQuery();//返回一个结果集

		    //实验九：通过数据库完成用户登录验证，步骤4:利用查询的结果来判断是否允许登录
		    loginSuccess=rs.next();//rs中如果有记录，loginSuccess为真，否则为假
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return loginSuccess;
	 }

	//修改密码
	public static void updatePassword(String sender, String latterPassword) {
		Connection conn = getConnection();
		String encrypt = MD5.encrypt(latterPassword);
		String update_User_Sql = "UPDATE USER SET PASSWORD = ? WHERE username = ?";
		PreparedStatement ptmt;

		try {
			ptmt = conn.prepareStatement(update_User_Sql);
			ptmt.setString(1,encrypt);
			ptmt.setString(2,sender);

			ptmt.executeUpdate();
			closeDB(conn,ptmt);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}


	//实验十一：在user表中注册新用户，步骤16：添加closeDB的重载方法
	public static void closeDB(Connection conn,PreparedStatement ptmt) {
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(ptmt!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closeDB(Connection conn,PreparedStatement ptmt,ResultSet rs) {
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(ptmt!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(rs!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}



}

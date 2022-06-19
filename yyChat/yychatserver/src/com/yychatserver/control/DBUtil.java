package com.yychatserver.control;

import com.util.MD5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//ʵ��ţ�ͨ�����ݿ�����û���¼��֤������5:��װ���ݿ����
public class DBUtil {

	public static final String URL="jdbc:mysql://localhost:3306/yychat?serverTimezone=GMT%2B8";
	public static final String USERNAME="root";
	public static final String PASSWORD="xu123..123";

	 public static Connection getConnection() {//��̬�������෽��
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

	//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����6����Ӳ��ҹ�ϵ�ķ�����
	 public static boolean seekUseRelation(String masterUser,String slaveUser,String relationType){
		 boolean friendRelation=false;
		 Connection conn=getConnection();
		 String seek_User_Relation_Sql="select * from userrelation where masteruser=? and slaveuser=?";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(seek_User_Relation_Sql);
			ptmt.setString(1,masterUser);
			ptmt.setString(2,slaveUser);
		    ResultSet rs=ptmt.executeQuery();//����һ�������
		    friendRelation=rs.next();
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return friendRelation;
	 }

		//ɾ������ DBUtil��
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


	//ʵ��ʮ��������������Ϣ��message���У�����4������������Ϣ�����ݿ�message����
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
			System.out.println("��1970-1-1 0ʱ0��0�뵽�����ж��ٺ��룺"+date.getTime());
		    ptmt.executeUpdate();
		    closeDB(conn,ptmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }

	//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����7����Ӳ����û�֮���ϵ�ķ�����
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

	//ʵ��ʮһ����user����ע�����û�������14�������user���в�ѯuserName�ķ���
	 public static boolean seekUser(String userName) {
		 boolean seekSuccess=false;
		 Connection conn=getConnection();
		 String seek_User_Sql="select * from user where username=?";
         PreparedStatement ptmt;
		try {
			ptmt = conn.prepareStatement(seek_User_Sql);
			ptmt.setString(1,userName);
		    ResultSet rs=ptmt.executeQuery();//����һ�������
		    seekSuccess=rs.next();
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		  return seekSuccess;
	 }

	//ʵ��ʮһ����user����ע�����û�������15�������user���в����¼�¼�ķ���
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
			ResultSet rs=ptmt.executeQuery();//����һ�������
			while(rs.next())
				allFriend=allFriend+" "+rs.getString(1);
			closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return allFriend;
	}


	 //ʵ��ʮ���������ݿ�userrelation�������º����б�����2����Ӵ����ݿ�userrelation�ж�ȡ��������
	   public static String seekAllFriend(String userName) {
		   String allFriend="";
		   Connection conn=getConnection();
			 String seek_All_Friend_Sql="select slaveuser from userrelation where masteruser=? and relationtype='1'";
	         PreparedStatement ptmt;
			try {
				ptmt = conn.prepareStatement(seek_All_Friend_Sql);
				ptmt.setString(1,userName);
			    ResultSet rs=ptmt.executeQuery();//����һ�������
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
		    ResultSet rs=ptmt.executeQuery();//����һ�������

		    //ʵ��ţ�ͨ�����ݿ�����û���¼��֤������4:���ò�ѯ�Ľ�����ж��Ƿ������¼
		    loginSuccess=rs.next();//rs������м�¼��loginSuccessΪ�棬����Ϊ��
		    closeDB(conn,ptmt,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return loginSuccess;
	 }

	//�޸�����
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


	//ʵ��ʮһ����user����ע�����û�������16�����closeDB�����ط���
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

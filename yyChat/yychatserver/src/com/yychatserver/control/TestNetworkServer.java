package com.yychatserver.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.yychat.model.User;

import java.io.*;

public class TestNetworkServer {

	public static void main(String[] args) {
		  
		try {
			ServerSocket ss= new ServerSocket(3456);
			System.out.println("服务器启动，监听3456...");
			Socket s=ss.accept();//等待客户端的连接，程序阻塞
			System.out.println("建立连接成功"+s);
			
			InputStream is=s.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			User user=(User)ois.readObject();
			System.out.println("用户名："+user.getUserName());
			System.out.println("密码："+user.getPassword());
			
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
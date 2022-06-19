package com.yychatclient.control;

import java.io.IOException;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yychat.model.User;

import java.io.*;

public class TestNetworkClient {

	public static void main(String[] args) {
		try {
			Socket s=new Socket("127.0.0.1",3456);
			System.out.println("与服务器连接成功！"+s);
			User user=new User();
			user.setUserName("刘");
			user.setPassword("0601");
			//把user从客户端发送到服务器端
			OutputStream os=s.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(user);

			s.close();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

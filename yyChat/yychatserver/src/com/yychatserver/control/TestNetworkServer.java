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
			System.out.println("����������������3456...");
			Socket s=ss.accept();//�ȴ��ͻ��˵����ӣ���������
			System.out.println("�������ӳɹ�"+s);
			
			InputStream is=s.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			User user=(User)ois.readObject();
			System.out.println("�û�����"+user.getUserName());
			System.out.println("���룺"+user.getPassword());
			
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
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
			System.out.println("����������ӳɹ���"+s);
			User user=new User();
			user.setUserName("��");
			user.setPassword("0601");
			//��user�ӿͻ��˷��͵���������
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

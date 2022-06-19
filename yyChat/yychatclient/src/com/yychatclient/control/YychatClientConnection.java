package com.yychatclient.control;

import com.yychat.model.Message;
import com.yychat.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class YychatClientConnection {
	 //ʵ���壺�ͻ��˷���������Ϣ��������������5���޸�s������
    public static Socket s;

    public YychatClientConnection(){
        //�ز��ַ
        try {
            s = new Socket("localhost",3456);
            System.out.println("����������ӳɹ���"+s);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ʵ��ʮһ����user����ע�����û�������8������registerUser
    public boolean registerUser(User user) {
    	boolean registerSuccess=false;

    	 OutputStream os;
         Message mess=null;
         try {
             os = s.getOutputStream();
             ObjectOutputStream oos=new ObjectOutputStream(os);
             oos.writeObject(user);

             //��������Server�˵���Ϣ
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
             mess=(Message)ois.readObject();
             if (mess.getMessageType().equals(Message.USER_REGISTER_SUCCESSS))
                 registerSuccess=true;
             s.close();//ע���ر�Socket����
         } catch (IOException |ClassNotFoundException e) {
             e.printStackTrace();
         }
    	return registerSuccess;
    }

    public boolean loginValidate1(User user) {
        boolean loginValidate=false;
        OutputStream os;
        Message mess=null;
        try {
            os = s.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(user);

            //��������Server�˵���Ϣ
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
             mess=(Message)ois.readObject();
             if (mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
                 loginValidate=true;
           //ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������5����¼�ɹ���Ӧ��׼������������Ϣ
             new ClientReceiverThread(s).start();
             }

        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginValidate;
    }

    public Message loginValidate(User user) {
        OutputStream os;
        Message mess=null;
        try {
            os = s.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(user);

            //��������Server�˵���Ϣ
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            mess=(Message)ois.readObject();
           //ʵ��ʮ������Ӻ��Ѻ͸��º����б�����12�����������ͻ��˽������̵߳Ĵ��롣
             if (mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
                 new ClientReceiverThread(s).start();
             }
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mess;
    }
}

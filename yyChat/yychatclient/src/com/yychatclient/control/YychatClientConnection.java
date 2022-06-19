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
	 //实验五：客户端发送聊天信息到服务器，步骤5：修改s的属性
    public static Socket s;

    public YychatClientConnection(){
        //回测地址
        try {
            s = new Socket("localhost",3456);
            System.out.println("与服务器连接成功！"+s);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //实验十一：在user表中注册新用户，步骤8：调用registerUser
    public boolean registerUser(User user) {
    	boolean registerSuccess=false;

    	 OutputStream os;
         Message mess=null;
         try {
             os = s.getOutputStream();
             ObjectOutputStream oos=new ObjectOutputStream(os);
             oos.writeObject(user);

             //接受来自Server端的消息
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
             mess=(Message)ois.readObject();
             if (mess.getMessageType().equals(Message.USER_REGISTER_SUCCESSS))
                 registerSuccess=true;
             s.close();//注册后关闭Socket对象
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

            //接受来自Server端的消息
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
             mess=(Message)ois.readObject();
             if (mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
                 loginValidate=true;
           //实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤5：登录成功后，应该准备接受聊天信息
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

            //接受来自Server端的消息
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            mess=(Message)ois.readObject();
           //实验十二：添加好友和更新好友列表，步骤12：增加启动客户端接收者线程的代码。
             if (mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
                 new ClientReceiverThread(s).start();
             }
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mess;
    }
}

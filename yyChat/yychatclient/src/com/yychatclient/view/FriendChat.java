package com.yychatclient.view;


import com.yychat.model.*;
import com.yychatclient.control.YychatClientConnection;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FriendChat extends JFrame implements ActionListener {

//        JScrollPane jScrollPane;
//        JTextArea jArea;
//
//        JTextField jTextField;
//        JButton jButton;
//        JButton jButton2;
//        JPanel jPanel;
        String sender;
        String receiver;
//
//        //发送文件按钮
//        JButton sendFileBtn;

    /**聊天对方的信息Label*/
    private JLabel otherInfoLbl;
    /** 当前用户信息Lbl */
    private JLabel currentUserLbl;
    /**聊天信息列表区域*/
    public static JTextArea msgListArea;
    /**要发送的信息区域*/
    public static JTextArea sendArea;
    /** 在线用户列表 */
    public static JList onlineList;
    /** 在线用户数统计Lbl */
    public static JLabel onlineCountLbl;
    /** 准备发送的文件 */
    public static FileInfo sendFile;
    /** 私聊复选框 */
    public JCheckBox rybqBtn;

    JButton submitBtn;

    JButton shakeBtn;

        //public FriendChat() {
        public FriendChat(String sender,String receiver) {
            this.setTitle("yy聊天室");
            this.setSize(550, 500);
            this.setResizable(false);

            //设置默认窗体在屏幕中央
            int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);

            //左边主面板
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            //右边用户面板
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BorderLayout());

            // 创建一个分隔窗格
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    mainPanel, userPanel);
            splitPane.setDividerLocation(380);
            splitPane.setDividerSize(10);
            splitPane.setOneTouchExpandable(true);
            getContentPane().add(splitPane, BorderLayout.CENTER);

            //左上边信息显示面板
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BorderLayout());
            //右下连发送消息面板
            JPanel sendPanel = new JPanel();
            sendPanel.setLayout(new BorderLayout());

            // 创建一个分隔窗格
            JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    infoPanel, sendPanel);
            splitPane2.setDividerLocation(300);
            splitPane2.setDividerSize(1);
            mainPanel.add(splitPane2, BorderLayout.CENTER);

            otherInfoLbl = new JLabel("当前状态：" + sender+"和"+receiver+"在聊天");
            infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

            msgListArea = new JTextArea();
            msgListArea.setLineWrap(true);
            msgListArea.setForeground(Color.red);//把文字设置成红色
            infoPanel.add(new JScrollPane(msgListArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new BorderLayout());
            sendPanel.add(tempPanel, BorderLayout.NORTH);

            // 聊天按钮面板
            JPanel btnPanel = new JPanel();
            tempPanel.add(btnPanel, BorderLayout.CENTER);

		/*//字体按钮
		JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
		fontBtn.setMargin(new Insets(0,0,0,0));
		fontBtn.setToolTipText("设置字体和格式");

		//表情按钮
		JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
		faceBtn.setMargin(new Insets(0,0,0,0));
		faceBtn.setToolTipText("选择表情");*/

            //发送文件按钮
            shakeBtn = new JButton("抖动");
            shakeBtn.setMargin(new Insets(0,0,0,0));
            shakeBtn.setToolTipText("向对方发送窗口振动");
            shakeBtn.addActionListener(this);

            //发送文件按钮
            JButton sendFileBtn = new JButton();
            sendFileBtn.setMargin(new Insets(0,0,0,0));
            sendFileBtn.setToolTipText("向对方发送文件");

            JLabel label = new JLabel("");

            GroupLayout gl_btnPanel = new GroupLayout(btnPanel);
            gl_btnPanel.setHorizontalGroup(
                    gl_btnPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl_btnPanel.createSequentialGroup()
                                    /*.addGap(5)
                                    .addComponent(fontBtn)
                                    .addGap(5)
                                    .addComponent(faceBtn)*/
                                    .addGap(5)
                                    .addComponent(shakeBtn)
                                    .addGap(5)
                                    .addComponent(sendFileBtn)
                                    .addGroup(gl_btnPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(gl_btnPanel.createSequentialGroup()
                                                    .addGap(113)
                                                    .addComponent(label)
                                                    .addContainerGap(175, Short.MAX_VALUE))
                                            .addGroup(GroupLayout.Alignment.TRAILING, gl_btnPanel.createSequentialGroup()
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                             )))
            );
            gl_btnPanel.setVerticalGroup(
                    gl_btnPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl_btnPanel.createSequentialGroup()
                                    .addGap(5)
                                    .addGroup(gl_btnPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(gl_btnPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    )
                                            .addComponent(label)
                                            .addComponent(shakeBtn)
                                            .addComponent(sendFileBtn))
                                    .addContainerGap())
            );
            btnPanel.setLayout(gl_btnPanel);

            //要发送的信息的区域
            sendArea = new JTextArea();
            sendArea.setLineWrap(true);

            sendPanel.add(new JScrollPane(sendArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            // 聊天按钮面板
            JPanel btn2Panel = new JPanel();
            btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(btn2Panel, BorderLayout.SOUTH);
            JButton closeBtn = new JButton("关闭");
            closeBtn.setToolTipText("退出整个程序");
            btn2Panel.add(closeBtn);
            submitBtn = new JButton("发送");
            submitBtn.addActionListener(this);
            submitBtn.setToolTipText("按Enter键发送消息");
            btn2Panel.add(submitBtn);

            //用户列表面板
//            JPanel onlineListPane = new JPanel();
//            onlineListPane.setLayout(new BorderLayout());
//            onlineCountLbl = new JLabel("好友列表(1)");
//            onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

//            onlineListPane.setLayout(new GridLayout(friendCount,1));//网格布局去管理50个好友
//            for(int i=1;i<2;i++){
//                onlineList[i]=new JLabel("陈晨",new ImageIcon("yychatclient/src/images/duck.gif"),JLabel.LEFT);
//                onlineListPane.add(onlineList[i]);
//            }
//            onlineListPane.repaint();
//            onlineListPane.revalidate();

            //当前用户面板
//            JPanel currentUserPane = new JPanel();
//            currentUserPane.setLayout(new BorderLayout());
//            currentUserPane.add(new JLabel("当前用户"), BorderLayout.NORTH);

            // 右边用户列表创建一个分隔窗格
//            JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
//                    onlineListPane, currentUserPane);
//            splitPane3.setDividerLocation(340);
//            splitPane3.setDividerSize(1);
//            userPanel.add(splitPane3, BorderLayout.CENTER);

//            //获取在线用户并缓存
//            DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
            //用户列表
            String[] s = FriendList.allFriend.split(" ");
            List<User> users = new ArrayList<>();
            System.out.println("好友列表名字");
            for (int i = 1;i < s.length;i++){
                User user = new User();
                user.setUserName(s[i]);
                users.add(user);
                System.out.println(s[i]);
            }

            onlineList = new JList(new OnlineUserListModel(users));

            onlineList.setCellRenderer(new MyCellRenderer());
            //设置为单选模式
            onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            onlineListPane.add(new JScrollPane(onlineList,
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            //当前用户信息Label
            currentUserLbl = new JLabel();
//            currentUserPane.add(currentUserLbl);

            currentUserLbl.setIcon(
                    new ImageIcon("yyChat/yychatclient/src/images/duck2.gif"));
            currentUserLbl.setText(sender);

            this.sender=sender;
            this.receiver=receiver;

            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setVisible(true);
        }

      public static void main(String[] args) {
     //FriendChat friendChat=new FriendChat();
      }

      public void updateOnlineListModel(){

      }

     //实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤12:增加append()方法
      public void append(Message mess) {
    	//实验十三：保存聊天信息到message表中，步骤5：显示发送聊天信息的时间
          msgListArea.append(mess.getSendTime().toLocaleString()+"\r\n"+mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getChatContent()+"\n\r");
      }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource()==submitBtn) {
           msgListArea.append(sender+" to "+receiver+"说"+sendArea.getText()+"\n\r");

          //实验五：客户端发送聊天信息到服务器，步骤3：创建message对象，发送聊天信息到服务器
          Message mess=new Message();
          mess.setSender(sender);
          mess.setReceiver(receiver);
          String chatContent=sendArea.getText();
          mess.setChatContent(chatContent);
          mess.setMessageType(Message.COMMON_CHAT_MESSAGE);

          //实验五：客户端发送聊天信息到服务器，步骤4：发送聊天信息到服务器
          try {
            ObjectOutputStream oos=new ObjectOutputStream( YychatClientConnection.s.getOutputStream());
            oos.writeObject(mess);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
       }
        if(e.getSource().equals(shakeBtn)) {

            //实验五：客户端发送聊天信息到服务器，步骤3：创建message对象，发送聊天信息到服务器
            Message mess=new Message();
            mess.setSender(sender);
            mess.setReceiver(receiver);

            mess.setMessageType(Message.SHAKE);
            try {
                ObjectOutputStream oos=new ObjectOutputStream( YychatClientConnection.s.getOutputStream());
                oos.writeObject(mess);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int x = this.getX();
            int y = this.getY();
            for (int i = 0; i < 20; i++) {
                if ((i & 1) == 0) {
                    x += 3;
                    y += 3;
                } else {
                    x -= 3;
                    y -= 3;
                }
                this.setLocation(x, y);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

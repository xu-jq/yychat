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
//        //�����ļ���ť
//        JButton sendFileBtn;

    /**����Է�����ϢLabel*/
    private JLabel otherInfoLbl;
    /** ��ǰ�û���ϢLbl */
    private JLabel currentUserLbl;
    /**������Ϣ�б�����*/
    public static JTextArea msgListArea;
    /**Ҫ���͵���Ϣ����*/
    public static JTextArea sendArea;
    /** �����û��б� */
    public static JList onlineList;
    /** �����û���ͳ��Lbl */
    public static JLabel onlineCountLbl;
    /** ׼�����͵��ļ� */
    public static FileInfo sendFile;
    /** ˽�ĸ�ѡ�� */
    public JCheckBox rybqBtn;

    JButton submitBtn;

    JButton shakeBtn;

        //public FriendChat() {
        public FriendChat(String sender,String receiver) {
            this.setTitle("yy������");
            this.setSize(550, 500);
            this.setResizable(false);

            //����Ĭ�ϴ�������Ļ����
            int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);

            //��������
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            //�ұ��û����
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BorderLayout());

            // ����һ���ָ�����
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    mainPanel, userPanel);
            splitPane.setDividerLocation(380);
            splitPane.setDividerSize(10);
            splitPane.setOneTouchExpandable(true);
            getContentPane().add(splitPane, BorderLayout.CENTER);

            //���ϱ���Ϣ��ʾ���
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BorderLayout());
            //������������Ϣ���
            JPanel sendPanel = new JPanel();
            sendPanel.setLayout(new BorderLayout());

            // ����һ���ָ�����
            JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    infoPanel, sendPanel);
            splitPane2.setDividerLocation(300);
            splitPane2.setDividerSize(1);
            mainPanel.add(splitPane2, BorderLayout.CENTER);

            otherInfoLbl = new JLabel("��ǰ״̬��" + sender+"��"+receiver+"������");
            infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

            msgListArea = new JTextArea();
            msgListArea.setLineWrap(true);
            msgListArea.setForeground(Color.red);//���������óɺ�ɫ
            infoPanel.add(new JScrollPane(msgListArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new BorderLayout());
            sendPanel.add(tempPanel, BorderLayout.NORTH);

            // ���찴ť���
            JPanel btnPanel = new JPanel();
            tempPanel.add(btnPanel, BorderLayout.CENTER);

		/*//���尴ť
		JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
		fontBtn.setMargin(new Insets(0,0,0,0));
		fontBtn.setToolTipText("��������͸�ʽ");

		//���鰴ť
		JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
		faceBtn.setMargin(new Insets(0,0,0,0));
		faceBtn.setToolTipText("ѡ�����");*/

            //�����ļ���ť
            shakeBtn = new JButton("����");
            shakeBtn.setMargin(new Insets(0,0,0,0));
            shakeBtn.setToolTipText("��Է����ʹ�����");
            shakeBtn.addActionListener(this);

            //�����ļ���ť
            JButton sendFileBtn = new JButton();
            sendFileBtn.setMargin(new Insets(0,0,0,0));
            sendFileBtn.setToolTipText("��Է������ļ�");

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

            //Ҫ���͵���Ϣ������
            sendArea = new JTextArea();
            sendArea.setLineWrap(true);

            sendPanel.add(new JScrollPane(sendArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            // ���찴ť���
            JPanel btn2Panel = new JPanel();
            btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(btn2Panel, BorderLayout.SOUTH);
            JButton closeBtn = new JButton("�ر�");
            closeBtn.setToolTipText("�˳���������");
            btn2Panel.add(closeBtn);
            submitBtn = new JButton("����");
            submitBtn.addActionListener(this);
            submitBtn.setToolTipText("��Enter��������Ϣ");
            btn2Panel.add(submitBtn);

            //�û��б����
//            JPanel onlineListPane = new JPanel();
//            onlineListPane.setLayout(new BorderLayout());
//            onlineCountLbl = new JLabel("�����б�(1)");
//            onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

//            onlineListPane.setLayout(new GridLayout(friendCount,1));//���񲼾�ȥ����50������
//            for(int i=1;i<2;i++){
//                onlineList[i]=new JLabel("�³�",new ImageIcon("yychatclient/src/images/duck.gif"),JLabel.LEFT);
//                onlineListPane.add(onlineList[i]);
//            }
//            onlineListPane.repaint();
//            onlineListPane.revalidate();

            //��ǰ�û����
//            JPanel currentUserPane = new JPanel();
//            currentUserPane.setLayout(new BorderLayout());
//            currentUserPane.add(new JLabel("��ǰ�û�"), BorderLayout.NORTH);

            // �ұ��û��б���һ���ָ�����
//            JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
//                    onlineListPane, currentUserPane);
//            splitPane3.setDividerLocation(340);
//            splitPane3.setDividerSize(1);
//            userPanel.add(splitPane3, BorderLayout.CENTER);

//            //��ȡ�����û�������
//            DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
            //�û��б�
            String[] s = FriendList.allFriend.split(" ");
            List<User> users = new ArrayList<>();
            System.out.println("�����б�����");
            for (int i = 1;i < s.length;i++){
                User user = new User();
                user.setUserName(s[i]);
                users.add(user);
                System.out.println(s[i]);
            }

            onlineList = new JList(new OnlineUserListModel(users));

            onlineList.setCellRenderer(new MyCellRenderer());
            //����Ϊ��ѡģʽ
            onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            onlineListPane.add(new JScrollPane(onlineList,
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

            //��ǰ�û���ϢLabel
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

     //ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������12:����append()����
      public void append(Message mess) {
    	//ʵ��ʮ��������������Ϣ��message���У�����5����ʾ����������Ϣ��ʱ��
          msgListArea.append(mess.getSendTime().toLocaleString()+"\r\n"+mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getChatContent()+"\n\r");
      }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource()==submitBtn) {
           msgListArea.append(sender+" to "+receiver+"˵"+sendArea.getText()+"\n\r");

          //ʵ���壺�ͻ��˷���������Ϣ��������������3������message���󣬷���������Ϣ��������
          Message mess=new Message();
          mess.setSender(sender);
          mess.setReceiver(receiver);
          String chatContent=sendArea.getText();
          mess.setChatContent(chatContent);
          mess.setMessageType(Message.COMMON_CHAT_MESSAGE);

          //ʵ���壺�ͻ��˷���������Ϣ��������������4������������Ϣ��������
          try {
            ObjectOutputStream oos=new ObjectOutputStream( YychatClientConnection.s.getOutputStream());
            oos.writeObject(mess);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
       }
        if(e.getSource().equals(shakeBtn)) {

            //ʵ���壺�ͻ��˷���������Ϣ��������������3������message���󣬷���������Ϣ��������
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

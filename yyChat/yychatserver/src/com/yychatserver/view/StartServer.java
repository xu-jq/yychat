package com.yychatserver.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.yychatserver.control.YychatServer;

public class StartServer extends JFrame implements ActionListener{
   JButton startServerJButton;
   JButton stopServerJButton;
   JPanel jPanel;
	
	public StartServer() {
		startServerJButton=new JButton("启动服务器");
		startServerJButton.addActionListener(this);
		stopServerJButton=new JButton("停止服务器");
		jPanel=new JPanel();
		jPanel.add(startServerJButton);
		jPanel.add(stopServerJButton);
		this.add(jPanel);
		
		this.setSize(300,100);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		StartServer StartServer=new StartServer();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startServerJButton) {
			new YychatServer();
		}
	}

}
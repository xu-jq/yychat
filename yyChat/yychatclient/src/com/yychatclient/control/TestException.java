package com.yychatclient.control;

public class TestException {

	public static void main(String[] args) {
		// 产生除以0的算术异常，程序中断
		try {
			int i=10/0;
		}catch(Exception e) {
			
		}
//因执行上一句代码时程序产生异常，中断，该条语句不会执行
		System.out.println("end");
	}

}

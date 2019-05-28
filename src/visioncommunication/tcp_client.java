package visioncommunication;

import java.net.*;
import java.io.*;




public class tcp_client {
	Socket client = null;
	InputStream input;
	OutputStream output;
	/**
	 * 
	 *************       create_tcp_client      ************
	 *	@param ip：服务器IP
	 *	@param port：服务器端口
	 *	void：
	 *	Description:创建一个TCP客服端
	 *	2019-5-28：
	 */
	public void create_tcp_client(String ip, int port) {
		try {
			client = new Socket(InetAddress.getByName(ip), port);//创建实例，通过ip名，反向查找IP
			client.setSoTimeout(5000);//连接延时5秒
		} catch (IOException e) {//出错
			System.out.println("tcp client connect failed!\r\n");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *************       tcp_client_get_data      ************
	 *	@return 字符串数据
	 *	@throws IOException：
	 *	String：
	 *	Description:从服务器得到所有的数据 包括数据头和尾
	 *	2019-5-28：
	 */
	public String tcp_client_get_data() throws IOException {
		String string = null;
		input = client.getInputStream();//从输入流获取数据
		byte[] bytes = new byte[1500];//缓冲区定义的大小
		int len = input.read(bytes);//读取数据并获得数据实际长度
		if (len!=-1) {//确保读取到数据
			string = new String(bytes,0,len);//将byte数组数据转换成string返回
		}
		
		return string;
	}
	
	/**
	 * 
	 *************       tcp_client_get_data      ************
	 *	@param framehead ：数据头  string数据类型
	 *	@return 字符串数据
	 *	@throws IOException：
	 *	String：
	 *	Description:默认以0x0d 0x0a结尾，解析数据，去掉帧头和帧尾，返回实际的字符串数据
	 *	2019-5-28：
	 */
	public String tcp_client_get_data(String framehead) throws IOException {
		String stringdata = null;
		boolean headok = false;
		input = client.getInputStream();
		byte[] bytes = new byte[1500];
		int len = input.read(bytes);
		if (len != -1) {
			String string = new String(bytes,0,len);
			byte[] data = string.getBytes();
			byte[] head = framehead.getBytes();
			int datalength = data.length;
			int headlength = head.length;
			for (int i = 0; i < head.length; i++) {//先判断帧头是否符合，逐个字符比较，效率有道低，但数据不大，还好
				if (data[i]==head[i]) {
					headok = true;
				}else {
					headok = false;
				}
			}
			if(headok){//如果帧头符合
				if (data[datalength - 2] == 0x0d) {//判断是否是以0xda结尾
					if (data[datalength - 1] == 0x0a) {//帧尾符合要求，提取数据
						stringdata = new String(data,headlength,datalength - headlength -2);					
					}
				}
			}	
		}
			
		return stringdata;//返回字符串数据
	}

	/**
	 * 
	 *************       tcp_client_send_data      ************
	 *	@param data //需要发送的数据 byte类型
	 *	@throws IOException：
	 *	void：
	 *	Description:将要发送的数据发送给服务器
	 *	2019-5-28：
	 */
	public void tcp_client_send_data(byte[] data) throws IOException {
		output = client.getOutputStream();//通过输出流发送数据出去
		output.write(data);
	}
	
	/**
	 * 
	 *************       tcp_client_close      ************
	 *	@throws IOException：
	 *	void：
	 *	Description:关闭tcp客服端的连接
	 *	2019-5-28：
	 */
	public void tcp_client_close() throws IOException {
		client.shutdownOutput();//关闭输出流，不能发送数据，但是可接收
		client.shutdownInput();//关闭输入流，不能接收数据
		client.close();	//完全立马关闭输入输出流，只用close 可能有造成服务器和客服端信息接收发送阻塞而收发不到数据
	}
	
	
	
	
	
	
}

package visioncommunication;
import java.net.*;
import java.io.*;

public class tcp_server {
	ServerSocket server = null;
	Socket socket = null;
	InputStream input;
	OutputStream output;
	
	/**
	 * 
	 *************       create_tcp_server      ************
	 *	@param port：服务器创建的端口号
	 *	void：
	 *	Description:没有循环监听，再次连接客服端需要重新开启服务器
	 *	2019-5-29：
	 */
	public void create_tcp_server(int port){		
		try {
			server = new ServerSocket(port);
			socket = server.accept();						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("this port can not create server");
		}
	}
	
	/**
	 * 
	 *************       tcp_server_get_data      ************
	 *	@return 收到的整个数据包
	 *	@throws IOException：
	 *	String：
	 *	Description:服务器接收数据
	 *	2019-5-29：
	 */
	public String tcp_server_get_data() throws IOException{
		String string = null;
		input = socket.getInputStream();//从输入流获取数据
		byte[] bytes = new byte[1500];//缓冲区定义的大小
		int len = input.read(bytes);//读取数据并获得数据实际长度
		if (len!=-1) {//确保读取到数据
			string = new String(bytes,0,len);//将byte数组数据转换成string返回
		}		
		return string;
	}
	
	/**
	 * 
	 *************       tcp_server_get_data      ************
	 *	@param framehead 数据帧头
	 *	@return 出去帧头 帧尾后的数据
	 *	@throws IOException：
	 *	String：
	 *	Description:默认以0x0d 0x0a结尾
	 *	2019-5-29：
	 */
	public String tcp_server_get_data(String framehead) throws IOException{
		String stringdata = null;
		boolean headok = false;
		input = socket.getInputStream();
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
	 *************       tcp_server_send_data      ************
	 *	@param data 需要发送出去的数据 byte类型
	 *	@throws IOException：
	 *	void：
	 *	Description:服务器发送数据
	 *	2019-5-29：
	 */
	public void tcp_server_send_data(byte[] data) throws IOException{
		output = socket.getOutputStream();//通过输出流发送数据出去
		output.write(data);
	}	
	
	/**
	 * 
	 *************       tcp_server_close      ************：
	 *	void：
	 *	Description:关闭服务器，中断所有连接
	 *	2019-5-29：
	 */
	public void tcp_server_close() {
		
		try {
			socket.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("close server failed");
		}
	}
	
	
}






















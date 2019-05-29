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
	 *	@param port�������������Ķ˿ں�
	 *	void��
	 *	Description:û��ѭ���������ٴ����ӿͷ�����Ҫ���¿���������
	 *	2019-5-29��
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
	 *	@return �յ����������ݰ�
	 *	@throws IOException��
	 *	String��
	 *	Description:��������������
	 *	2019-5-29��
	 */
	public String tcp_server_get_data() throws IOException{
		String string = null;
		input = socket.getInputStream();//����������ȡ����
		byte[] bytes = new byte[1500];//����������Ĵ�С
		int len = input.read(bytes);//��ȡ���ݲ��������ʵ�ʳ���
		if (len!=-1) {//ȷ����ȡ������
			string = new String(bytes,0,len);//��byte��������ת����string����
		}		
		return string;
	}
	
	/**
	 * 
	 *************       tcp_server_get_data      ************
	 *	@param framehead ����֡ͷ
	 *	@return ��ȥ֡ͷ ֡β�������
	 *	@throws IOException��
	 *	String��
	 *	Description:Ĭ����0x0d 0x0a��β
	 *	2019-5-29��
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
			for (int i = 0; i < head.length; i++) {//���ж�֡ͷ�Ƿ���ϣ�����ַ��Ƚϣ�Ч���е��ͣ������ݲ��󣬻���
				if (data[i]==head[i]) {
					headok = true;
				}else {
					headok = false;
				}
			}
			if(headok){//���֡ͷ����
				if (data[datalength - 2] == 0x0d) {//�ж��Ƿ�����0xda��β
					if (data[datalength - 1] == 0x0a) {//֡β����Ҫ����ȡ����
						stringdata = new String(data,headlength,datalength - headlength -2);					
					}
				}
			}	
		}			
		return stringdata;//�����ַ�������
	}
	
	/**
	 * 
	 *************       tcp_server_send_data      ************
	 *	@param data ��Ҫ���ͳ�ȥ������ byte����
	 *	@throws IOException��
	 *	void��
	 *	Description:��������������
	 *	2019-5-29��
	 */
	public void tcp_server_send_data(byte[] data) throws IOException{
		output = socket.getOutputStream();//ͨ��������������ݳ�ȥ
		output.write(data);
	}	
	
	/**
	 * 
	 *************       tcp_server_close      ************��
	 *	void��
	 *	Description:�رշ��������ж���������
	 *	2019-5-29��
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






















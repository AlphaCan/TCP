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
	 *	@param ip��������IP
	 *	@param port���������˿�
	 *	void��
	 *	Description:����һ��TCP�ͷ���
	 *	2019-5-28��
	 */
	public void create_tcp_client(String ip, int port) {
		try {
			client = new Socket(InetAddress.getByName(ip), port);//����ʵ����ͨ��ip�����������IP
			client.setSoTimeout(5000);//������ʱ5��
		} catch (IOException e) {//����
			System.out.println("tcp client connect failed!\r\n");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *************       tcp_client_get_data      ************
	 *	@return �ַ�������
	 *	@throws IOException��
	 *	String��
	 *	Description:�ӷ������õ����е����� ��������ͷ��β
	 *	2019-5-28��
	 */
	public String tcp_client_get_data() throws IOException {
		String string = null;
		input = client.getInputStream();//����������ȡ����
		byte[] bytes = new byte[1500];//����������Ĵ�С
		int len = input.read(bytes);//��ȡ���ݲ��������ʵ�ʳ���
		if (len!=-1) {//ȷ����ȡ������
			string = new String(bytes,0,len);//��byte��������ת����string����
		}
		
		return string;
	}
	
	/**
	 * 
	 *************       tcp_client_get_data      ************
	 *	@param framehead ������ͷ  string��������
	 *	@return �ַ�������
	 *	@throws IOException��
	 *	String��
	 *	Description:Ĭ����0x0d 0x0a��β���������ݣ�ȥ��֡ͷ��֡β������ʵ�ʵ��ַ�������
	 *	2019-5-28��
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
	 *************       tcp_client_send_data      ************
	 *	@param data //��Ҫ���͵����� byte����
	 *	@throws IOException��
	 *	void��
	 *	Description:��Ҫ���͵����ݷ��͸�������
	 *	2019-5-28��
	 */
	public void tcp_client_send_data(byte[] data) throws IOException {
		output = client.getOutputStream();//ͨ��������������ݳ�ȥ
		output.write(data);
	}
	
	/**
	 * 
	 *************       tcp_client_close      ************
	 *	@throws IOException��
	 *	void��
	 *	Description:�ر�tcp�ͷ��˵�����
	 *	2019-5-28��
	 */
	public void tcp_client_close() throws IOException {
		client.shutdownOutput();//�ر�����������ܷ������ݣ����ǿɽ���
		client.shutdownInput();//�ر������������ܽ�������
		client.close();	//��ȫ����ر������������ֻ��close ��������ɷ������Ϳͷ�����Ϣ���շ����������շ���������
	}
	
	
	
	
	
	
}

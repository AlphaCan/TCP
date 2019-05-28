package visioncommunication;

import java.io.IOException;

public class test {

	/**
	 *************       main      ************
	 *	@param args£º
	 *	void£º
	 *	Description:TODO
	 *	2019-5-28£º
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		tcp_client client = new tcp_client();
		client.create_tcp_client("127.0.0.1", 9990);
		byte[] bytes = new byte[5];
		bytes[0] = 0x1;
		bytes[1] = 0x2;
		bytes[2] = 0x3;
		bytes[3] = 0x4;
		bytes[4] = 0x5;
		
		client.tcp_client_send_data(bytes);
		while (true) {
			String mes = client.tcp_client_get_data("0xffff");	
			
			System.out.println(mes);
		}
								

	}


}

package ting.youri.tcpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/*
 * �������ڶ��߳��࣬���ڷ����
 */
public class SocketThread implements Runnable{
	
	private Socket client;
	
	public SocketThread(Socket client){
		this.client = client;
	}
	@Override
	public void run() {
		//����Ҫ������ǽ����յ��ַ������д���Ȼ�󷵻ظ��ͻ���

		try {
			//������
			InputStream input = client.getInputStream();
			//������
			OutputStream out = client.getOutputStream();
			//ͨ��BufferedReader����װ
			BufferedReader buffReader = new BufferedReader(
										new InputStreamReader(input));
			
			boolean flag = true;
			while(flag){
				String read = buffReader.readLine();
				if(read == null || "".equals(read)){
					flag = false;
				}else{
					System.out.println("����˽��տͻ��˵���Ϣ�� " + read );
					out.write(("echo: " + read).getBytes());
					out.flush();
				}
			}
			out.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

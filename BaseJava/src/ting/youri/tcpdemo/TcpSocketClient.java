package ting.youri.tcpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.sun.star.bridge.oleautomation.SCode;

public class TcpSocketClient {
	
	//��ʼ���ͻ���
	public void  init(String ip, int port) throws IOException{
		
		//
		Socket socket = new Socket(ip, port);
		socket.setSoTimeout(10000);
		
		BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
		boolean  flag = true;
		OutputStream out = socket.getOutputStream();
		InputStream input = socket.getInputStream();
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(input));
		while(flag){
			System.out.print("������Ϣ�� ");
			String  str  = keyInput.readLine();
			//�����ж��Ƿ����
			if(str.equals("bye")){
				flag = false;
			}else{
				//��������
				out.write(str.getBytes());
				out.flush();
				out.close();
				//����������֮�󣬿�ʼ�����������������
				try {
					String back = bufReader.readLine();
					System.out.println(back);
					
				} catch (SocketTimeoutException e) {
					System.out.println("time out!");
				}
			}
		}
		input.close();
		if(socket != null){
			//������캯�����������½⣬��ر��׽��֣����û�н������ӣ���Ȼ���ùر�
			socket.close(); //���ر�socket,����������������Ҳ�ᱻ�ر�
		}
	}
	
	public static void main(String[] args) {
		TcpSocketClient tcpSocketClient = new TcpSocketClient();
		try {
			tcpSocketClient.init("127.0.0.1",10506);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package ting.youri.tcpdemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.InitialContext;

public class TcpSocketServer {
	
	//ָ���˿ڣ���ʼ������Server
	public void init(int port) throws IOException{
		
		ServerSocket server = new ServerSocket(port);
		System.out.println("��ʼ����������");
		Socket clientSocket = null;
		boolean flag = true;
		while(flag){
			//�ȴ��ͻ������ӣ������ȡ�ɹ�
			clientSocket = server.accept();
			System.out.println("��ͻ������ӳɹ���");
			Thread t = new Thread(new SocketThread(clientSocket));
			t.start();
			
		}
		server.close();
	}
	
	public static void main(String[] args) {
		TcpSocketServer tcpSocketServer = new TcpSocketServer();
		try {
			tcpSocketServer.init(10506);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

package ting.youri.tcpdemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.InitialContext;

public class TcpSocketServer {
	
	//指定端口，初始化创建Server
	public void init(int port) throws IOException{
		
		ServerSocket server = new ServerSocket(port);
		System.out.println("初始化服务器！");
		Socket clientSocket = null;
		boolean flag = true;
		while(flag){
			//等待客户端连接，如果获取成功
			clientSocket = server.accept();
			System.out.println("与客户端连接成功！");
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

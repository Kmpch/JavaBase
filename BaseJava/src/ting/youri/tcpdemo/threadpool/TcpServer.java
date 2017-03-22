package ting.youri.tcpdemo.threadpool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	
	//init 初始化的时候，创建
	public ServerSocket init(int port){
		ServerSocket server = null;
		try {
			 server = new ServerSocket(port);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return server;
	}
}

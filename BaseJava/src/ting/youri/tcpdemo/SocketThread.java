package ting.youri.tcpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/*
 * 该类用于多线程类，用于服务端
 */
public class SocketThread implements Runnable{
	
	private Socket client;
	
	public SocketThread(Socket client){
		this.client = client;
	}
	@Override
	public void run() {
		//这里要处理的是将接收的字符串进行处理，然后返回给客户端

		try {
			//接收流
			InputStream input = client.getInputStream();
			//发送流
			OutputStream out = client.getOutputStream();
			//通过BufferedReader来封装
			BufferedReader buffReader = new BufferedReader(
										new InputStreamReader(input));
			
			boolean flag = true;
			while(flag){
				String read = buffReader.readLine();
				if(read == null || "".equals(read)){
					flag = false;
				}else{
					System.out.println("服务端接收客户端的消息： " + read );
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

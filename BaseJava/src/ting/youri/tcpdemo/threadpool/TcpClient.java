package ting.youri.tcpdemo.threadpool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
	private String name;
	public TcpClient(String name){
		this.name = name;
	}
	//客户端初始化创建
	public void init(){
		//用于本地地址
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			Socket client = new Socket(address, 23006);
			OutputStream outputStream = client.getOutputStream();
			PrintStream ps = new PrintStream(outputStream);
			ps.write((name + "-请求连接服务器！").getBytes());
			ps.flush();
			ps.close();
			BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(client.getInputStream()));
			int r = -1;
			String response = "";
			while((r = bufferedReader.read()) != -1){
				response += (char)r;
			}
			System.out.println("从服务器返回的消息：　" + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

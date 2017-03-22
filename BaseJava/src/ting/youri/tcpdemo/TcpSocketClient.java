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
	
	//初始化客户端
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
			System.out.print("输入信息： ");
			String  str  = keyInput.readLine();
			//进行判断是否结束
			if(str.equals("bye")){
				flag = false;
			}else{
				//发送数据
				out.write(str.getBytes());
				out.flush();
				out.close();
				//发送完数据之后，开始监听服务端来得数据
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
			//如果构造函数建立起了谅解，则关闭套接字，如果没有建立连接，自然不用关闭
			socket.close(); //当关闭socket,其关联的输入输出流也会被关闭
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

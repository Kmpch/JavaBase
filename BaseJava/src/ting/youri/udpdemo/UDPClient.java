package ting.youri.udpdemo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
	
	//设置接收数据的超时时间
	private static final int TIMEOUT = 	5000;
	//设置重发数据的最多次数
	private static final int MAXNUM = 5;
	
	public static void main(String[] args) throws Exception {
		
		String  str_send = "Hello UDPserver";
		byte[] buf = new byte[1024];
		
		//客户端在9000端口监听接收到的数据
		DatagramSocket ds = new DatagramSocket(9000);
		//获取本地ip地址
		InetAddress loc = InetAddress.getLocalHost();
		
		//定义发送数据DatagramPacket
		//发送数据，同时包含指定发送的ip和端口
		DatagramPacket dp_send = new DatagramPacket(str_send.getBytes(), str_send.length(),
								loc,3000);
		
		//定义接收数据的DatagramPacket实例
		DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
		/**
		 * 发送数据到本地3000端口
		 * 
		 */
		ds.setSoTimeout(TIMEOUT); //设置接收数据时阻塞的最长时间
		int tries = 0;  //重发数据的次数
		boolean receiveResponse = false; //是否接收到数据的标志位
		
		//直到接收数据，或者重发次数达到预定值，则推出循环
		while(!receiveResponse && tries < MAXNUM){
			
			//发送数据
			ds.send(dp_send);
			try {
				//接收从服务器端发送回来的数据
				ds.receive(dp_receive);
			} catch (Exception e) {
			}
			
		}
		
		
		
	}
	
}

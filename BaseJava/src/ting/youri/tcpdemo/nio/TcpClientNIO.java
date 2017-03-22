package ting.youri.tcpdemo.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.sun.star.io.SocketException;
import com.sun.star.lang.IllegalArgumentException;

public class TcpClientNIO {
	
	public static void main(String[] args) throws Exception {
		
		if((args.length < 2) || (args.length >3)){
			throw  new IllegalArgumentException("参数不正确");
		}
		
		//第一个参数作为服务器主机名或ip
		String server = args[0];
		//第二参数作为端口号
		int port = (args.length == 2)? Integer.parseInt(args[1]) : 7;
		//第三个参数作为输入内容
		String message = args[2];
		//转换成bytes[]
		byte[] msgBytes = message.getBytes();
		
		//创建一个信道，并设为非阻塞模式
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		//向服务器端发起连接
		if(!socketChannel.connect(new InetSocketAddress(server, port))){
			
			//不断地轮询连接状态，直到完成连接
			while(!socketChannel.finishConnect()){
				
				//在等待时间里，可以执行其他任务，以充分发挥阻塞IO的异步特性
				//这里为了演示该方法的使用，只是一直打印
				System.out.println("wait for connecting,to do other things");
			}
			
			//分别实例化读写缓冲区
			ByteBuffer sendBuf = ByteBuffer.wrap(msgBytes);
			ByteBuffer readBuf = ByteBuffer.allocate(msgBytes.length);
			
			//接收到总的字节数
			int totalBytesRcvd = 0;
			//每一次调用read()方法接到的字节数
			int bytesRcvd;
			//循环接收和发送
			while(totalBytesRcvd < msgBytes.length){
				//判断当前通道中的缓冲区是否还有剩余字节，将数据写入通道
				//发送
				if(sendBuf.hasRemaining()){
					//通道将缓冲区的内容抽出去
					socketChannel.write(sendBuf);
				}
				//接收
				if((bytesRcvd = socketChannel.read(readBuf)) == -1){
					throw new SocketException("Connection closed prematurely");
				}
				totalBytesRcvd += bytesRcvd;
				//在等待时间里，可以执行其他任务，以充分发挥阻塞IO的异步特性
				//这里为了演示该方法的使用，只是一直打印
				System.out.println("wait for reading or writing,to do other things");
				
			}
			System.out.println("Received: " + new String(readBuf.array(),0,totalBytesRcvd));
			socketChannel.close();
		}
		
		
	
	}
	
}

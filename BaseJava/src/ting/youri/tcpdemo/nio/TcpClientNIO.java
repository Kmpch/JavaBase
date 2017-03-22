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
			throw  new IllegalArgumentException("��������ȷ");
		}
		
		//��һ��������Ϊ��������������ip
		String server = args[0];
		//�ڶ�������Ϊ�˿ں�
		int port = (args.length == 2)? Integer.parseInt(args[1]) : 7;
		//������������Ϊ��������
		String message = args[2];
		//ת����bytes[]
		byte[] msgBytes = message.getBytes();
		
		//����һ���ŵ�������Ϊ������ģʽ
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		//��������˷�������
		if(!socketChannel.connect(new InetSocketAddress(server, port))){
			
			//���ϵ���ѯ����״̬��ֱ���������
			while(!socketChannel.finishConnect()){
				
				//�ڵȴ�ʱ�������ִ�����������Գ�ַ�������IO���첽����
				//����Ϊ����ʾ�÷�����ʹ�ã�ֻ��һֱ��ӡ
				System.out.println("wait for connecting,to do other things");
			}
			
			//�ֱ�ʵ������д������
			ByteBuffer sendBuf = ByteBuffer.wrap(msgBytes);
			ByteBuffer readBuf = ByteBuffer.allocate(msgBytes.length);
			
			//���յ��ܵ��ֽ���
			int totalBytesRcvd = 0;
			//ÿһ�ε���read()�����ӵ����ֽ���
			int bytesRcvd;
			//ѭ�����պͷ���
			while(totalBytesRcvd < msgBytes.length){
				//�жϵ�ǰͨ���еĻ������Ƿ���ʣ���ֽڣ�������д��ͨ��
				//����
				if(sendBuf.hasRemaining()){
					//ͨ���������������ݳ��ȥ
					socketChannel.write(sendBuf);
				}
				//����
				if((bytesRcvd = socketChannel.read(readBuf)) == -1){
					throw new SocketException("Connection closed prematurely");
				}
				totalBytesRcvd += bytesRcvd;
				//�ڵȴ�ʱ�������ִ�����������Գ�ַ�������IO���첽����
				//����Ϊ����ʾ�÷�����ʹ�ã�ֻ��һֱ��ӡ
				System.out.println("wait for reading or writing,to do other things");
				
			}
			System.out.println("Received: " + new String(readBuf.array(),0,totalBytesRcvd));
			socketChannel.close();
		}
		
		
	
	}
	
}

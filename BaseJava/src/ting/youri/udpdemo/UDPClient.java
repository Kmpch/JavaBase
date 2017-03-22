package ting.youri.udpdemo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
	
	//���ý������ݵĳ�ʱʱ��
	private static final int TIMEOUT = 	5000;
	//�����ط����ݵ�������
	private static final int MAXNUM = 5;
	
	public static void main(String[] args) throws Exception {
		
		String  str_send = "Hello UDPserver";
		byte[] buf = new byte[1024];
		
		//�ͻ�����9000�˿ڼ������յ�������
		DatagramSocket ds = new DatagramSocket(9000);
		//��ȡ����ip��ַ
		InetAddress loc = InetAddress.getLocalHost();
		
		//���巢������DatagramPacket
		//�������ݣ�ͬʱ����ָ�����͵�ip�Ͷ˿�
		DatagramPacket dp_send = new DatagramPacket(str_send.getBytes(), str_send.length(),
								loc,3000);
		
		//����������ݵ�DatagramPacketʵ��
		DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
		/**
		 * �������ݵ�����3000�˿�
		 * 
		 */
		ds.setSoTimeout(TIMEOUT); //���ý�������ʱ�������ʱ��
		int tries = 0;  //�ط����ݵĴ���
		boolean receiveResponse = false; //�Ƿ���յ����ݵı�־λ
		
		//ֱ���������ݣ������ط������ﵽԤ��ֵ�����Ƴ�ѭ��
		while(!receiveResponse && tries < MAXNUM){
			
			//��������
			ds.send(dp_send);
			try {
				//���մӷ������˷��ͻ���������
				ds.receive(dp_receive);
			} catch (Exception e) {
			}
			
		}
		
		
		
	}
	
}

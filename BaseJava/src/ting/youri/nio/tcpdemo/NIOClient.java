package ting.youri.nio.tcpdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
	
	//ͨ��������
	private Selector selector;
	
	/**
	 * NIO �ͻ��˳�ʼ��
	 * ���һ��Socketͨ�������Ը�ͨ����һЩ��ʼ������
	 * 1.��ȡһ��ͨ���������ͨ��ɶҲû�У�ֻ��ͨ��SocketChannel.open()��ͨһ��������ͨ������
	 * 2.����ͨ��֮�����ǿ�ʼ��ͨ���������ã�
	 * 		1������ͨ��Ϊ������
	 * 		2���Կͻ��˰���Ӧ�ķ��������ӣ�ip�Ͷ˿ڣ�
	 * 3. ͨ��Selector����һ��ͨ��������
	 * 4. ��ͨ������ע�ᣬ����Ӧ��ͨ����������ָ��ͨ����״̬
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public void initClient(String ip,int port) throws IOException{
		//���һ��Sockeͨ��
		SocketChannel channel  = SocketChannel.open();
		//����ͨ��Ϊ������
		channel.configureBlocking(false);
		//�ͻ������ӷ���������ʵ����ִ�в�û��ʵ�����ӣ���Ҫ��listen()������
		//����channel.finishConnect();�����������
		channel.connect(new InetSocketAddress(ip, port));
		//��һ��ͨ��������һ��ͨ���������͹���״̬
		this.selector = Selector.open();
		//ע��ͨ��������ͨ����ͨ�����������а�
		channel.register(selector, SelectionKey.OP_CONNECT);
	}
	
	public void listen() throws IOException{
		//��ѯ����selector
		while(true){
			/**
			 * Selects a set of keys whose corresponding channels are ready for I/O operations. 
			   This method performs a blocking selection operation. 
			   It returns only after at least one channel is selected, 
			   this selector's wakeup method is invoked, 
			   or the current thread is interrupted, whichever comes first. 
			 */
			selector.select();
			//��ͨ��������selector��key�еĵ�����ȡ
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				
				SelectionKey key = ite.next();
				//ɾ����ѡ�е�key,�Է��ظ�����
				ite.remove();
				//�жϵ�ǰͨ����������״̬
				if(key.isConnectable()) {
					//ͨ��ͨ����������ȡ��Ӧ��ͨ��
					SocketChannel channel = (SocketChannel) key.channel();
					//����������ӣ����������
					if(channel.isConnectionPending()) {
						//�����Ľ�������
						channel.finishConnect();
					}
					//��ͨ�����óɷ�����
					channel.configureBlocking(false);
					
					//��������Ը�����˷�����Ϣ
					channel.write(ByteBuffer.wrap(
								new String("�����˷���һ����Ϣ").getBytes()));
					//�ںͷ�������ӳɹ�֮��Ϊ�˿��Խ��յ�����˵���Ϣ
					//��Ҫ��ͨ�����ö���Ȩ��
					channel.register(this.selector, SelectionKey.OP_READ);
				} else if(key.isReadable()){
					read(key);
				}
				
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
				//��ȡ�������Ϣ���õ��¼�������Socketͨ��
				SocketChannel channel = (SocketChannel) key.channel();
				//������ȡ�Ļ�����
				ByteBuffer buffer = ByteBuffer.allocate(10);
				channel.read(buffer);
				byte[] data = buffer.array();
				String msg = new String(data).trim();
				System.out.println("�ͻ��˽��յ���Ϣ��" + msg);
				ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
				//����Ϣ���ط�������
				channel.write(outBuffer);
		
	}
	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient();
		client.initClient("localhost", 8000);
		client.listen();
	}
	
}

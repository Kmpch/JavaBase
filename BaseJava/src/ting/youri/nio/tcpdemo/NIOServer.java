package ting.youri.nio.tcpdemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * nio ������ 
 * @author youri
 */
public class NIOServer {
	//ͨ��������
	private Selector selector;
	public void initServer(int port) throws IOException{
		//���һ��ServerSocketͨ��
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// ����ͨ��Ϊ������
		serverChannel.configureBlocking(false);
		//����ͨ����Ӧ��ServerSocketָ����Ӧ��port�˿�
		serverChannel.socket().bind(new InetSocketAddress(port));
		//���һ��ͨ��������
		this.selector = Selector.open();
		//��ͨ���������͸�ͨ���󶨣���Ϊ��ͨ��ע��SelectionKey.OP_ACCEPT�¼���ע����¼���
		//�����¼�����ʱ��selector.select()�᷵�أ�������¼�û��selector.select()��һֱ����
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	public void listen() throws IOException{
		System.out.println("����������ɹ���");
		//��ѯ����selector
		while(true){
			//��ע����¼�����ʱ���������أ����򣬸÷�����һֱ����
			selector.select();
			//���selector��ѡ�еĵ�������ѡ�е���Ϊע����¼�
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				SelectionKey key = ite.next();
				//ɾ����ѡ��key,�Է��ظ�����
				ite.remove();
				//�ͻ������������¼�
				if (key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					//��ȡ�Ϳͻ������ӵ�ͨ��
					SocketChannel channel = server.accept();
					//���óƷ�����
					channel.configureBlocking(false);
					//��������Ը��ͻ��˷�����Ϣ
					channel.write(ByteBuffer.wrap(
								new String("��ͻ��˷���һ����Ϣ").getBytes()));
					//�ںͿͻ������ӳɹ�֮��Ϊ�˿��Խ��տͻ�����Ϣ����Ҫ��ͨ�����ö���Ȩ�ޡ�
					channel.register(this.selector, SelectionKey.OP_READ);
					
				} else if(key.isReadable()){
					read(key);
				}
			}
			
			
		}
	}

	private void read(SelectionKey key) throws IOException {
		
		//�������ɶ���Ϣ���õ��¼�������Socketͨ��
		SocketChannel channel = (SocketChannel) key.channel();
		//������ȡ�Ļ�����
		ByteBuffer buffer = ByteBuffer.allocate(10);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("���������յ���Ϣ��" + msg);
		ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		//����Ϣ���͵��ͻ���
		channel.write(outBuffer);
	}
	
	/**
	 * ��������˲���
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.initServer(8000);
		server.listen();
	}
	
}

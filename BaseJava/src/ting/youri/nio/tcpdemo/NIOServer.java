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
 * nio 服务器 
 * @author youri
 */
public class NIOServer {
	//通道管理器
	private Selector selector;
	public void initServer(int port) throws IOException{
		//获得一个ServerSocket通道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 设置通道为非阻塞
		serverChannel.configureBlocking(false);
		//将该通道对应的ServerSocket指定对应的port端口
		serverChannel.socket().bind(new InetSocketAddress(port));
		//获得一个通道管理器
		this.selector = Selector.open();
		//将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，注册该事件后，
		//当该事件到达时，selector.select()会返回，如果该事件没有selector.select()会一直阻塞
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	public void listen() throws IOException{
		System.out.println("服务端启动成功！");
		//轮询访问selector
		while(true){
			//当注册的事件到达时，方法返回；否则，该方法会一直阻塞
			selector.select();
			//获得selector中选中的迭代器，选中的项为注册的事件
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				SelectionKey key = ite.next();
				//删除已选的key,以防重复处理
				ite.remove();
				//客户端请求连接事件
				if (key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					//获取和客户端连接的通道
					SocketChannel channel = server.accept();
					//设置称非阻塞
					channel.configureBlocking(false);
					//在这里可以给客户端发送信息
					channel.write(ByteBuffer.wrap(
								new String("向客户端发送一条信息").getBytes()));
					//在和客户端连接成功之后，为了可以接收客户端信息，需要给通道设置读的权限。
					channel.register(this.selector, SelectionKey.OP_READ);
					
				} else if(key.isReadable()){
					read(key);
				}
			}
			
			
		}
	}

	private void read(SelectionKey key) throws IOException {
		
		//服务器可读消息：得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		//创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(10);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("服务器接收的消息：" + msg);
		ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		//将信息回送到客户端
		channel.write(outBuffer);
	}
	
	/**
	 * 启动服务端测试
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.initServer(8000);
		server.listen();
	}
	
}

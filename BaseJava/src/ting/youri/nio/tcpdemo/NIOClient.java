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
	
	//通道管理器
	private Selector selector;
	
	/**
	 * NIO 客户端初始化
	 * 获得一个Socket通道，并对该通道做一些初始化工作
	 * 1.获取一个通道对象，这个通道啥也没有，只是通过SocketChannel.open()开通一个，返回通道对象
	 * 2.有了通道之后，咱们开始对通道进行设置，
	 * 		1）设置通道为非阻塞
	 * 		2）对客户端绑定相应的服务器连接（ip和端口）
	 * 3. 通过Selector开启一个通道管理器
	 * 4. 对通道进行注册，绑定相应的通道管理器和指定通道的状态
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public void initClient(String ip,int port) throws IOException{
		//获得一个Socke通道
		SocketChannel channel  = SocketChannel.open();
		//设置通道为非阻塞
		channel.configureBlocking(false);
		//客户端连接服务器，其实方法执行并没有实现连接，需要在listen()方法中
		//调用channel.finishConnect();才能完成连接
		channel.connect(new InetSocketAddress(ip, port));
		//打开一个通道，返回一个通道管理器和管理状态
		this.selector = Selector.open();
		//注册通道，并将通道和通道管理器进行绑定
		channel.register(selector, SelectionKey.OP_CONNECT);
	}
	
	public void listen() throws IOException{
		//轮询访问selector
		while(true){
			/**
			 * Selects a set of keys whose corresponding channels are ready for I/O operations. 
			   This method performs a blocking selection operation. 
			   It returns only after at least one channel is selected, 
			   this selector's wakeup method is invoked, 
			   or the current thread is interrupted, whichever comes first. 
			 */
			selector.select();
			//对通道管理器selector的key中的迭代获取
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				
				SelectionKey key = ite.next();
				//删除以选中的key,以防重复处理
				ite.remove();
				//判断当前通道管理器的状态
				if(key.isConnectable()) {
					//通过通道管理器获取相应的通道
					SocketChannel channel = (SocketChannel) key.channel();
					//如果正在连接，则完成连接
					if(channel.isConnectionPending()) {
						//真正的进行连接
						channel.finishConnect();
					}
					//将通道设置成非阻塞
					channel.configureBlocking(false);
					
					//在这里可以给服务端发送信息
					channel.write(ByteBuffer.wrap(
								new String("像服务端发送一条消息").getBytes()));
					//在和服务端连接成功之后，为了可以接收到服务端的消息
					//需要给通道设置读的权限
					channel.register(this.selector, SelectionKey.OP_READ);
				} else if(key.isReadable()){
					read(key);
				}
				
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
				//读取服务端消息：得到事件发生的Socket通道
				SocketChannel channel = (SocketChannel) key.channel();
				//创建读取的缓冲区
				ByteBuffer buffer = ByteBuffer.allocate(10);
				channel.read(buffer);
				byte[] data = buffer.array();
				String msg = new String(data).trim();
				System.out.println("客户端接收的消息：" + msg);
				ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
				//将消息返回服务器端
				channel.write(outBuffer);
		
	}
	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient();
		client.initClient("localhost", 8000);
		client.listen();
	}
	
}

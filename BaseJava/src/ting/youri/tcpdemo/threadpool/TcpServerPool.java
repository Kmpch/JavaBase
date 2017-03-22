package ting.youri.tcpdemo.threadpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TcpServerPool {
	
	// 管理线程的线程池
	private ThreadPoolExecutor threadPool;
	//socket队列
	private LinkedBlockingQueue<Socket> socketQueue;
	
	public TcpServerPool() {
		
		//线程池的大小
		int corePoolSize = 5;
		//初始化线程池
		threadPool = new ThreadPoolExecutor(
					corePoolSize, corePoolSize, 10l, 
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2000));
		//初始化socket队列
		socketQueue = new LinkedBlockingQueue<Socket>();
	}
	
	//处理整个服务端连接整体逻辑的服务
	public void connect(){
		//初始化服务端
		TcpServer tcpServer = new TcpServer();
		ServerSocket serverSocket = tcpServer.init(23006);
		//一直循环,等待客户端连接
		while(true){
			try {
				Socket s = serverSocket.accept();
				Thread t = new Thread(new SocketToQueueTask(s));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//处理socket
			try {
				Socket client = socketQueue.poll(10,TimeUnit.SECONDS);
				if(client == null){
					break;
				}
				//将所有的取线程交给线程池来处理
				threadPool.submit(new SocketExcute(client));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//将socket插入队列中
	class SocketToQueueTask implements Runnable{

		private Socket socket;
		
		public SocketToQueueTask(Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			socketQueue.offer(socket);
		}
		
	}
	
	//处理客户端与服务器的连接
	class SocketExcute implements Runnable{

		private Socket client;
		public SocketExcute(Socket client){
			this.client = client;
		}
		@Override
		public void run() {
			try {
				//读入客户端发送过来的流
				BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(client.getInputStream()));
				String line = "";
				int l = -1;
				while((l = bufferedReader.read()) != -1){
					line += (char)l;
				}
				System.out.println(line);
				//如果数据不为空，则向客户端发送成功发送消息
				//如果数据为空，则发送客户端发送失败消息
				OutputStream outputStream = client.getOutputStream();
				PrintStream ps = new PrintStream(outputStream);
				if(line.equals("")){
					ps.write("error".getBytes());
				}else{
					String name = line.split("-")[0];
					ps.write(("hello,"+ name +" ,success connect").getBytes());
					ps.flush();
				}
				ps.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		TcpServerPool tcpServer = new TcpServerPool();
		tcpServer.connect();
	}
}






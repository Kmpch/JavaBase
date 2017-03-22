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
	
	// �����̵߳��̳߳�
	private ThreadPoolExecutor threadPool;
	//socket����
	private LinkedBlockingQueue<Socket> socketQueue;
	
	public TcpServerPool() {
		
		//�̳߳صĴ�С
		int corePoolSize = 5;
		//��ʼ���̳߳�
		threadPool = new ThreadPoolExecutor(
					corePoolSize, corePoolSize, 10l, 
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2000));
		//��ʼ��socket����
		socketQueue = new LinkedBlockingQueue<Socket>();
	}
	
	//����������������������߼��ķ���
	public void connect(){
		//��ʼ�������
		TcpServer tcpServer = new TcpServer();
		ServerSocket serverSocket = tcpServer.init(23006);
		//һֱѭ��,�ȴ��ͻ�������
		while(true){
			try {
				Socket s = serverSocket.accept();
				Thread t = new Thread(new SocketToQueueTask(s));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//����socket
			try {
				Socket client = socketQueue.poll(10,TimeUnit.SECONDS);
				if(client == null){
					break;
				}
				//�����е�ȡ�߳̽����̳߳�������
				threadPool.submit(new SocketExcute(client));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//��socket���������
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
	
	//����ͻ����������������
	class SocketExcute implements Runnable{

		private Socket client;
		public SocketExcute(Socket client){
			this.client = client;
		}
		@Override
		public void run() {
			try {
				//����ͻ��˷��͹�������
				BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(client.getInputStream()));
				String line = "";
				int l = -1;
				while((l = bufferedReader.read()) != -1){
					line += (char)l;
				}
				System.out.println(line);
				//������ݲ�Ϊ�գ�����ͻ��˷��ͳɹ�������Ϣ
				//�������Ϊ�գ����Ϳͻ��˷���ʧ����Ϣ
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






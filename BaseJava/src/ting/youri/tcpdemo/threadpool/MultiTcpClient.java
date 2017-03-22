package ting.youri.tcpdemo.threadpool;

public class MultiTcpClient {
	
	public static void main(String[] args) {
		for(int i = 0; i<1000; i++){
			TcpClient tcpClient = new TcpClient("client" + i);
			Thread t = new Thread(new MultiClientTask(tcpClient));
			t.start();
		}
	}
}
class MultiClientTask implements Runnable{
	private TcpClient client;
	
	public MultiClientTask(TcpClient client){
		this.client = client;
	}
	@Override
	public void run() {
		client.init();
	}
}

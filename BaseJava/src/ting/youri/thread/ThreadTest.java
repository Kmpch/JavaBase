package ting.youri.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 这里主要测试Callable
 * @author kmpch
 *
 */
public class ThreadTest {

	public static void main(String[] args) {
		
		for (int i = 0; i < 10; i++) {
			ThreadRunnable tr = new ThreadRunnable(i);
			FutureTask<Integer> ft = new FutureTask<>(tr);
			new Thread(ft).start();
			try {
				System.out.println(ft.get());;
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
class ThreadRunnable implements Callable<Integer> {

	private int i;
	public ThreadRunnable(int i) {
		this.i = i;
	}
	@Override
	public Integer call() throws Exception {
		
		return i*10;
	}
	
}

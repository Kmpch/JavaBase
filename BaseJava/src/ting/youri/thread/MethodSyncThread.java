package ting.youri.thread;

// 如果是同步方法，则分静态和非静态两种。
// 静态方法则一定会同步，非静态方法需在单例模式才生效，推荐用静态方法(不用担心是否单例)。
public class MethodSyncThread implements Runnable {
    private Integer key = 0;

    // 此示范为非静态方法同步
    public synchronized Integer getKey() {
        key++;

        return key;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ":" + getKey());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        // 非静态方法同步，需要启动单例模式
    	MethodSyncThread st = new MethodSyncThread();
        for (int i = 0; i < 10; i++) {
        	
            new Thread(st, "Thread" + i).start();
        }
    }
}
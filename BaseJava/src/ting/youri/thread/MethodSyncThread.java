package ting.youri.thread;

// �����ͬ����������־�̬�ͷǾ�̬���֡�
// ��̬������һ����ͬ�����Ǿ�̬�������ڵ���ģʽ����Ч���Ƽ��þ�̬����(���õ����Ƿ���)��
public class MethodSyncThread implements Runnable {
    private Integer key = 0;

    // ��ʾ��Ϊ�Ǿ�̬����ͬ��
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
        // �Ǿ�̬����ͬ������Ҫ��������ģʽ
    	MethodSyncThread st = new MethodSyncThread();
        for (int i = 0; i < 10; i++) {
        	
            new Thread(st, "Thread" + i).start();
        }
    }
}
package ting.youri.nio.filedemo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NIOFile {
	
	private String inputFilePath;
	private String outFilePath;
	
	public NIOFile(String inputFilePath, String outFilePath) {
		this.inputFilePath = inputFilePath;
		this.outFilePath = outFilePath;
	}
	public void fileCopy() throws Exception{
		
		//1.׼��������ȷ���ļ�λ�ã�������ֱ�Ӳ������ļ�ת�����ַ�������ʽ
		FileInputStream fis = new FileInputStream(new File(inputFilePath));
		FileOutputStream fout = new FileOutputStream(new File(outFilePath));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//2.�����ļ�����ܵ������ļ����ͨ��(Channel��Buffer���ڶ�д��ʱ��ŷ�����"����"����)
		//�����ļ���ȡͨ��
		FileChannel inputChannel = fis.getChannel();
		FileChannel outChannel = fout.getChannel();
		
		/*//���б��������
		Charset charSet = Charset.forName("utf-8");
		CharsetDecoder decoder = charSet.newDecoder();
		CharsetEncoder encoder = charSet.newEncoder();*/
		
		//��ʼ�����ļ����ݹ���
		while(true){
			//׼����buffer��д������
			buffer.clear();
		/*	//�����ַ�����
			CharBuffer afterDecodeBuffer = decoder.decode(buffer);
			ByteBuffer afterEncodeBuffer = encoder.encode(afterDecodeBuffer);
			//���ݾ��������Ժ��ݴ滺����
			int t = inputChannel.read(afterEncodeBuffer);*/
			int t = inputChannel.read(buffer);
			System.out.println(t);
			if(t == -1){
				break;
			}
		/*	afterEncodeBuffer.flip();
			outChannel.write(afterEncodeBuffer);*/
			buffer.flip();
			outChannel.write(buffer);
		}
	}
	public static void main(String[] args) {
		String inputStr = "D:\\�Ի�.txt";
		String outputStr = "E:\\�Ի�.txt";
		NIOFile file = new NIOFile(inputStr, outputStr);
		try {
			file.fileCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

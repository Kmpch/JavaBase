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
		
		//1.准备工作，确定文件位置，将不可直接操作的文件转换成字符流的形式
		FileInputStream fis = new FileInputStream(new File(inputFilePath));
		FileOutputStream fout = new FileOutputStream(new File(outFilePath));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//2.创建文件输入管道，和文件输出通道(Channel和Buffer是在读写的时候才发生的"连接"动作)
		//创建文件读取通道
		FileChannel inputChannel = fis.getChannel();
		FileChannel outChannel = fout.getChannel();
		
		/*//进行编解码配置
		Charset charSet = Charset.forName("utf-8");
		CharsetDecoder decoder = charSet.newDecoder();
		CharsetEncoder encoder = charSet.newEncoder();*/
		
		//开始进行文件备份工作
		while(true){
			//准备向buffer中写入数据
			buffer.clear();
		/*	//进行字符编码
			CharBuffer afterDecodeBuffer = decoder.decode(buffer);
			ByteBuffer afterEncodeBuffer = encoder.encode(afterDecodeBuffer);
			//数据经过编码以后暂存缓存区
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
		String inputStr = "D:\\对话.txt";
		String outputStr = "E:\\对话.txt";
		NIOFile file = new NIOFile(inputStr, outputStr);
		try {
			file.fileCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

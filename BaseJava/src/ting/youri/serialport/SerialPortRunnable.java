package ting.youri.serialport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;
 
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
 
public class SerialPortRunnable implements Runnable, SerialPortEventListener {
    // 检测系统中可用的通讯端口类
    private CommPortIdentifier portId;
    // 枚举类型
    private Enumeration<CommPortIdentifier> portList;
    // RS232串口
    private SerialPort serialPort;
    // 输入输出流
    private InputStream inputStream;
    private OutputStream outputStream;
    // 保存串口返回信息
    private String test = "";
    //波特率
    private String baudRate;
    //发送的命令
    private String cmd;
    //当前使用的端口
    private String com;
   
    // 构造函数
    public SerialPortRunnable(String baudRate,String com,String cmd){
    	this.baudRate = baudRate;
    	this.cmd = cmd;
    	this.com = com;
    	System.out.println("端口: " + com +" 命令： " + cmd );
    }
    // 初始化串口
    @SuppressWarnings("unchecked")
    public void init() {
        // 获取系统中所有的通讯端口
        portList = CommPortIdentifier.getPortIdentifiers();
        // 循环通讯端口
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            // 判断是否是串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (com.equals(portId.getName())) {
                    // 打开串口
                    try {
                        // open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
                        serialPort = (SerialPort) portId.open(Object.class.getSimpleName(), 2000);
                        //实例化输入流
                        inputStream = serialPort.getInputStream();
                        // 设置串口监听
                        serialPort.addEventListener(this);
                        // 设置串口数据时间有效(可监听)
                        serialPort.notifyOnDataAvailable(true);
                        // 设置串口通讯参数
                        // 波特率，数据位，停止位和校验方式
                        // 波特率2400,偶校验
                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, //
                                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
 
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
 
                }
            }
        }
    }
    public void closeSerialPort() {
        this.serialPort.close();
    }
    // 实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
        case SerialPortEvent.BI: // 通讯中断
        case SerialPortEvent.OE: // 溢位错误
        case SerialPortEvent.FE: // 帧错误
        case SerialPortEvent.PE: // 奇偶校验错误
        case SerialPortEvent.CD: // 载波检测
        case SerialPortEvent.CTS: // 清除发送
        case SerialPortEvent.DSR: // 数据设备准备好
        case SerialPortEvent.RI: // 响铃侦测
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 输出缓冲区已清空
            break;
        case SerialPortEvent.DATA_AVAILABLE: // 有数据到达
            readComm();
            break;
        default:
            break;
        }
    }
 
    // 读取串口返回信息
    public void readComm() {
        byte[] readBuffer = new byte[1024];
        try {
            inputStream = serialPort.getInputStream();
            // 从线路上读取数据流
            int len = 0;
            while ((len = inputStream.read(readBuffer)) != -1) {
                System.out.println("实时反馈：" + new String(readBuffer, 0, len).trim() + new Date());
                test += new String(readBuffer, 0, len).trim();
                break;
            }
            System.out.println(test + " ");
            //closeSerialPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //向串口发送数据
    public void sendMsg(String information){
        try {
            //实例化输出流
            outputStream = serialPort.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.write(hex2byte(information));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
 
    public void run() {
        init();
        //sendMsg(cmd);
        readComm();
        closeSerialPort();
    }
    //将字符串转化成16进制码
    public static byte[] hex2byte(String hex) {
        String digital = "0123456789ABCDEF";
        String hex1 = hex.replace(" ", "");
        char[] hex2char = hex1.toCharArray();
        byte[] bytes = new byte[hex1.length() / 2];
        byte temp;
        for (int p = 0; p < bytes.length; p++) {
            temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
            temp += digital.indexOf(hex2char[2 * p + 1]);
            bytes[p] = (byte) (temp & 0xff);
        }
        return bytes;
    }
}
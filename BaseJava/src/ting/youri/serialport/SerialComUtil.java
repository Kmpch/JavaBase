package ting.youri.serialport;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class SerialComUtil {

	  /**
     * @Description:获取所有可用的串口集合
     */
    @SuppressWarnings("unchecked")
    protected static  HashSet<CommPortIdentifier> getAvailableSerialPorts() {
    	//定义一个Set对象
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        //获取一个枚举类型
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        
        //依次循环列出枚举类型
        while (portList.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) portList.nextElement();
            
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    // open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
                    /*
                     * open方法打开通讯端口，获得一个CommPort对象，它使程序独占端口。
                     * 如果端口正被其他应用程序占用，将使用CommPortOwnershipListener事件机制
                     * 传递一个PORT_OWNERSHIP_REQUESTED事件。
                     * 每个端口都关联一个InputStream和一个OutputStream,如果端口是用
                     * open方法打开的，那么任何的getInputStream都将返回相同的数据流对象，除非 有close被调用。
                     */
                    CommPort thePort = com.open(Object.class.getSimpleName(), 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    // 不可用串口
                    System.out.println("Port, " + com.getName() + ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " + com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }
    /**
     * 获取当前的串口，并通过对比配置文件中的串口配置,进行过滤
     * @return
     */
    public static List<String> listPorts() {
    	List<String> serialPosts = new ArrayList<String>();
        HashSet<CommPortIdentifier> portSet = getAvailableSerialPorts();
       /* PropertiesUtil pro = new PropertiesUtil("distributor.properties");
        String portNums  = pro.readValue("distributor.portnums");*/
        /*if(CommonUtil.isValid(portNums)){
        	int portNum = Integer.parseInt(portNums);
        	for(int i = 1 ; i<= portNum; i++){*/
		for(CommPortIdentifier comm : portSet) {
				serialPosts.add(comm.getName());
	        }
        /*	}
        }*/
        
		return serialPosts;
    }
    /**
     * @Description:获取通信端口类型名称
     * @author:Lu
     * @date:2015-8-29 上午11:35:32
     */
    public static String getPortTypeName(int portType) {
        switch (portType) {
        case CommPortIdentifier.PORT_I2C:
            return "I2C";
        case CommPortIdentifier.PORT_PARALLEL: // 并口
            return "Parallel";
        case CommPortIdentifier.PORT_RAW:
            return "Raw";
        case CommPortIdentifier.PORT_RS485: // RS485端口
            return "RS485";
        case CommPortIdentifier.PORT_SERIAL: // 串口
            return "Serial";
        default:
            return "unknown type";
        }
    }
    
    public static void main(String[] args) {
    	List<String> listPorts = SerialComUtil.listPorts();
		while(true){
			Thread t = new Thread(new SerialPortRunnable("", listPorts.get(0), ""));
			t.run();
		}
	}
    
    
    //单台设备执行指令方法
    //1 设备类型 --->  投影 (直接)和其他（电源控制）controller 判断
    //2.投影的话--> port ,COM  ;其他---> port COM,POWER
    //
    //多台设备
    //1 设备类型 --->  投影 (直接)和其他（电源控制）controller 分类放进list当中
    //投影List (port,COM) 其他list(port,COM,power)
    //(list1,list2)
    
    /**
     * 
     * @param cmd  命令  6种
     * @param port
     * @param com
     * @return
     */
   /* public static boolean excuteSingleProjector(Projector pro,String cmd){
    	boolean flag = false;
    	//电脑发送指定的端口
    	String serialPort = "";
    	//起始指令
		String startPart = "";
    	//通道
    	String comPart = "";
    	//主分配器通道
    	String comPart_1 ="";
    	//校验
    	String checkPart = "";
    	//8路串口分配器波特率
    	String uart8BaudRate = "";
    	//投影仪的波特率
    	String projectorBaudRate = "";
    	//第一次封装的数据长度
    	String dataLength_1 = "";
    	//第二次封装的数据长度
    	String dataLength_2 = "";
    	//投影仪数据
    	//获取正在使用投影仪品牌，获取当前命令的16进制码字符串
    	String projectorCmd = "";
    	//第一次封装命令
    	String finalCmd_1 = "";
    	//第二次封装命令
    	String finalCmd_2 = "";
    	if(CommonUtil.isValid(pro.getCom()) && CommonUtil.isValid(cmd) && CommonUtil.isValid(pro.getConnector())){
	    	//读取配置文件
	    	//投影仪
			PropertiesUtil deviceUtil = new PropertiesUtil("projector.properties");
			//八路串口分配器
			PropertiesUtil uart8Util = new PropertiesUtil("uart8cmd.properties");
			//8路串口分配器启始指令
			startPart = uart8Util.readValue("uart8.start".trim());
			//8路串口校验位
			checkPart = uart8Util.readValue(("uart8." + uart8Util.readValue("uart8.currentcheck")).trim());
			//8路串口波特率
			uart8BaudRate = uart8Util.readValue("uart8." + uart8Util.readValue(("uart8.baudrate".trim())));
			//第一次封装命令
			//投影仪名称数组
			String[] projectorName = deviceUtil.readValue("projector.currentuse").split(",");
			for(String name : projectorName){
				if(name.equals(pro.getName())){
					projectorCmd = deviceUtil.readValue( (name + "." + cmd).trim());
					projectorBaudRate = deviceUtil.readValue((name + ".baudrate").trim());
				}
			}
			//数据长度_1
			dataLength_1 = decimalToHex(projectorCmd.length()/2);
			//第一次封装的通道
			comPart = uart8Util.readValue(("uart8." + pro.getConnector().toLowerCase()).trim());
			finalCmd_1 = startPart + comPart + checkPart + projectorBaudRate + dataLength_1 + projectorCmd;
			
			//第二次封装命令
			comPart_1 = uart8Util.readValue(("uart8." + pro.getCom().toLowerCase().replace("com", "connector")).trim());
			//数据长度_2
			dataLength_2 = decimalToHex(finalCmd_1.length()/2);
			finalCmd_2 = startPart + comPart_1 + checkPart + uart8BaudRate + dataLength_2 + finalCmd_1; 
			List<String> listPorts = SerialComUtil.listPorts();
			if(listPorts.size() > 0){
				SerialPortRunnable serialPortRunnable = new SerialPortRunnable(uart8BaudRate,listPorts.get(0),finalCmd_2);
				Thread t = new Thread(serialPortRunnable);
				t.start();
				flag = true;
			}else{
				flag = false;
				System.out.println("serialPort is null");
			}
			
    	}
    	return flag;
    }*/
    //将整数转换成为四位的16进制数的字符串
    public static String decimalToHex(int decimal){
        String hex = "";
        
        while(decimal != 0){
        	
        	char m;
            int hexValue = decimal % 16;
            if(hexValue <= 9 && hexValue >= 0){
            	m = (char)(hexValue + '0');
            }else{
            	m = (char)(hexValue - 10 + 'A');
            }
            hex = m + hex;
            decimal = decimal / 16;
        }
    	switch(hex.length()){
    		case 1:
    			hex = "000" +hex;
    			break;
    		case 2:
    			hex = "00" + hex;
    			break;
    		case 3:
    			hex = "0" + hex;
    			break;
    		case 4 :
    			hex = hex + "";
    			break;
    		default :
    			hex = "FFFF";
    			break;
    	}
        	
        return hex;
    }
   /**
     * 
     * @param cmd 命令 open/close
     * @param port 电脑端的串口 COM1 ,COM2
     * @param com  串口分配器对应的COM1,COM2,COM3..COM8
     * @param idCode 对应电源的16进制编码 00-99
     * @param power 对应电源控制通道 POWER1-POWER8
     * @return
     */
   /* public static boolean excuteSinglePower(Power pow,String cmd){
    	
    	boolean flag = false;
    	*//**
    	 * 串口分配器封装指令
    	 *//*
    	//起始指令
		String startPart = "";
    	//通道_1
    	String comPart_1 = "";
    	//通道_2
    	String comPart_2 = "";
    	//校验
    	String checkPart = "";
    	//8路串口分配波特率
    	String uart8BaudRate = "";
    	//数据长度_1
    	String dataLength_1 = "";
    	//数据长度_2
    	String dataLength_2 = "";
    	//电源控制器16进制指令
    	String powerCmd = "";
    	//最后封装的命令
    	String finalCmd_1 = "";
    	String finalCmd_2 = "";
    	*//**
    	 * 电源指令部分
    	 *//*
    	//命令开始位
    	String powerStart = "4C1C";
    	//命令总长度和指令号
    	String powerLen = "0701";
    	//命令结束位
    	String powerEnd = "23";
    	//标志是那个端口
    	String powerCon = "";
    	//电源控制器标志的idCode
    	String idCode = "06";
    	//
    	if(CommonUtil.isValid(pow.getCom()) && CommonUtil.isValid(cmd) && CommonUtil.isValid(pow.getConnector()) 
    								&& CommonUtil.isValid(pow.getPower())){
	    	//读取配置文件
			//八路串口分配器
			PropertiesUtil uart8Util = new PropertiesUtil("uart8cmd.properties");
			PropertiesUtil powerUtil = new PropertiesUtil("power.properties");
			startPart = uart8Util.readValue("uart8.start".trim());
			comPart_1 = uart8Util.readValue(("uart8." + pow.getConnector().toLowerCase()).trim());
			checkPart = uart8Util.readValue(("uart8." + uart8Util.readValue("uart8.currentcheck")).trim());
			uart8BaudRate = uart8Util.readValue(("uart8.baudrate".trim()));
			String mark = "";
			//拼接电源命令
			for(int i = 0 ; i< 8; i++){
				if(pow.getPower().equalsIgnoreCase("POWER"+(i+1))){
					mark = i + "";
				}
			}
			if(cmd.equalsIgnoreCase("open")){
				powerCon  = "0" + mark;
			}else if(cmd.equalsIgnoreCase("close")){
				powerCon = "8" + mark;
			}
			//电源控制命令
			powerCmd = powerStart + powerLen + idCode +  powerCon + powerEnd;
			dataLength_1 = decimalToHex(powerEnd.length()/2);
			finalCmd_1 = startPart + comPart_1 + checkPart + uart8BaudRate + dataLength_1 + powerCmd;
			comPart_2 = uart8Util.readValue(("uart8." + pow.getCom().toLowerCase().replace("com", "connector")).trim());
			dataLength_2 = decimalToHex(finalCmd_2.length()/2);
			finalCmd_2 = startPart + comPart_2 + checkPart + uart8BaudRate + dataLength_2 + finalCmd_1;
			List<String> listPorts = listPorts();
			if(listPorts.size() > 0){
				SerialPortRunnable serialPortRunnable = new SerialPortRunnable(uart8BaudRate,listPorts.get(0),finalCmd_2);
				Thread t = new Thread(serialPortRunnable);
				t.start();
				flag = true;
			}else {
				flag = false;
			}
			
    	}
    	return flag;
    }*/
    
    /*//全展厅的一键全开，全关
    public static boolean excuteAll(List<Projector> list, List<Power> powers,String cmd){
    	boolean flag = false;
    	if(list.size() > 0){
    		for(Projector pro: list){
    			excuteSingleProjector(pro,cmd);
    		}
    		
    	}
    	if(powers.size() > 0){
    		*//**
        	 * 串口分配器封装指令
        	 *//*
        	//起始指令
    		String startPart = "";
        	//通道
        	String comPart = "";
        	//校验
        	String checkPart = "";
        	//波特率
        	String baudRate = "";
        	//数据长度
        	String dataLength = "";
        	//电源控制器16进制指令
        	String powerCmd = "";
        	//最后封装的命令
        	String finalCmd = "";
        	*//**
        	 * 电源指令部分
        	 *//*
        	//命令开始位
        	String powerStart = "4C1C";
        	//命令总长度和指令号
        	String powerLen = "0702";
        	//命令结束位
        	String powerEnd = "23";
        	//开关指令
        	String powerOpenClose = "";
        	PropertiesUtil powerUtil = new PropertiesUtil("power.properties");
        	//八路串口分配器
			PropertiesUtil uart8Util = new PropertiesUtil("uart8cmd.properties");
			startPart = uart8Util.readValue("uart8.start".trim());
			checkPart = uart8Util.readValue(("uart8." + uart8Util.readValue("uart8.currentcheck")).trim());
			baudRate = uart8Util.readValue(("uart8.baudrate".trim()));
    		//统计所有的串口
    		HashSet<String> comSets = new HashSet<String>();
    		for(Power pow : powers){
    			comSets.add(pow.getCom());
    		}
    		HashSet<String> connectorSets = new HashSet<String>();
    		//根据串口去找串口分配器下的connector口
    		for(Power pow : powers){
    			for(String com : comSets){
    				if(pow.getCom().equalsIgnoreCase(com)){
    					connectorSets.add(pow.getCom());
    				}
    			}
    		}
    		for(String com : comSets){
    			for(String connector : connectorSets){
    				comPart = uart8Util.readValue(("uart8." + connector.toUpperCase()).trim());
    				String idCode = powerUtil.readValue(com + "." + connector);
    				//电源控制命令
    				if(cmd.equalsIgnoreCase("open")){
    					powerOpenClose = "00";
    				}else if(cmd.equals("close")){
    					powerOpenClose = "ff";
    				}
    				powerCmd = powerStart + powerLen + idCode +  powerOpenClose + powerEnd;
    				dataLength = "000" + powerEnd.length()/2;
    				finalCmd = startPart + comPart + checkPart + baudRate + dataLength + powerCmd;
    				SerialPortRunnable serialPortRunnable = new SerialPortRunnable(baudRate, com,finalCmd);
    				Thread t = new Thread(serialPortRunnable);
    				t.start();
    				flag = true;
    			}
    		}
    	}
    	
    	
    	return flag;
    }*/
    	
    //批量开关
    /*public static boolean excutePatch(List<Projector> list, List<Power> powers,String cmd){
    	boolean flag = false;
    	if(list.size() > 0){
    		for(Projector pro : list){
    			excuteSingleProjector(pro,cmd);
    		}
    		
    	}
    	if(powers.size() > 0){
    		for(Power pow : powers){
    			excuteSinglePower(pow,cmd);
    		}
    		flag = true;
    	}
    	return flag;
    }
    */
   /* @Test
    public void testPort() {
		List<String> listPorts = listPorts();
		System.out.println(listPorts.size());
		for(String s : listPorts){
			System.out.println(s);
		}
		
	}*/
}

package ting.youri.test;

public class IntegerToHex {
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
    
    public static void main(String[] args) {
		int num = 1800;
		System.out.println(decimalToHex(num));
	}
    
    
}

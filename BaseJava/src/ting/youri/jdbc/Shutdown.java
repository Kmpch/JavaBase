package ting.youri.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shutdown {
	
	private String username;
	private String password;
	private String url;
	private String driver;
	
	public Shutdown(String username,String password,String url,String driver) {
		this.username = username;
		this.password = password;
		this.url = url;
		this.driver = driver;
	}
	
	public int opShutdwon(String sql){
		int count = -1;
		Connection conn = getConnection();
		if(conn != null){
			try {
				Statement createStatement = conn.createStatement(); //创建执行静态sql的Statement对象
				count = createStatement.executeUpdate(sql); //执行插入数据库
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	public List<Integer> queryAllIds(String sql){
		List<Integer> ids = new ArrayList<Integer>();
		Connection conn = getConnection();
		try {
			Statement createStatement = conn.createStatement();
			ResultSet rs = createStatement.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("id");
				ids.add(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	//获取数据库连接
	public  Connection getConnection(){
		Connection con = null;
		try {
			Class.forName(driver);//指定连接类型
			con = DriverManager.getConnection(url, username, password);//获取连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	  public static Date getNowDate(String formatStr) {
    	  Date currentTime = new Date();
    	  SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
    	  String dateString = formatter.format(currentTime);
    	  Date currentTime_2 = new Date();
    	try {
			currentTime_2 = formatter.parse(dateString);
    	} catch (Exception e) {
			System.out.println("格式化时间失败");
			e.printStackTrace();
		}
    	  return currentTime_2;
    }
	public static void main(String[] args) {
		String querySql = "select * from client";
		//查询客户端id
		Shutdown st = new Shutdown("root", "123456", 
									"jdbc:mysql://localhost/gxgdbv20?characterEncoding=utf-8", 
									"com.mysql.jdbc.Driver");
		List<Integer> queryAllIds = st.queryAllIds(querySql);
		for(int id : queryAllIds) {
			String insertSql = "";
			if(id != 61) {
				insertSql = "INSERT INTO task(client_id,cmd,status)" +
						" values("+ id +", 'shutdown'" +
						"," + 0 +")";
				st.opShutdwon(insertSql);
			}
			
			
		}
	}
	
}

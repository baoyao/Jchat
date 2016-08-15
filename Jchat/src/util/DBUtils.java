package util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import bean.Message;
import bean.TbMsg;
import bean.User;

public class DBUtils {
	//public static final String BASE_PATH = DBUtils.class.getClassLoader().getResource(".").getPath().substring(1);//remove by bao
	public static final String BASE_PATH = "./";//add by bao
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	public  DBUtils(String dbPath){
			try {
				String path = BASE_PATH+dbPath;
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:"+path,null,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	private void insert(TbMsg msg){
		try {
			String insertSql = "insert into tb_msg (userName,hostName,macAddress,hostAddress,localUserName,localHostName,"
					+ " localHostAddress,localMacAddress,type,messageType,messageText,sendTime,receiveTime)  "
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(insertSql);
			ps.setString(1, msg.getUserName());
			ps.setString(2, msg.getHostName());
			ps.setString(3, msg.getMacAddress());
			ps.setString(4, msg.getHostAddress());
			ps.setString(5, msg.getLocalUserName());
			ps.setString(6, msg.getLocalHostName());
			ps.setString(7, msg.getLocalHostAddress());
			ps.setString(8, msg.getLocalMacAddress());
			ps.setString(9, msg.getType());
			ps.setString(10, msg.getMessageType());
			ps.setString(11, msg.getMessageText());
			ps.setString(12, msg.getSendTime());
			ps.setString(13, msg.getReceiveTime());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
			}
		}
	}
	
	/**
	 * 插入一条通讯信息的工具方法
	 * @param msg
	 */
	public static void insertMsg(TbMsg msg){
		DBUtils dbUtil = new DBUtils("data/userdata.db");
		dbUtil.insert(msg);
		dbUtil.close();
	}
	
	public static void checkMsgDB() throws IOException, SQLException{
		File dataFolder = new File(BASE_PATH+"data");
		if(!dataFolder.exists() || !dataFolder.isDirectory()){
			dataFolder.mkdir();
		}
		File dbFile = new File(BASE_PATH+"data/userdata.db");
		if(!dbFile.exists() || !dbFile.isFile()){ // 如果不存在或者存在但不是文件
			System.out.println("开始创建数据库..."+dbFile.getAbsolutePath());
			dbFile.createNewFile();
			DBUtils dbUtil = new DBUtils("data/userdata.db");
			String sql = "CREATE TABLE  tb_msg  ("
					+ "id  INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ " userName  VARCHAR2(30) NOT NULL, hostName  VARCHAR2(30) NOT NULL, "
					+ "macAddress  CHAR(17) NOT NULL, hostAddress  VARCHAR2(15) NOT NULL, "
					+ "localUserName  VARCHAR2(30) NOT NULL, "
					+ "localHostName  VARCHAR2(30) NOT NULL, localHostAddress  VARCHAR2(15) NOT NULL, "
					+ " localMacAddress  CHAR(17) NOT NULL,  type  CHAR(1) NOT NULL, "
					+ " messageType  CHAR(2) NOT NULL,  messageText  VARCHAR2(512), "
					+ " sendTime  VARCHAR2(19) NOT NULL,  receiveTime  VARCHAR2(19) NOT NULL);";
			Connection conn = dbUtil.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			dbUtil.close();
			System.out.println("数据库创建成功！");
		}
	}
	/**
	 * 将接收到的消息写入数据库（只写聊天信息和文件传送的记录）
	 * @param m 消息对象
	 * @param hostAddr 对方IP地址
	 */
	public static void writeReceivedMsgToDB(Message m,String hostAddr){
		TbMsg msg = new TbMsg();
		msg.setHostAddress(hostAddr);
		msg.setHostName(m.getHostName());
		msg.setUserName(m.getUserName());
		msg.setLocalHostAddress(Config.LOCAL_HOST_ADDR);
		msg.setLocalHostName(Config.LOCAL_HOST_NAME);
		msg.setLocalMacAddress(Config.LOCAL_MAC_ADDR);
		msg.setLocalUserName(Config.LOCAL_USER_NAME_DEFAULT);
		msg.setType("1"); //0表示发送的消息；1表示接收的消息
		msg.setMacAddress("Unknown");  //暂时没做，预留
		msg.setMessageText(m.getData());
		msg.setMessageType(m.getHead());
		msg.setSendTime(BasicUtils.formatDate(new Date(m.getSeq()),false));
		msg.setReceiveTime(BasicUtils.formatDate(new Date(), false));
		DBUtils.insertMsg(msg);
	}
	
	/**
	 * 将发送的消息写入数据库（只写聊天信息和文件传送记录）
	 * @param m 消息对象
	 * @param user 对方user对象
	 */
	public static void writeSentMsgToDB(Message m,User user){
		TbMsg msg = new TbMsg();
		msg.setHostAddress(user.getHostAddress()); 
		msg.setHostName(user.getHostName());   //通过查找该用户信息得到
 		msg.setUserName(user.getUserName());   //通过查找该用户信息得到
		msg.setLocalHostAddress(Config.LOCAL_HOST_ADDR); 
		msg.setLocalHostName(m.getHostName());
		msg.setLocalMacAddress(Config.LOCAL_MAC_ADDR);
		msg.setLocalUserName(m.getUserName());
		msg.setType("0"); //0表示发送的消息；1表示接收的消息
		msg.setMacAddress(user.getMacAddress());  //对方的Mac地址，通过查找该用户信息得到
		msg.setMessageText(m.getData());
		msg.setMessageType(m.getHead());
		msg.setSendTime(BasicUtils.formatDate(new Date(m.getSeq()),false));
		msg.setReceiveTime("Unknown"); //发送信息时暂时是不知道对方接收时间的，以后可能通过对方确认消息得到，暂时没做，预留
		DBUtils.insertMsg(msg);
	}
	
}
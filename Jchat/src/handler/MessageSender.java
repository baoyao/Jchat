package handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.codec.DecoderException;

import util.Config;
import util.SysUtils;
import util.UIUtils;
import bean.Message;
import exception.MessageOverflowException;

/**
 * 发送消息的类
 * @author zengjian
 *
 */
public class MessageSender {
	private static DatagramSocket socket ;
	private static DatagramPacket packet;

	static{
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public static void send(Message msg,String host) throws MessageOverflowException, IOException, DecoderException   {
		byte data[] = msg.toBytes();
		if(data.length>512){  //数据长度限制在512字节内
			throw new MessageOverflowException();
		}
		packet = new DatagramPacket(data, data.length, InetAddress.getByName(host),Config.MSG_UDP_PORT);
		socket.send(packet);
	}
	
	public static void sendLoginBroadcast(){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message msg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_LOGIN_MSG,"LoginBroadcast");
		try {
			MessageSender.send(msg, Config.BROADCAST_ADDR);
		} catch (Exception e) {
			UIUtils.showErroMessage(null, "登陆广播发送失败");
			e.printStackTrace();
		}
	}
	
	
	public static void sendLogoutBroadcast(){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message msg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_LOGOUT_MSG,"LogoutBroadcast");
		try {
			MessageSender.send(msg, Config.BROADCAST_ADDR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendLoginReplyMsg(String hostAddress){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message replyMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_LOGIN_REPLY_MSG,"LoginBroadcastReply");
		try {
			MessageSender.send(replyMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendShakeMsg(String hostAddress){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message shakeMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_SHAKE,"Shake");
		try {
			MessageSender.send(shakeMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendRequestVoiceMsg(String hostAddress){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message reqVoiceMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_REQUEST_VOICE,"Request_Voice");
		try {
			MessageSender.send(reqVoiceMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendStopVoiceMsg(String hostAddress){
		String userName = SysUtils.getLoginUserName(); //此处暂时取主机登录用户名
		Message stopVoiceMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_STOP_VOICE,"StopVoice");
		try {
			MessageSender.send(stopVoiceMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @throws DecoderException 
	 * @throws IOException 
	 * @throws MessageOverflowException 
	 * @throws Exception 
	 * 发送传送文件请求消息,
	 * @param hostAddress 文件名字符串和长度字符串组合时以“*”连接
	 * @param fileNameStr 封装多个文件名的组合字符串，以符合“|”分隔
	 * @param fileLengthStr 封装多个文件名的长度字符串，以符合“|”分隔
	 * @throws  
	 */
	public static void sendRequestFileSendMsg(String hostAddress, String fileNameStr,String fileLengthStr) throws MessageOverflowException, IOException, DecoderException  {
		String userName = SysUtils.getLoginUserName();
		Message msg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_REQUEST_SEND_FILE,fileNameStr+"*"+fileLengthStr);
		MessageSender.send(msg, hostAddress);
	}
	
	public static void sendRefuseFileMsg(String hostAddress){
		String userName = SysUtils.getLoginUserName();
		Message refuseFileMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_REFUSE_FILE,"RefuseFile");
		try {
			MessageSender.send(refuseFileMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void sendRefuseVoiceMsg(String hostAddress) {
		String userName = SysUtils.getLoginUserName();
		Message refuseVoiceMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_REFUSE_VOICE,"RefuseVoice");
		try {
			MessageSender.send(refuseVoiceMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAcceptVoiceMsg(String hostAddress) {
		String userName = SysUtils.getLoginUserName();
		Message acceptVoiceMsg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_ACCEPT_VOICE,"AcceptVoice");
		try {
			MessageSender.send(acceptVoiceMsg, hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Test
	public static void main(String[] args) throws Exception {
		String localHost = "192.168.1.103";
		//String localHost = "192.168.101.211";
		sendRequestVoiceMsg(localHost);
		//sendStopVoiceMsg(localHost);
		//String userName = "张三";
		//send(new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_REQUEST_VOICE,BasicUtils.formatDate(new Date(),false)),localHost);
	}

}

package handler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;

import ui.MainFrame;
import ui.TalkFrame;
import util.BasicUtils;
import util.DBUtils;
import util.Config;
import util.NetUtils;
import util.UIUtils;
import bean.Message;
import bean.User;

/**
 * ����������Ϣ���̣߳������յ���Ϣ����ʾ�������ڵ�������Ϣ�����
 * @author zengjian
 *
 */
public class MessageReceiver implements Runnable {
	private DatagramSocket socket;
	private DatagramPacket packet;
	private MainFrame mf;
	private static MessageReceiver instance = null;

	private MessageReceiver(MainFrame mf) {
		try {
			this.mf = mf;
			byte data[] = new byte[512];
			packet = new DatagramPacket(data, data.length);
			socket = new DatagramSocket(Config.MSG_UDP_PORT);
		} catch (SocketException e) {
			UIUtils.showErroMessage(mf, "��Ϣ�����߳�����ʧ�ܣ�");
		}
	}
	
	public static synchronized MessageReceiver getInstance(MainFrame mf){
		if(instance == null){
			instance = new MessageReceiver(mf);
		}
		instance.mf = mf;
		return instance;
	}


	@Override
	public void run() {
		try {
			while (true) {
				socket.receive(packet);
				String hostAddress = packet.getAddress().getHostAddress();
				Message msg = parseMessage(packet.getData(), packet
						.getLength());
				String msgHead = msg.getHead();
				String msgData = msg.getData();
				String userName = msg.getUserName();
				String hostName = msg.getHostName();
				User user = new User();
				user.setHostAddress(hostAddress);
				user.setHostName(hostName);
				user.setUserName(userName);
				String sendTime = BasicUtils.formatDate(new Date(msg.getSeq()));
				String info = userName + " " + sendTime;
				boolean isSelf = hostAddress.equals(NetUtils.getLocalHostAddress());
				if (msgHead.equals(Config.HEAD_TALK_MSG)) {
					receiveTalkMessage(user, info, msgData);
					UIUtils.playSound("sounds/msg.wav");
					DBUtils.writeReceivedMsgToDB(msg, hostAddress); //����Ϣ��װд�����ݿ�
				} else if (msgHead.equals(Config.HEAD_LOGIN_MSG) && !isSelf) { // ��½ʱ���͵Ĺ㲥��Ϣ
					receiveLoginMessage(user);
				}else if(msgHead.equals(Config.HEAD_LOGIN_REPLY_MSG)){ // �ظ���½�㲥����Ϣ�����ÿ����Ƿ�Ϊ�������ͣ���Ϊ�����ղ�������ĵ�½��
					receiveLoginReplyMessage(user);
				}else if (msgHead.equals(Config.HEAD_LOGOUT_MSG) && !isSelf) { // �˳�ʱ���͵Ĺ㲥��Ϣ
					receiveLogoutMessage(user);
				}else if(msgHead.equals(Config.HEAD_REQUEST_SEND_FILE)){ //�������ļ�����Ϣ
					receiveSendFileMessage(user, msgData, sendTime);
				}else if(msgHead.equals(Config.HEAD_SHAKE)){ //���ڶ�����Ϣ
					receiveShakeMessage(user, sendTime);
				}else if(msgHead.equals(Config.HEAD_REFUSE_FILE)){ //�ܾ������ļ�����Ϣ
					receiveRefuseFileMessage(user, sendTime);
				}else if(msgHead.equals(Config.HEAD_REQUEST_VOICE)){ //������������
					receiveRequestVoiceMessage(user,sendTime);
				}else if(msgHead.equals(Config.HEAD_ACCEPT_VOICE)){ //������������
					receiveAcceptVoiceMessage(user,sendTime);
				}else if(msgHead.equals(Config.HEAD_REFUSE_VOICE)){//�ܾ���������
					receiveRefuseVoiceMessage(user,sendTime);
				}else if(msgHead.equals(Config.HEAD_STOP_VOICE)){ //�ж���������
					receiveStopVoiceMessage(user,sendTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void receiveStopVoiceMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.STOP_VOICE_TIP+" "+sendTime);
		tf.stopVoice();
	}

	private void receiveAcceptVoiceMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.ACCEPT_VOICE_TIP+" "+sendTime);
		tf.startVoice();
	}


	private void receiveRefuseVoiceMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.REFUSE_VOICE_TIP+" "+sendTime);
	}


	private void receiveRequestVoiceMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.REQUEST_VOICE_TIP+" "+sendTime);
		tf.receiveRequestVoiceMsg();
	}


	private void receiveRefuseFileMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.REFUSE_FILE_MSG_TIP+" "+sendTime);
		tf.recoverSendPanel();
		tf.stopFileServer();
	}

	private void receiveSendFileMessage(User user, String msgData, String sendTime) {
		int splitIndex = msgData.indexOf("*");
		String fileNames = msgData.substring(0,splitIndex);
		String fileLengths = msgData.substring(splitIndex+1);
		String[] names = fileNames.split("\\|");
		String[] lengthStrs = fileLengths.split("\\|");
		long[] lengths = new long[lengthStrs.length];
		for(int i=0;i<lengthStrs.length;i++){
			lengths[i] = Long.parseLong(lengthStrs[i]);
		}
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.SEND_FILE_MSG_TIP+" "+sendTime);
		tf.getFilePanel().enableOperateBtn();
		tf.setFileNames(names);
		tf.setFileLengths(lengths);
		tf.updateReceiveTable();
		tf.openFilePanel();
	}

	private void receiveLogoutMessage(User user) {
		mf.removeUser(user);
	}

	private void receiveShakeMessage(User user, String sendTime) {
		TalkFrame tf = mf.getTalkFrame(user);
		if(tf==null){
			tf = new TalkFrame(user);
			mf.addTalkFrame(tf);
		}
		tf.setVisible(true);
		tf.appendSysMsg(Config.SHAKE_TIP+" "+sendTime);
		tf.shake();
	}

	private void receiveLoginReplyMessage(User user) {
		mf.addUser( user);
	}

	private void receiveLoginMessage(User user) {
		mf.addUser(user);
		// �յ���½�����跢��һ����½��Ӧ�������߶Է��Լ�����
		MessageSender.sendLoginReplyMsg(user.getHostAddress());
	}

	private void receiveTalkMessage(User user, String info,
			String msgData) {
		// ������յ�����������Ϣ
			TalkFrame tf = mf.getTalkFrame(user);
			if(tf==null){
				tf = new TalkFrame(user);
				mf.addTalkFrame(tf);
			}
			tf.setVisible(true);
			tf.appendMsg(info, msgData);
	}
	
	public  Message parseMessage(byte msgBytes[], int length) throws Exception{
		byte[] seqBytes = Arrays.copyOfRange(msgBytes, 0, 8);
		byte[] otherBytes = Arrays.copyOfRange(msgBytes, 8, length);
		String otherStr = new String(otherBytes);
		Message msg = new Message();
		msg.setSeq(BasicUtils.byteArrayToLong(seqBytes));
		msg.setHead(otherStr.substring(0,2));
		otherStr = otherStr.substring(2);
		int index = otherStr.indexOf(":");
		String userName = otherStr.substring(0,index);
		msg.setUserName(userName);
		otherStr = otherStr.substring(index+1);
		index = otherStr.indexOf(":");
		msg.setHostName(otherStr.substring(0,index));
		msg.setData(otherStr.substring(index+1));
		System.out.println("====�յ���Ϣ��"+msg.getUserName()+" "+msg.getHostName()+" "+msg.getData());
		return msg;
	}


}

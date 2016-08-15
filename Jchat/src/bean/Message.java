package bean;

import org.apache.commons.codec.DecoderException;

import util.BasicUtils;

/**
 * ��ϢЭ���ʽ��
 * �����+������+�û���+':'+������+':'+��Ϣ����
 * ����ţ�ռ8Byte�����õ�ǰ������ת��Ϊ�ֽ����飬��Ҳ�ǰ��ķ���ʱ��
 * �����֣���head�ֶΣ��ǳ���Ϊ2���ַ�����ÿ����������Global���ж���
 * @author DELL
 *
 */
public class Message {
	private long seq; //�����
	private String userName;
	private String hostName;
	private String head; //��Ϣͷ��������Ϣ������
	private String data; //��Ϣ����
	
	public Message(){
		
	}
	
	public Message(String userName,String hostName,String head, String message) {
		this.seq = System.currentTimeMillis();
		this.head = head;
		this.data = message;
		this.hostName = hostName;
		this.userName = userName;
		System.out.println("====������Ϣ��"+seq+this.head+this.userName+":"+this.hostName+":"+this.data);
	}
	
	public Message(long seq, String userName,String hostName,String head, String message) {
		this.seq = seq;
		this.head = head;
		this.data = message;
		this.hostName = hostName;
		this.userName = userName;
	}
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public byte[] toBytes() throws DecoderException{
		byte[] seqBytes = BasicUtils.longToByteArray(this.seq);
		byte[] otherBytes = (this.head+this.userName+":"+this.hostName+":"+this.data).getBytes();
		byte[] msgBytes = new byte[seqBytes.length+otherBytes.length];
		for(int i=0;i<msgBytes.length;i++){
			if(i<seqBytes.length)
				msgBytes[i] = seqBytes[i];
			else
				msgBytes[i] = otherBytes[i-seqBytes.length];
		}
		return msgBytes;
	}
	

}

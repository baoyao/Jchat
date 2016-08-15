package handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.commons.codec.DecoderException;

import util.Config;
import exception.MessageOverflowException;

/**
 * �����������࣬��Ͳ��
 * @author zengjian
 *
 */
public class VoiceSender implements Runnable{
	private  DatagramSocket socket ;
	private  DatagramPacket packet;
	private boolean flag = false;  //�����Ƿ���еı�ǣ�false��ʾ�Ͽ�
	TargetDataLine targetDataLine; 
	private String hostAddress;
	private static VoiceSender instance = null;
	private VoiceSender(){
	}
	public static synchronized VoiceSender getInstance(){
		if(instance == null)
			instance = new VoiceSender();
		return instance;
	}
	@Override
	public void run() {
		flag = true;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		AudioFormat format =new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 44100.0f, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
        try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			if(!targetDataLine.isOpen())
				targetDataLine.open(format, targetDataLine.getBufferSize());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			flag = true;
			System.out.println("˵���������Ѿ��������ж���ռ�ã��ʳ�������flagΪtrueȻ���˳�");
			return ;
		} 
        
         int length = 512;
         System.out.println(length);
         byte[] data = new byte[length];
         int readLen=0; 
         targetDataLine.start();
         while (flag) { 
             readLen = targetDataLine.read(data, 0,data.length);
             try { 
               send(data, readLen);//���ͳ�ȥ 
             } 
             catch (Exception ex) { 
          	   ex.printStackTrace();
                 break; 
             } 
         }
         targetDataLine.stop(); 
         targetDataLine.close(); 
         targetDataLine = null;
         socket.close();
         socket = null;
         flag = false;
         System.out.println("�˳�sender run");
	}
	
	public  void send(byte[] data,int length) throws MessageOverflowException, IOException, DecoderException   {
		if(length>512){  //���ݳ���������512�ֽ���
			throw new MessageOverflowException();
		}
		packet = new DatagramPacket(data, length, InetAddress.getByName(this.hostAddress),Config.VOICE_UDP_PORT);
		socket.send(packet);
	}

	public boolean isFlag() {
		return flag;
	}
	
	public void setHostAddress(String hostAddr){
		this.hostAddress = hostAddr;
	}
	
	public void shutDown() {
		this.flag = false;
	}
	
}

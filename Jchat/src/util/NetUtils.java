package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetUtils {	
	public static String getLocalHostAddress(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getLocalHostName(){
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * ��ȡ������Ӳ����ַ
	 * @return
	 */
	public static String getLocalHostMacAddress() {
		String mac = "";
		StringBuffer sb = new StringBuffer();
		try {
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] macs = ni.getHardwareAddress(); //Ӳ����ַ���ֽ�����
			//����ѭ�����ֽ�����ÿһ��Ԫ�أ�byte��ʾ�����֣�ת��Ϊ����ֵ��16���Ʊ�ʾ
			for (int i = 0; i < macs.length; i++) {
				mac = Integer.toHexString(macs[i] & 0xFF);
				if (mac.length() == 1) {
					mac = '0' + mac;
				}
				sb.append(mac + "-");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		mac = sb.toString();
		if(mac==null || mac.equals("")){
			UIUtils.showAlertMessage(null, "�����ַ�޷���ȡ�����������Ƿ�Ͽ���");
			return "";
		}
		mac = mac.substring(0, mac.length() - 1);
		return mac;
	}
}

package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import ext.BaoUtils;

public class NetUtils {	
	public static String getLocalHostAddress(){
		try {
//			return InetAddress.getLocalHost().getHostAddress();//remove by bao
			return BaoUtils.getLocalHost().getHostAddress();//add by bao
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getLocalHostName(){
		try {
//			return InetAddress.getLocalHost().getHostName();//remove by bao
			return BaoUtils.getLocalHost().getHostName();//add by bao
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取本机的硬件地址
	 * @return
	 */
	public static String getLocalHostMacAddress() {
		String mac = "";
		StringBuffer sb = new StringBuffer();
		try {
			System.out.println("InetAddress.getLocalHost(): "+InetAddress.getLocalHost());
			System.out.println("BaoUtils.getLocalHost(): "+BaoUtils.getLocalHost());
//			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());//remove by bao
			NetworkInterface ni = NetworkInterface.getByInetAddress(BaoUtils.getLocalHost());//add by bao
			byte[] macs = ni.getHardwareAddress(); //硬件地址的字节数组
			//下面循环将字节数组每一个元素（byte表示的数字）转换为该数值的16进制表示
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
			UIUtils.showAlertMessage(null, "物理地址无法获取，请检查网卡是否断开！");
			return "";
		}
		mac = mac.substring(0, mac.length() - 1);
		return mac;
	}
}

package util;

import java.util.Map;

public class SysUtils {
	/**
	 * ��ȡ�����ϵ�ǰ�ĵ�¼�û�
	 * @return
	 */
	public static String getLoginUserName(){
		Map<String, String> map = System.getenv();
		if(map.containsKey("USERNAME"))
			return  map.get("USERNAME");
		else
			return "";
	}
	/**
	 * ��ȡ����������
	 * @return
	 */
	public static String getComputerName(){
		Map<String, String> map = System.getenv();
		if(map.containsKey("COMPUTERNAME"))
			return  map.get("COMPUTERNAME");
		else
			return "";
	}
}

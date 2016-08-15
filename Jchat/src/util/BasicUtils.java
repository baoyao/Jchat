package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BasicUtils {
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 * @param date Ҫ��ʽ��������
	 * @param timeOnly ����ֻ����ʱ�䲿���������ڲ��֣�����Ϊ����+ʱ��
	 * @return
	 */
	public static String formatDate(Date date,boolean timeOnly){
		if(timeOnly)
			return timeFormat.format(date);
		else
			return dateTimeFormat.format(date);
	}
	
	public static String formatDate(Date date){
		return formatDate(date,true);
	}

	public static String convertByte(long byteData){
		String result = byteData + "B";
		double temp;
		if(byteData >= 1024){
			temp = byteData / 1024;
			result = String.format("%.1f", temp) + "KB";
			if(temp >=1024){
				temp = temp / 1024;
				result = String.format("%.1f", temp) + "MB";
				if(temp >=1024){
					temp = temp/1024;
					result = String.format("%.2f", temp)+"GB";
				}
			}
		}
		return result;
	}
	
	public static String convertTime(long second){
		String result = second + "��";
		long m;
		long h;
		long s;
		if(second >= 60){
			m = second / 60;
			s = second % 60;
			result = m+"��"+s+"��";
			if(m >= 60){
				h = m / 60;
				m = m % 60;
				result = h+"Сʱ"+m+"��"+s+"��";
			}
		}
		return result;
	}
	
    /**
     * ����������ת��Ϊbyte���飨�Ա��������ϴ��䣩
     * @param lNum ����������64λ��
     * @return ת������ֽ����� ������Ϊ8��
     * @throws DecoderException 
     */
    public static byte[] longToByteArray(Long lNum) throws DecoderException{
    	String hexStr = Long.toHexString(lNum);
    	StringBuilder sb = new StringBuilder(hexStr);
    	int len = sb.length();
    	for(int i=0;i<(16-len);i++){
    		sb.insert(0, '0');
    	}
    	hexStr = sb.toString();
		return Hex.decodeHex(hexStr.toCharArray());
		
    }
    
    /**
     * ��byte���飨��ͨ�����紫����������ݣ�ת��Ϊ��������
     * @param byteArray �ֽ����飨����Ϊ8��
     * @return ת����ĳ���������64λ��
     * @throws Exception 
     */
    public static Long byteArrayToLong(byte[] byteArray) throws Exception{
    	if(byteArray.length != 8)
    		throw new Exception("�ֽ����鲻��ȷ�����ȱ���Ϊ8");
    	char[] charArray = Hex.encodeHex(byteArray);
    	String strValue = String.valueOf(charArray);
    	return Long.decode("#"+strValue);
    }
}

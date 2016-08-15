package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class UIUtils {
	
	/**
	 * ������ʾ������Ϣ�ĶԻ���
	 */
	public static void showErroMessage(Component f,String msg){
		JOptionPane.showMessageDialog(f, msg, "����", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * ������ʾ������Ϣ�ĶԻ���
	 */
	public static void showAlertMessage(Component f,String msg){
		JOptionPane.showMessageDialog(f, msg, "��ʾ", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * ��ʼ��ϵͳ��Ĭ������
	 * @param fnt
	 */
	public static void initGlobalFont(Font fnt) {
		FontUIResource fontRes = new FontUIResource(fnt);
		for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource){
				//System.out.println(key);
				UIManager.put(key, fontRes);
			}
		}
	}
	
	
	/**
	 * �������������Ļ�м�
	 */
	public static void setLocationCenter(JFrame f){
		/*Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screen.getWidth()-f.getWidth())/2);
		int y = (int) ((screen.getHeight()-f.getHeight())/2);
		f.setLocation(x, y);*/
		f.setLocationRelativeTo(null);
	}

	
	public static String getLabelString(int num){
		return "�����û�����"+num+"��";
	}
	
	/**
	 * �õ���ԴͼƬ
	 * @param imgFile
	 * @return
	 */
	public static ImageIcon getImageIcon(String imgFile){
		return new ImageIcon(UIUtils.class.getClassLoader().getResource(imgFile));
	}
	
	public static Image getIconImage(String iconFile){
		try {
			return ImageIO.read(UIUtils.class.getClassLoader().getResourceAsStream(iconFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * �õ���̬ͼƬ
	 * @param gifFile
	 * @return
	 */
	public static ImageIcon getImageIconFromGIF(String gifFile){
		try{
			Image image=ImageIO.read(UIUtils.class.getClassLoader().getResourceAsStream(gifFile));
			return new ImageIcon(image);
		}catch(IOException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ����au��ʽ��Ч
	 * @param soundPath
	 * @throws IOException
	 */
	public static void playSound(String soundPath) throws IOException{
		AudioStream as=new AudioStream(UIUtils.class.getClassLoader().getResourceAsStream(soundPath));
		AudioPlayer.player.start(as);
	}
	
	/**
	 * ����16������ɫֵ�õ�Color����
	 * @param color ��ʽΪ��#FFFFFF����FFFFFF��
	 * @return
	 */
	static public Color getColorFromHex(String color) {
		if (color.charAt(0) == '#') {
			color = color.substring(1);
		}
		if (color.length() != 6) {
			return null;
		}
		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
	
}

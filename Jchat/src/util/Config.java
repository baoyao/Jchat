package util;

import java.awt.Color;

public class Config {
	//������Ĭ��ֵ
	public static final int TALK_FRAME_WIDTH = 500;
	public static final int TALK_FRAME_HEIGHT = 550;
	public static final int MAIN_FRAME_HEIGHT = 600;
	public static final int MAIN_FRAME_WIDTH = 280;
	
	//�㲥��ַ
	public static final String BROADCAST_ADDR = "255.255.255.255";
	
	public static final int MSG_UDP_PORT = 9876; //����UDPͨ�Ŷ˿�
	public static final int FILE_TCP_PORT = 6789; //�����ļ���TCP��Ϣ�˿�
	public static final int VOICE_UDP_PORT = 8765 ; //���������UDP��Ϣ�˿�
	
	public static final Color DEFAULT_MSG_COLOR = Color.black; //��Ϣ����Ĭ��������ɫ
	public static final Color DEFAULT_INFO_COLOR = Color.blue; //��Ϣ������ϢĬ��������ɫ
	public static final Color SYS_COLOR_WEAK= Color.gray; //ϵͳ��ϢĬ��������ɫ
	public static final Color SYS_COLOR_COMMON = new Color(75, 0, 130); //ϵͳ��ϢĬ��������ɫ
	public static final Color SYS_COLOR_STRONG = Color.red; //ϵͳ��ϢĬ��������ɫ
	public static final Color SYS_COLOR_MOUSEOVER = new Color(183,243,155); //������ʱ���Ĭ�ϱ�����ɫ
	public static final Color SYS_COLOR_SELECTED = UIUtils.getColorFromHex("#fcf0c1"); //���ѡ��ʱ��Ĭ�ϱ�����ɫ
	public static final int DEFAULT_MSG_FONT_SIZE = 14; //��Ϣ����Ĭ�������С 
	public static final int DEFAULT_INFO_FONT_SIZE = 14; //��Ϣ������ϢĬ�������С
	
	//��Ϣͷ����ȡֵ
	public static final String HEAD_LOGIN_MSG = "01"; //��½ʱ�㲥��Ϣͷ��
	public static final String HEAD_TALK_MSG = "02"; //������Ϣ
	public static final String HEAD_LOGOUT_MSG = "03"; //�˳�ʱ�㲥��Ϣͷ��
	public static final String HEAD_LOGIN_REPLY_MSG = "04"; //��Ӧ��½�㲥����Ϣͷ��
	public static final String HEAD_REQUEST_SEND_FILE = "05"; //�������ļ�����Ϣͷ��
	public static final String HEAD_REFUSE_FILE = "07"; //�ܾ������ļ�����Ϣͷ��
	public static final String HEAD_SHAKE = "08"; //���ʹ��ڶ�������Ϣͷ��
	public static final String HEAD_REQUEST_VOICE = "09"; //���������������Ϣͷ��
	public static final String HEAD_ACCEPT_VOICE = "10"; //���������������Ϣͷ��
	public static final String HEAD_REFUSE_VOICE = "11"; //�ܾ������������Ϣͷ��
	public static final String HEAD_STOP_VOICE = "12"; //�ж������������Ϣͷ��
	
	//������Ϣ
	public static final String LOCAL_HOST_ADDR = NetUtils.getLocalHostAddress(); //����������ַ
	public static final String LOCAL_HOST_NAME = NetUtils.getLocalHostName(); //����������
	public static final String LOCAL_MAC_ADDR = NetUtils.getLocalHostMacAddress(); //	����Ӳ����ַ
	public static final String LOCAL_USER_NAME_DEFAULT = SysUtils.getLoginUserName(); //Ĭ�ϵ��û���Ϊ������¼�����������������ȡ������
	
	//ϵͳ��ʾ
	public static final String SHAKE_TIP = "�Է�����������һ������"; //������ʾ
	public static final String SHAKE_TIP_SELF = "�����Է�������һ������"; //������ʾ
	public static final String SEND_FILE_MSG_TIP="�Է��������ļ��������ұ��ļ����������ջ�ܾ���"; //�ļ�������Ϣ��ʾ
	public static final String REFUSE_FILE_MSG_TIP = "�Է������˽�������Ҫ���͵��ļ���"; // �ܾ������ļ���Ϣ����ʾ
	public static final String REFUSE_FILE_MSG_TIP_SELF = "�������˽��նԷ���Ҫ���͵��ļ���"; // �ܾ������ļ���Ϣ����ʾ
	public static final String FILE_SEND_FINISHED = "�ļ��������"; //
	public static final String FILE_ACCEPT_FINISHED="�ļ��������"; //
	public static final String REQUEST_VOICE_TIP = "�Է������������죬�Ƿ���ܣ�";
	public static final String REQUEST_VOICE_TIP_SELF = "���������������죬���ڵȴ��Է���Ӧ�����Ժ�...";
	public static final String REFUSE_VOICE_TIP_SELF = "���ܾ�����Է���������������";
	public static final String REFUSE_VOICE_TIP = "�Է��ܾ�������������������";
	public static final String ACCEPT_VOICE_TIP = "�Է������������������������������쿪ʼ";
	public static final String ACCEPT_VOICE_TIP_SELF = "�������˶Է����������������������쿪ʼ";
	public static final String STOP_VOICE_TIP = "�Է��ж��˺������������죡";
	public static final String STOP_VOICE_TIP_SELF = "���ж��˺ͶԷ�����������";
	
	//�ļ�����״̬
	public static final String STATE_WAITING = "01";   //�ȴ����ͻ����
	public static final String STATE_SENDING = "02";  //���ڷ���
	public static final String STATE_ACCEPTING = "03";  //���ڽ���
	public static final String STATE_COMPLETED = "04"; //���ͻ�������
	public static final String STATE_CANCEL_SEND = "05"; //��ȡ������
	public static final String STATE_CANCEL_ACCEPT = "06"; //��ȡ������
	//�ļ�����״̬����
	public static final String TEXT_STATE_WAITING = "�ȴ�";   //�ȴ����ͻ����
	public static final String TEXT_STATE_SENDING = "���ڷ���";  //���ڷ���
	public static final String TEXT_STATE_ACCEPTING = "���ڽ���";  //���ڽ���
	public static final String TEXT_STATE_COMPLETED = "�����"; //���ͻ�������
	public static final String TEXT_STATE_CANCEL_SEND = "��ȡ������"; //��ȡ������
	public static final String TEXT_STATE_CANCEL_ACCEPT = "��ȡ������"; //��ȡ������
}

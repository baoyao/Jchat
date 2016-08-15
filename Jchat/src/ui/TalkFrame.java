package ui;

import handler.MessageSender;
import handler.VoiceReceiver;
import handler.VoiceSender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import p2p.FileClient;
import p2p.FileServer;
import util.BasicUtils;
import util.Config;
import util.DBUtils;
import util.SysUtils;
import util.UIUtils;
import bean.Message;
import bean.User;


public class TalkFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private User user; //�Է��û�����
	private String savePath; // �����ļ�ʱ�ı���·����ȫ������ʱ�û�ѡ���·����
	private String fileNames[]; //�����ļ����ļ�������
	private long fileLengths[]; //�����ļ��ĳ�������
	private FileServer server; //�����ļ�ʱ��TCP������
	private boolean filePanelOpen = false;
	private boolean voiceTalkFlag = false;
	public TalkFrame(User user){
		this.user = user;
		initComponents();
	}
	
	private void initComponents() {
		this.setIconImage(UIUtils.getIconImage("images/user_24.png"));
		this.setTitle(user.getUserName()+"("+user.getHostName()+")");
		this.setSize(new Dimension(Config.TALK_FRAME_WIDTH,Config.TALK_FRAME_HEIGHT));

		JToolBar toolBar = new JToolBar();
		toolBar.setBorderPainted(false);
		openFilePaneBtn = new JButton("�����ļ�",UIUtils.getImageIcon("images/file24.png"));
		openFilePaneBtn.setFocusable(false);
		openFilePaneBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		openFilePaneBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolBar.add(openFilePaneBtn);
		voiceBtn = new JButton("��������",UIUtils.getImageIcon("images/mic2_24.png"));
		voiceBtn.setFocusable(false);
		voiceBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		voiceBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolBar.add(voiceBtn);
		textPane = new JTextPane();
		textPane.setEditable(false);
		msgRecordSP = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		msgToSendTA = new JTextArea();
		msgToSendTA.setRows(5);
		msgToSendTA.setLineWrap(true);
		msgToSendTA.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					sendTalkMsg();
				}
			}
		});
		msgToSendSP = new JScrollPane(msgToSendTA,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel south = new JPanel(new BorderLayout());
		JPanel sthBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		sendBtn = new JButton("����",UIUtils.getImageIcon("images/send.png"));
		cancelBtn = new JButton("�ر�",UIUtils.getImageIcon("images/cancel.png"));
		sthBtnPanel.add(sendBtn);
		sthBtnPanel.add(cancelBtn);
		JToolBar toolBar2 = new JToolBar();
		toolBar2.setBorderPainted(false);
		shakeBtn = new JButton(UIUtils.getImageIcon("images/bell24.png"));
		shakeBtn.setToolTipText("����");
		shakeBtn.setFocusable(false);
		
		toolBar2.add(shakeBtn);
		south.add(toolBar2,BorderLayout.NORTH);
		south.add(msgToSendSP,BorderLayout.CENTER);
		south.add(sthBtnPanel,BorderLayout.SOUTH);
		
		mainPanel = new JPanel(new BorderLayout(0,2));
		mainPanel.add(msgRecordSP, BorderLayout.CENTER);
		mainPanel.add(south, BorderLayout.SOUTH);
		
		filePanel = new FileSelectPanel(this);
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,mainPanel,filePanel);
		jsp.setDividerLocation(Config.TALK_FRAME_WIDTH);
		jsp.setDividerSize(5);
		jsp.setOneTouchExpandable(false);
		jsp.setEnabled(false); //���÷ָ��߲����ƶ�
		
		menu = new JPopupMenu();
		menuItemClear = new JMenuItem("����",UIUtils.getImageIcon("images/book_go.png"));
		menu.add(menuItemClear); 
		textPane.setComponentPopupMenu(menu);
		menuItemClear.addActionListener(this);
		
		openFilePaneBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		shakeBtn.addActionListener(this);
		voiceBtn.addActionListener(this);
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(toolBar,BorderLayout.NORTH);
		content.add(jsp,BorderLayout.CENTER);
		this.setResizable(false);
		UIUtils.setLocationCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.openFilePaneBtn){
			if(filePanelOpen){
				hideFilePanel();
			}else{
				openFilePanel();
			}
		}else if(e.getSource()==this.sendBtn){
			sendTalkMsg();
		}else if(e.getSource()==this.cancelBtn){
			this.dispose();
		}else if(e.getSource()==this.shakeBtn){
			this.sendShakeMsg();
		}else if(e.getSource()==this.menuItemClear){
			this.clear();
		}else if(e.getSource()==this.voiceBtn){
			if(this.voiceTalkFlag){
				this.stopVoice();
				this.sendStopVoiceMsg();
			}else{
				this.sendRequestVoiceMsg();
			}
		}
	}
	

	public void updateSendProgress(int value,String info,String toolTip){
		this.filePanel.updateSendProgress(value,info,toolTip);
	}
	
	public void updateReceiveProgress(int value,String info, String toolTip){
		this.filePanel.updateReceiveProgress(value,info,toolTip);
	}
/*	public void changeSendPanel(){
		this.filePanel.showSendProgressBar();
	}*/
	public void changeReceivePanel(){
		this.filePanel.showReceiveProgressBar();		
	}
	public void recoverSendPanel(){
		this.filePanel.recoverSendPanel();
	}
	
	public void recoverReceivePanel(){
		this.filePanel.recoverReceivePanel();
	}
	
	public void startFileServer(String[] fileNames,long[] fileLengths){
		server = null;
		server = new FileServer(this,fileNames,fileLengths);
		server.start();
	}
	
	public void stopFileServer(){
		if(server != null){
			if(server.isAlive()){
				server.stopServer();
			}
			server = null;
		}
	}
	
	private void clear() {
		textPane.setText("");
	}

	/**
	 * ������������
	 */
	private void sendTalkMsg() {

		String content = msgToSendTA.getText().trim();
		if(!content.equals("")){
			try {
				String userName = SysUtils.getLoginUserName();
				Message msg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_TALK_MSG,content);
				String name = Config.LOCAL_USER_NAME_DEFAULT;
				String sendTime = BasicUtils.formatDate(new Date());
				String info = name+" "+sendTime;
				this.appendMsg(info,content);
				msgToSendTA.setText("");
				MessageSender.send(msg, user.getHostAddress());
				DBUtils.writeSentMsgToDB(msg, user);
			} catch (Exception ex) {
				ex.printStackTrace();
				UIUtils.showErroMessage(this, "����ʧ�ܣ�������Ϣ��"+ex.getMessage());
			}
		}
	
	}

	/**
	 * ���Ͷ���
	 */
	private void sendShakeMsg() {
		try {
			String sendTime = BasicUtils.formatDate(new Date());
			this.appendSysMsg(Config.SHAKE_TIP_SELF+" "+sendTime);
			MessageSender.sendShakeMsg(user.getHostAddress());
		} catch (Exception ex) {
			UIUtils.showErroMessage(this, "����ʧ��");
		}
	}

	private void sendRequestVoiceMsg(){
		try {
			String sendTime = BasicUtils.formatDate(new Date());
			this.appendSysMsg(Config.REQUEST_VOICE_TIP_SELF+" "+sendTime);
			MessageSender.sendRequestVoiceMsg(user.getHostAddress());
		} catch (Exception ex) {
			UIUtils.showErroMessage(this, "����ʧ��");
		}
	}
	
	private void sendStopVoiceMsg() {
		try {
			String sendTime = BasicUtils.formatDate(new Date());
			this.appendSysMsg(Config.STOP_VOICE_TIP_SELF+" "+sendTime);
			MessageSender.sendStopVoiceMsg(user.getHostAddress());
		} catch (Exception ex) {
			UIUtils.showErroMessage(this, "����ʧ��");
		}
	}
	
	/**
	 * ���ش����ļ����
	 */
	public void hideFilePanel(){
		this.setSize(Config.TALK_FRAME_WIDTH, Config.TALK_FRAME_HEIGHT);
		jsp.setDividerLocation(Config.TALK_FRAME_WIDTH);
		filePanelOpen = false;
		this.openFilePaneBtn.setIcon(UIUtils.getImageIcon("images/file24.png"));
		this.openFilePaneBtn.setText("�ļ�����");
	}
	
	/**
	 * �򿪴����ļ����
	 */
	public void openFilePanel(){
		this.setSize(Config.TALK_FRAME_WIDTH+370, Config.TALK_FRAME_HEIGHT);
		jsp.setDividerLocation(Config.TALK_FRAME_WIDTH-15);
		filePanelOpen = true;
		this.openFilePaneBtn.setIcon(UIUtils.getImageIcon("images/forbiden24.png"));
		this.openFilePaneBtn.setText("�ر��ļ�");
	}
	
	/**
	 * 
	 * @param info �����¼�ĸ�����Ϣ��������+����ʱ��
	 * @param msg �����¼����Ϣ����
	 */
	public synchronized void appendMsg(String info,String msg){
		updateMsgInfo(info+"\n",Config.DEFAULT_INFO_COLOR,false,Config.DEFAULT_INFO_FONT_SIZE,null);
		updateMsgInfo("  "+msg+"\n",Config.DEFAULT_MSG_COLOR,false,Config.DEFAULT_MSG_FONT_SIZE,null);
		textPane.setCaretPosition(textPane.getDocument().getLength()); //�����Զ����������
	}
	
	public synchronized void  appendSysMsg(String msg){
		updateMsgInfo(msg+"\n",Config.SYS_COLOR_COMMON,false,Config.DEFAULT_MSG_FONT_SIZE,UIUtils.getImageIcon("images/information.png"));
		textPane.setCaretPosition(textPane.getDocument().getLength()); //�����Զ����������
	}
	
	/**
	 * ����������Ϣ�������ͨ��ʽ��Ϣ�ķ���
	 * @param str ������Ϣ��������
	 * @param col ����������ɫ
	 * @param isBold �����Ƿ�Ϊ����
	 * @param fontSize ���������С
	 * @param icon ��������Ϣ�ײ�Ҫ�����ͼ�꣬���Ϊnull���ʾ������ͼ��
	 */
	private void updateMsgInfo(String str, Color col, boolean isBold, int fontSize, Icon icon ) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col); // ��ɫ
		if (isBold) {
			StyleConstants.setBold(attrSet, true);
		}// ��������
		StyleConstants.setFontSize(attrSet, fontSize); // �����С
		Document doc = textPane.getDocument();
		if(icon != null){
			textPane.setCaretPosition(doc.getLength());
			textPane.insertIcon(icon);
		}
		try {
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	
	public void shake(){
		this.requestFocus(); // �Ȼ�ý���
		UIUtils.setLocationCenter(this);//Ȼ����õ���Ļ����
		try {
			UIUtils.playSound("sounds/alarm.au");  //���Ŷ�����Ч
		} catch (IOException e) {
			e.printStackTrace();
		}
		//���ʵ������Ļ����Ķ���Ч��
		Point origiLocation = this.getLocationOnScreen();
		for(int i=0;i<15;i++){
			int shakeX = (int)((Math.random()-0.5)*50);
			int shakeY = (int)((Math.random()-0.5)*50);
			this.setLocation(origiLocation.x+shakeX, origiLocation.y+shakeY);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.setLocation(origiLocation);
	}
	
	public void receiveRequestVoiceMsg() {
		if(JOptionPane.showConfirmDialog(this, Config.REQUEST_VOICE_TIP, "����ȷ��", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
			startVoice();
			this.appendSysMsg(Config.ACCEPT_VOICE_TIP_SELF);
			MessageSender.sendAcceptVoiceMsg(this.user.getHostAddress());
		}else{
			this.appendSysMsg(Config.REFUSE_VOICE_TIP_SELF);
			MessageSender.sendRefuseVoiceMsg(this.user.getHostAddress());
		}
	}
	
	public void startVoice(){
		System.out.println("��ʼ��������������");
		this.startVoiceSender();
		this.startVoiceReceiver();
		this.voiceBtn.setText("�ر�����");
		this.voiceBtn.setIcon(UIUtils.getImageIcon("images/closemic24.png"));
		this.voiceTalkFlag = true;
	}
	
	public void stopVoice(){
		System.out.println("��ʼ�ر�����");
		VoiceSender voiceSender  = VoiceSender.getInstance();
		if(voiceSender.isFlag()){
			System.out.println("��ʼ�ر����������̡߳�����");
			voiceSender.shutDown();
		}
		VoiceReceiver voiceReceiver = VoiceReceiver.getInstance();
		if(voiceReceiver.isFlag()){
			System.out.println("��ʼ�ر����������̡߳�����");
			voiceReceiver.shutDown();
		}
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				TalkFrame.this.voiceBtn.setText("��������");
				TalkFrame.this.voiceBtn.setIcon(UIUtils.getImageIcon("images/mic2_24.png"));
				TalkFrame.this.voiceTalkFlag = false;
			}
		});
	}
	
	public void startVoiceReceiver(){
		VoiceReceiver voiceReceiver = VoiceReceiver.getInstance();
		if(!voiceReceiver.isFlag()){
			System.out.println("��ʼ�������������̡߳�����");
			new Thread(voiceReceiver).start();
		}
	}
	
	public void startVoiceSender(){
		VoiceSender voiceSender  = VoiceSender.getInstance();
		voiceSender.setHostAddress(this.getHostAddress());
		if(!voiceSender.isFlag()){
			System.out.println("��ʼ�������������̡߳�����");
			new Thread(voiceSender).start();
		}
	}
	/**
	 * ���յ������ļ�����Ϣʱ�����Է�Ҫ���͵��ļ���ʾ�ڱ����
	 */
	public void updateReceiveTable() {
		this.filePanel.updateReceiveTable(this.fileNames,this.fileLengths);
	}
	
	public void recoverReceiveTable(){
		this.filePanel.recoverReceiveTable();
	}
	
	/**
	 * ���·����ļ�����״̬�ֶ�
	 * index : �к�
	 * state ��״ֵ̬
	 */
	public void updateSendTableState(int index,String state){
		this.filePanel.updateSendTableState(index, state);
	}
	
	/**
	 * ���½����ļ�����״̬�ֶ�
	 * index : �к�
	 * state ��״ֵ̬
	 */
	public void updateReceiveTableState(int index,String state){
		this.filePanel.updateReceiveTableState(index, state);
	}
	
	public String getHostAddress(){
		return this.user.getHostAddress();
	}
	
	public void startAcceptFile(){
		new FileClient(this).start();
	}
	
	public FileSelectPanel getFilePanel(){
		return this.filePanel;
	}
	
	public void setSavePath(String savePath){
		this.savePath = savePath;
	}
	
	public String getSavePath(){
		return this.savePath;
	}
	
	public FileServer getServer() {
		return server;
	}

	public void setServer(FileServer server) {
		this.server = server;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public long[] getFileLengths() {
		return fileLengths;
	}

	public void setFileLengths(long[] fileLengths) {
		this.fileLengths = fileLengths;
	}



	/*�ָ���壬���Ϊ����壨������壩���ұ�Ϊ�ļ�������壬
	    Ĭ���ұ�������أ������ļ�ʱ����ʾ*/
	private JSplitPane jsp;  
	private JPanel mainPanel;  // ����壨������壩
	private FileSelectPanel filePanel;  // �ļ��������
	
	private JButton sendBtn; //�����������ݰ�ť
	private JButton cancelBtn; //ȡ����ť
	private JButton openFilePaneBtn; //�򿪴����ļ����İ�ť
	private JButton shakeBtn; //������ť
	private JButton voiceBtn; //��������
	
	private JTextArea msgToSendTA; //Ҫ���͵������ı���
	private JScrollPane msgRecordSP; 
	private JScrollPane msgToSendSP;
	
	private JTextPane textPane;
	private JPopupMenu menu;
	private JMenuItem menuItemClear;



}

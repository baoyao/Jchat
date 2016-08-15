package ui;

import handler.MessageReceiver;
import handler.MessageSender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.skin.SubstanceModerateLookAndFeel;
import org.jvnet.substance.utils.SubstanceConstants.ImageWatermarkKind;
import org.jvnet.substance.watermark.SubstanceImageWatermark;

import util.Config;
import util.DBUtils;
import util.NetUtils;
import util.UIUtils;
import bean.User;

public class MainFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private List<TalkFrame> talkFrames ;  //�������д򿪵������
	private MessageReceiver msgReceiver; //��Ϣ������
	private ArrayList<User> users; //�����û�����
	private User self; //�������Ϣ
	
	public MainFrame(){
		try {
			DBUtils.checkMsgDB();
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showErroMessage(this, "�������ݿ�ʧ�ܣ�");
			return;
		}
		talkFrames = new LinkedList<TalkFrame>();
		users = new ArrayList<User>();
		self = new User();
		self.setHostName(NetUtils.getLocalHostName());
		self.setHostAddress(NetUtils.getLocalHostAddress());
		self.setMacAddress(NetUtils.getLocalHostMacAddress());
		self.setUserName(Config.LOCAL_USER_NAME_DEFAULT);  //�˴�Ԥ��������ͨ����ȡ�����ļ������û������ã����δ���������loginName
		self.setRemark(""); //�˴�Ԥ��������ͨ����ȡ�����ļ������û�������
		users.add(self);
		
		initComponents();
		//����������Ϣ�߳�
		msgReceiver = MessageReceiver.getInstance(this);
		new Thread(msgReceiver).start(); 
		
		//���͵�½�㲥��Ϣ
		MessageSender.sendLoginBroadcast();
	
	}
	private void initComponents() {
		userList = new JList(users.toArray());
		userList.setCellRenderer(new MyListCellRenderer());
		label = new JLabel(UIUtils.getLabelString(users.size()));
		userList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){  
				MyListCellRenderer rd = (MyListCellRenderer)(userList.getCellRenderer());
				rd.setBorder(new LineBorder(Color.red,4,false));
		        if(e.getClickCount()==2){   //˫��ʱ 
		        	User user = ((User)userList.getSelectedValue());
		        	TalkFrame tf = MainFrame.this.getTalkFrame(user);
		        	if(tf==null){
			        	TalkFrame newTF = new TalkFrame(user);
						MainFrame.this.addTalkFrame(newTF);
						newTF.setVisible(true);    
		        	}else{
		        		tf.setVisible(true);
		        	}
		        	
		        }  
		    }  
		});
		refresh = new JButton("ˢ��",UIUtils.getImageIcon("images/refresh.png"));
		refresh.setFocusable(false);
		refresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		refresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		refresh.addActionListener(this);
		JToolBar toolBar = new JToolBar();
		toolBar.setBorderPainted(false);
		toolBar.add(refresh);
		JPanel center = new JPanel(new BorderLayout(0,5));
		center.add(label,BorderLayout.NORTH);
		JScrollPane userScrollPane = new JScrollPane(userList);
		center.add(userScrollPane,BorderLayout.CENTER);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,5));
		contentPane.add(toolBar,BorderLayout.NORTH);
		contentPane.add(center,BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				//�ر�ʱ���˳��������ʱ�跢��һ���˳��㲥��֪ͨ���������û�
				System.out.println("closing...");
				MessageSender.sendLogoutBroadcast();
				System.exit(0);
			}
		});
		this.setTitle("���������칤��");
		this.setIconImage(UIUtils.getIconImage("images/app64.png"));
		this.setSize(new Dimension(Config.MAIN_FRAME_WIDTH,Config.MAIN_FRAME_HEIGHT));
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==refresh){
			refresh(); 
		}
	}
	
	private void refresh() {
		users.clear();
		users.add(self);
		userList.setListData(users.toArray());
		label.setText(UIUtils.getLabelString(users.size()));
		MessageSender.sendLoginBroadcast();
	}

	/**
	 * �жϺ�hostAddressָ����ip��ַ��������Ƿ����
	 * @param hostAddress
	 * @return
	 */
	public boolean hasTalkFrame(String hostAddress){
		Iterator<TalkFrame> it = this.talkFrames.iterator();
		while(it.hasNext()){
			if(it.next().getHostAddress().equals(hostAddress))
				return true;
		}
		return false;
	}
	
	/**
	 * ����hostAddress���ض�Ӧ�������
	 * @param hostAddress
	 * @return
	 */
	public TalkFrame getTalkFrame(User user){
		Iterator<TalkFrame> it = this.talkFrames.iterator();
		while(it.hasNext()){
			TalkFrame tf = it.next();
			if(tf.getHostAddress().equals(user.getHostAddress()))
				return tf;
		}
		return null;
	}
	
	public void addTalkFrame(TalkFrame tf){
		this.talkFrames.add(tf);
	}
	
	/**
	 * �յ��û���½�㲥��Ϣʱ���������û��б�,����û��Ѵ����б��������
	 * @param hostAddress 
	 * @param hostName 
	 */
	public void addUser(User user) {
		for(int i=0;i<users.size();i++){
			User u = users.get(i);
			if(u.getHostAddress().equals(user.getHostAddress())){
				System.out.println("���û��Ѵ���");
				return;
			}
		}
		users.add(user);
		userList.setListData(users.toArray());
		label.setText(UIUtils.getLabelString(users.size()));
	}

	/**
	 * �յ��û��˳��㲥��Ϣʱ���������û��б�
	 */
	public void removeUser(User user) {
		for(int i=0;i<this.users.size();i++){
			User u = this.users.get(i);
			if(u.getHostAddress().equals(user.getHostAddress())){
				users.remove(i);
				break;
			}
		}
		userList.setListData(users.toArray());
		label.setText(UIUtils.getLabelString(users.size()));
	}

	 
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel());  
			SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
			SubstanceImageWatermark watermark  =   new  SubstanceImageWatermark("images/1.jpg");
			SubstanceImageWatermark.setKind(ImageWatermarkKind.SCREEN_TILE);
			SubstanceLookAndFeel.setCurrentWatermark(watermark);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		UIUtils.initGlobalFont(new Font("��������", Font.PLAIN, 14));
		new MainFrame();
	}
	
	private JLabel label;
	private JList userList;
	private JButton refresh;
	
}

class MyListCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	final ImageIcon userIcon = UIUtils.getImageIcon("images/user_48.png");
	Border unselectedBorder = null;
	Border selectedBorder = null;
	public MyListCellRenderer(){
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		User user = (User)value;
		this.setLayout(new BorderLayout());
		setText("<html><font color='blue'>"+user.getUserName()+"("+user.getHostName()+")</font><br/><font color='#A9A9A9'>"+
					user.getHostAddress()+"</font></html>");
		setIcon(userIcon);
		setToolTipText("˫����ʼ����");
		if (isSelected) {
			if (selectedBorder == null) {
				selectedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.blue);
			}
			setBorder(selectedBorder);
			setBackground(Config.SYS_COLOR_SELECTED);
		} else {
			if (unselectedBorder == null) {
				unselectedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.gray);
			}
			setBorder(unselectedBorder);
			setBackground(Color.white);
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		return this;
	}

}
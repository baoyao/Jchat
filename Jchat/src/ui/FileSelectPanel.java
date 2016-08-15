package ui;

import handler.MessageSender;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import util.BasicUtils;
import util.Config;
import util.UIUtils;
import exception.MessageOverflowException;

@SuppressWarnings("serial")
public class FileSelectPanel extends JPanel implements ActionListener {
	
	public FileSelectPanel(TalkFrame parentFrame) {
		this.parentFrame = parentFrame;
		initComponents();
	}

	private void initComponents() {	
		this.fileSendProgressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		this.fileSendProgressBar.setStringPainted(true);
		this.fileReceiveProgressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		this.fileReceiveProgressBar.setStringPainted(true);
		sendFileBtn = new JButton("����",UIUtils.getImageIcon("images/accept.png"));
		sendFileBtn.setToolTipText("����");
		cancelBtn = new JButton("ȡ��",UIUtils.getImageIcon("images/cancel2.png"));
		cancelBtn.setToolTipText("�ر�");
		cancelSendBtn = new JButton(UIUtils.getImageIcon("images/cancel2.png"));
		cancelSendBtn.setToolTipText("ȡ������");
		cancelReceiveBtn = new JButton(UIUtils.getImageIcon("images/cancel2.png"));
		cancelReceiveBtn.setToolTipText("ȡ������");
		addBtn = new JButton("���",UIUtils.getImageIcon("images/add.png"));
		addBtn.setToolTipText("����ļ�");
		delBtn = new JButton("ɾ��",UIUtils.getImageIcon("images/delete.png"));
		delBtn.setToolTipText("ɾ���ļ�");
		receiveBtn = new JButton("����",UIUtils.getImageIcon("images/accept.png"));
		receiveBtn.setToolTipText("����");
		refuseBtn = new JButton("�ܾ�",UIUtils.getImageIcon("images/cancel2.png"));
		refuseBtn.setToolTipText("�ܾ�");
		sendFileBtn.setFocusable(false);
		addBtn.setFocusable(false);
		delBtn.setFocusable(false);
		cancelBtn.setFocusable(false);
		cancelSendBtn.setFocusable(false);
		cancelReceiveBtn.setFocusable(false);
		receiveBtn.setFocusable(false);
		refuseBtn.setFocusable(false);
		receiveBtn.setEnabled(false);
		refuseBtn.setEnabled(false);
		
		JLabel fileSendLabel = new JLabel("�����ļ���");
		JLabel fileReceiveLabel = new JLabel("�����ļ���");
		fileSendTable = new JTable(new MyTableModel(new Object[][]{},new String[]{"״̬","�ļ���","�ļ���С","�ļ�·��","������_�ļ���ʵ��С"}));
		fileSendTable.setSelectionBackground(Config.SYS_COLOR_SELECTED);
		fileSendTable.setSelectionForeground(fileSendTable.getForeground());
		TableColumn c = fileSendTable.getColumnModel().getColumn(0);
		c.setCellRenderer(new StateRenderer());
		fileSendTable.setRowHeight(fileSendTable.getRowHeight()+5);
		fileSendTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //�ر��Զ�������С
		fileSendTable.getColumnModel().getColumn(0).setPreferredWidth(85);
		fileSendTable.getColumnModel().getColumn(1).setPreferredWidth(198);
		fileSendTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		//�������д���ʵ��������
		fileSendTable.getColumnModel().getColumn(3).setMinWidth(0);
		fileSendTable.getColumnModel().getColumn(3).setMaxWidth(0);
		fileSendTable.getColumnModel().getColumn(4).setMinWidth(0);
		fileSendTable.getColumnModel().getColumn(4).setMaxWidth(0);
		
		fileReceiveTable = new JTable(new MyTableModel(new Object[][]{},new String[]{"״̬","�ļ���","�ļ���С","���·��"}));
		fileReceiveTable.setSelectionBackground(Config.SYS_COLOR_SELECTED);
		fileReceiveTable.setSelectionForeground(fileSendTable.getForeground());
		TableColumn sc = fileReceiveTable.getColumnModel().getColumn(0);
		sc.setCellRenderer(new StateRenderer());
		fileReceiveTable.setRowHeight(fileReceiveTable.getRowHeight()+5);
		fileReceiveTable.getColumnModel().getColumn(0).setPreferredWidth(85);
		fileReceiveTable.getColumnModel().getColumn(1).setPreferredWidth(198);
		fileReceiveTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		fileReceiveTable.getColumnModel().getColumn(3).setMinWidth(0);
		fileReceiveTable.getColumnModel().getColumn(3).setMaxWidth(0);
		fileReceiveTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //�ر��Զ�������С
		
		JScrollPane fileSendScrollPane = new JScrollPane(fileSendTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane fileReceiveScrollPane = new JScrollPane(fileReceiveTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		receiveToolBar = new JToolBar();
		receiveToolBar.setBorderPainted(false);
		receiveToolBar.add(receiveBtn);
		receiveToolBar.add(refuseBtn);
		//receiveToolBar.add(cancelBtn);
		receiveCardLayout = new CardLayout();
		receiveCardPanel = new JPanel(receiveCardLayout);
		receiveCardPanel.add(receiveToolBar, "receiveToolBar");
		receiveProgressPanel = new JPanel(new BorderLayout());
		receiveProgressPanel.add(fileReceiveProgressBar,BorderLayout.CENTER);
		receiveProgressPanel.add(cancelReceiveBtn,BorderLayout.EAST);
		receiveCardPanel.add(receiveProgressPanel,"receiveProgressBar");
		JPanel fileReceiveNorthPanel = new JPanel(new BorderLayout());
		fileReceiveNorthPanel.add(fileReceiveLabel,BorderLayout.WEST);
		fileReceiveNorthPanel.add(receiveCardPanel,BorderLayout.CENTER);
		JPanel fileReceivePanel = new JPanel(new BorderLayout(0,0));
		fileReceivePanel.add(fileReceiveNorthPanel,BorderLayout.NORTH);
		fileReceivePanel.add(fileReceiveScrollPane,BorderLayout.CENTER);
		
		sendToolBar = new JToolBar();
		sendToolBar.setBorderPainted(false);
		sendToolBar.add(addBtn);
		sendToolBar.add(delBtn);
		sendToolBar.add(sendFileBtn);
		sendCardLayout = new CardLayout();
		sendCardPanel = new JPanel(sendCardLayout);
		sendCardPanel.add(sendToolBar, "sendToolBar");
		sendProgressPanel = new JPanel(new BorderLayout());
		sendProgressPanel.add(fileSendProgressBar,BorderLayout.CENTER);
		sendProgressPanel.add(cancelSendBtn,BorderLayout.EAST);
		sendCardPanel.add(sendProgressPanel,"sendProgressBar");
		JPanel fileSendNorthPanel = new JPanel(new BorderLayout());
		fileSendNorthPanel.add(fileSendLabel,BorderLayout.WEST);
		fileSendNorthPanel.add(sendCardPanel,BorderLayout.CENTER);
		JPanel fileSendPanel = new JPanel(new BorderLayout(0,0));
		fileSendPanel.add(fileSendNorthPanel,BorderLayout.NORTH);
		fileSendPanel.add(fileSendScrollPane,BorderLayout.CENTER);
		

		sendFileBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		cancelSendBtn.addActionListener(this);
		cancelReceiveBtn.addActionListener(this);
		addBtn.addActionListener(this);
		delBtn.addActionListener(this);	
		receiveBtn.addActionListener(this);
		refuseBtn.addActionListener(this);
		
		popMenu = new JPopupMenu();
		JMenuItem addFile = new JMenuItem("����");
		popMenu.add(addFile);
		this.add(popMenu);

		fileSendTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					//��ѡ���һ�ʱ������ڵ���
					int row = fileSendTable.rowAtPoint(e.getPoint());
			        if(row>=0)
			        	fileSendTable.setRowSelectionInterval(row,row);
			        //�ٵ����һ��˵�
			        popMenu.show(fileSendTable, e.getX(), e.getY());
					int r = fileSendTable.getSelectedRow(); // ��õ�ǰѡ�е��к�
					String ID = (String) fileSendTable.getValueAt(r, 0);
					System.out.println(ID);
				}
			}
		});

/*		JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		south.add(cancelBtn);*/
		
		JPanel center = new JPanel(new GridLayout(2,1,0,3));
		center.add(fileReceivePanel);
		center.add(fileSendPanel);
		
		this.setLayout(new BorderLayout(0, 0));
		this.add(center, BorderLayout.CENTER);
/*		this.add(south, BorderLayout.SOUTH);
*/
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelBtn){
			parentFrame.hideFilePanel();
		}else if(e.getSource()==addBtn){
			addFile();
		}else if(e.getSource()==delBtn){
			delFile();
		}else if(e.getSource()==sendFileBtn){
			sendFile();
		}else if(e.getSource()==receiveBtn){
			receiveFile();
		}else if(e.getSource()==refuseBtn){
			refuseFile();
		}
	}
	
	private void receiveFile() {
		JFileChooser fc = new JFileChooser(); 
	    fc.setFileSelectionMode ( JFileChooser.DIRECTORIES_ONLY );
	    if ( fc.showOpenDialog ( this ) == JFileChooser.CANCEL_OPTION ) return;
	    //�˴���Ӧ������udp���͸��Է���Ϣ����Ϊ�������ܵ���udp��Ϣ��tcp����������ɲ�ͬ�����߼�����
	    //��Ҫ���ͽ�����ϢҲӦ��ͨ��tcp���ӷ���
	    //MessageSender.sendAcceptFileMsg(this.parentFrame.getHostAddress());
	    File file = fc.getSelectedFile();
	    String path = file.getAbsolutePath();
	    this.parentFrame.setSavePath(path);
	    this.parentFrame.startAcceptFile();
	    this.disableOperateBtn();
	    this.showReceiveProgressBar();
	}
	
	private void refuseFile() {
		String sendTime = BasicUtils.formatDate(new Date());
		this.parentFrame.appendSysMsg(Config.REFUSE_FILE_MSG_TIP_SELF+" "+sendTime);
		this.disableOperateBtn();
		this.recoverReceiveTable();
		MessageSender.sendRefuseFileMsg(this.parentFrame.getHostAddress());
	}

	public void enableOperateBtn(){
		this.receiveBtn.setEnabled(true);
		this.refuseBtn.setEnabled(true);
	}
	
	private void disableOperateBtn(){
		this.receiveBtn.setEnabled(false);
		this.refuseBtn.setEnabled(false);
	}

	public void showSendProgressBar(){
		System.out.println("showing send progress bar....");
		this.sendCardLayout.show(this.sendCardPanel,"sendProgressBar");
	}
	
	public void recoverSendPanel(){
		System.out.println("recovering send panel...");
		this.fileSendProgressBar.setValue(0);
		this.fileSendProgressBar.setString("");
		this.fileSendProgressBar.setToolTipText("");
		this.sendCardLayout.show(this.sendCardPanel,"sendToolBar");
		((MyTableModel)(fileSendTable.getModel())).setRowCount(0);
	}
	
	public void showReceiveProgressBar(){
		this.receiveCardLayout.show(this.receiveCardPanel, "receiveProgressBar");
	}
	
	public void recoverReceivePanel(){
		System.out.println("recovering receive panel...");
		this.fileReceiveProgressBar.setValue(0);
		this.fileReceiveProgressBar.setString("");
		this.fileReceiveProgressBar.setToolTipText("");
		this.receiveCardLayout.show(this.receiveCardPanel,"receiveToolBar");
		((MyTableModel)(fileReceiveTable.getModel())).setRowCount(0);
	}
	
	public void updateSendProgress(int value,String info,String toolTip){
		this.fileSendProgressBar.setToolTipText(toolTip);
		this.fileSendProgressBar.setString(info);
		this.fileSendProgressBar.setValue(value);
	}
	
	public void updateReceiveProgress(int value,String info, String toolTip){
		this.fileReceiveProgressBar.setToolTipText(toolTip);
		this.fileReceiveProgressBar.setString(info);
		this.fileReceiveProgressBar.setValue(value);
	}
	
	public void recoverReceiveTable(){
		((MyTableModel)(fileReceiveTable.getModel())).setRowCount(0);
	}
	
	private void sendFile() {
		int count = fileSendTable.getRowCount(); //�ļ���Ŀ
		if(count==0){
			UIUtils.showAlertMessage(this, "û��Ҫ���͵��ļ���������ӣ�");
			return;
		}
		StringBuilder fileNamesStr = new StringBuilder();
		StringBuilder fileLengthsStr = new StringBuilder();
		MyTableModel tm = (MyTableModel)(fileSendTable.getModel());
		String fileNames[] = new String[count];
		long fileLengths[] = new long[count];
		for(int i=0;i<count;i++){
			String fileName = (String) tm.getValueAt(i, 1);
			String fileLength = (String) tm.getValueAt(i, 4);
			if(i!=0){
				fileNamesStr.append("|");
				fileLengthsStr.append("|");
			}
			fileNamesStr.append(fileName); //����������ļ���
			fileLengthsStr.append(fileLength);
			fileNames[i] = (String) tm.getValueAt(i, 3); //�ļ�����·��
			fileLengths[i] = Long.parseLong(fileLength);
		}
		this.parentFrame.startFileServer(fileNames, fileLengths); // �������ļ����ͷ�����
		System.out.println("���͵����ݣ�=================");
		System.out.println(fileNamesStr);
		System.out.println(fileLengthsStr);
		System.out.println(Arrays.toString(fileNames));
		System.out.println(Arrays.toString(fileLengths));
		//�ٷ��ʹ����ļ���Ϣ
		try {
			MessageSender.sendRequestFileSendMsg(this.parentFrame.getHostAddress(), fileNamesStr.toString(),fileLengthsStr.toString());
			this.fileSendProgressBar.setString("�ȴ��Է����գ����Ժ�...");
			this.showSendProgressBar();
		} catch (MessageOverflowException e) {
			e.printStackTrace();
			UIUtils.showErroMessage(this, e.getMessage());
			this.parentFrame.stopFileServer();
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showErroMessage(this, e.toString());
			this.parentFrame.stopFileServer();
		}
		
	}

	private void delFile() {
		int r = fileSendTable.getSelectedRow(); // ��õ�ǰѡ�е��к�
		if(r==-1) {
			UIUtils.showAlertMessage(this, "����ѡ��Ҫɾ������");
			return;
		}
		while(r != -1){
			((MyTableModel)(fileSendTable.getModel())).removeRow(r);
			r =  fileSendTable.getSelectedRow(); 
		}
		
	}

	private void addFile() {
	    JFileChooser fc = new JFileChooser(); 
	    fc.setMultiSelectionEnabled ( true );
	    fc.setFileSelectionMode ( JFileChooser.FILES_ONLY );
	    if ( fc.showOpenDialog ( this ) == JFileChooser.CANCEL_OPTION ) return;
	    File []file = fc.getSelectedFiles ();
	    for (int i = 0 ; i <file.length ; i ++){
	        ((MyTableModel)(fileSendTable.getModel())).addRow ( 
	        		new String [] {Config.STATE_WAITING,file[i].getName (),BasicUtils.convertByte(file[i].length()),
	        				file[i].getAbsolutePath(),String.valueOf(file[i].length())});
		    //������������Ϊѡ����
		    int lastRowIndex = fileSendTable.getRowCount()-1;
		    fileSendTable.clearSelection();
		    fileSendTable.addRowSelectionInterval(lastRowIndex, lastRowIndex);
	    }
	}
	
	public void updateReceiveTable(String[] fileNames, long[] fileLengths) {
		    for (int i = 0 ; i <fileNames.length ; i ++){
		        ((MyTableModel)(fileReceiveTable.getModel())).addRow( 
		        		new String [] {Config.STATE_WAITING, fileNames[i],BasicUtils.convertByte(fileLengths[i]),""});
		    }
	}
	/**
	 * ���·����ļ�����״̬�ֶ�
	 * index : �к�
	 * state ��״ֵ̬
	 */
	public void updateSendTableState(int index,String state){
		this.fileSendTable.setValueAt(state, index, 0);
		if(state.equals(Config.STATE_SENDING)){
			this.fileSendTable.clearSelection();
			this.fileSendTable.addRowSelectionInterval(index, index);
		}
	}
	/**
	 * ���½����ļ�����״̬�ֶ�
	 * index : �к�
	 * state ��״ֵ̬
	 */
	public void updateReceiveTableState(int index,String state){
		this.fileReceiveTable.setValueAt(state, index, 0);
		if(state.equals(Config.STATE_ACCEPTING)){
			this.fileReceiveTable.clearSelection();
			this.fileReceiveTable.addRowSelectionInterval(index, index);
		}
	}
	    

	private JButton sendFileBtn;
	private JButton cancelBtn;
	private JButton addBtn;
	private JButton delBtn;
	private JButton receiveBtn;
	private JButton refuseBtn;
	private JButton cancelSendBtn;
	private JButton cancelReceiveBtn;
	private JTable fileSendTable;
	private JTable fileReceiveTable;
	private JPopupMenu popMenu;
	private JProgressBar fileSendProgressBar;
	private JProgressBar fileReceiveProgressBar;
	private JToolBar receiveToolBar;
	private JToolBar sendToolBar;
	private JPanel sendCardPanel ;
	private JPanel receiveCardPanel ;
	private JPanel sendProgressPanel ;
	private JPanel receiveProgressPanel ;
	private CardLayout sendCardLayout;
	private CardLayout receiveCardLayout;
	private TalkFrame parentFrame;

}

@SuppressWarnings("serial")
class MyTableModel extends DefaultTableModel{
	public MyTableModel(Object[][] data, Object[] columnNames){
		super(data,  columnNames) ;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

class StateRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setOpaque(true);
		if(isSelected){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		String v = (String)value;
		if(v.equals(Config.STATE_WAITING)){
			 setIcon(UIUtils.getImageIcon("images/wait.png"));
			 setText(Config.TEXT_STATE_WAITING);
		}else if(v.equals(Config.STATE_SENDING)){
			 setIcon(UIUtils.getImageIcon("images/basket_remove.png"));
			 setText(Config.TEXT_STATE_SENDING);
		}else if(v.equals(Config.STATE_ACCEPTING)){
			 setIcon(UIUtils.getImageIcon("images/basket_put.png"));
			 setText(Config.TEXT_STATE_ACCEPTING);
		}else if(v.equals(Config.STATE_COMPLETED)){
			 setIcon(UIUtils.getImageIcon("images/completed.png"));
			 setText(Config.TEXT_STATE_COMPLETED);
		}
		return this;
	}
}

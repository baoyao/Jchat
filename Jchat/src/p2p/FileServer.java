package p2p;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import ui.TalkFrame;
import util.BasicUtils;
import util.Config;

public class FileServer extends Thread{
	private TalkFrame parentFrame;
	private static final String TAG = "SERVER INFO:";
	private ServerSocket serverSocket;
	private String[] fileNames;
	private long[] fileLengths;
	private long progress;
	private int index;
	public FileServer(TalkFrame parentFrame,String[] fileNames,long[] fileLengths){
		this.parentFrame = parentFrame;
		this.fileLengths = fileLengths;
		this.fileNames = fileNames;
		this.progress = 0;
	}
	public void run(){
		if(fileLengths.length ==0 || fileNames.length == 0)
			return;
		try {
			serverSocket = new ServerSocket(Config.FILE_TCP_PORT);
			System.out.println(TAG+"�������Ѿ�������");
			Socket socket = serverSocket.accept();
			System.out.println(TAG+"�Է����ӳɹ���");
			BufferedOutputStream socketOS = new BufferedOutputStream(socket.getOutputStream());
			BufferedInputStream socketIS = new BufferedInputStream(socket.getInputStream());
			int len = 0; // ���뻺���ֽ���������ݳ���
			byte buffer[] =  new byte[1024];
			long totalCostTime = 0; //�ܺ�ʱ
			int totalFileCount = 0; //���ļ���Ŀ
			long totalFileLength = 0; //���ļ���С
			
			//new Thread(new WatchThread()).start();
			while( ((len=socketIS.read(buffer))!=-1)){
				String msg = new String(buffer,0,len);
				index = Integer.parseInt(msg); //�Է�����������Ϣ�Ǹ���������ʾ��Ҫ���յ��ļ��ı�ţ�
				System.out.println("�Է�����������ǣ�"+index);
				//�����������һ���ļ����ٷ���һ��������Ϣ����ֵ�����ļ�������������ȫ��������ϣ���رշ�����
				if(index==(fileNames.length)){
					System.out.println("�����˳�ѭ��");
					break;
				}
				System.out.println(TAG+"============================================================");
				System.out.println(TAG+"��ʼ���͵ڡ�"+index+"�����ļ���"+fileNames[index]+"����ȴ�...");
				long startTime = System.currentTimeMillis();
				this.parentFrame.updateSendTableState(index, Config.STATE_SENDING);
				File fileToSend = new File(fileNames[index]);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
				this.progress = 0;
				long fileLength = fileLengths[index];
				while((len=bis.read(buffer))!=-1){
					socketOS.write(buffer, 0, len);
					//������Ϣ
					progress += len;
					String lengthShow = BasicUtils.convertByte(fileLength);
					String info = "("+(index+1)+"/"+fileNames.length+")  "+BasicUtils.convertByte(progress)+"/"+lengthShow+"  "+progress*100/fileLength+"%";
					int pValue = (int)(progress*100/fileLength);
					parentFrame.updateSendProgress(pValue,info,"���ڷ��ͣ�"+fileNames[index]);
				}
				socketOS.flush();
				bis.close();
				System.out.println(TAG+"�ڡ�"+index+"�����ļ�������ϡ�");
				long costTime = (System.currentTimeMillis()-startTime);
				totalCostTime += costTime;
				totalFileCount++;
				totalFileLength += fileLengths[index];
				parentFrame.updateSendTableState(index, Config.STATE_COMPLETED);
				parentFrame.appendSysMsg("�ļ�["+fileNames[index]+"]������ϣ��ļ���С["+BasicUtils.convertByte(fileLengths[index])+"]����ʱ["
							+BasicUtils.convertTime(costTime/1000)+"] "+BasicUtils.formatDate(new Date()));
			}
			socketOS.close();
			socketIS.close();
			socket.close();
			serverSocket.close();
			parentFrame.appendSysMsg("�����ļ�������ϣ��ļ�����Ŀ["+totalFileCount+"]�����ļ��ܴ�С["+BasicUtils.convertByte(totalFileLength)+"]���ܺ�ʱ["+BasicUtils.convertTime(totalCostTime/1000)+"]");
			parentFrame.recoverSendPanel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopServer(){
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}

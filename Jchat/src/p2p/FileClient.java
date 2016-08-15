package p2p;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

import ui.TalkFrame;
import util.BasicUtils;
import util.Config;

public class FileClient extends Thread{
	private static final String TAG = "CLIENT INFO:";
	private TalkFrame talkFrame;
	private String hostAddress;
	private String savePath;
	private String[] fileNames;
	private long[] fileLengths;
	public FileClient(TalkFrame talkFrame){
		this.talkFrame = talkFrame;
		this.hostAddress = talkFrame.getHostAddress();
		this.fileLengths = talkFrame.getFileLengths();
		this.fileNames = talkFrame.getFileNames();
		this.savePath = talkFrame.getSavePath();
		System.out.println(TAG+this.hostAddress+"=="+this.savePath);
		System.out.println(TAG+Arrays.toString(this.fileNames));
		System.out.println(TAG+Arrays.toString(this.fileLengths));
	}
	
	public void run(){
		try {
			Socket socket = new Socket(hostAddress,Config.FILE_TCP_PORT);
			BufferedOutputStream socketOS = new BufferedOutputStream(socket.getOutputStream());
			BufferedInputStream socketIS = new BufferedInputStream(socket.getInputStream());
			byte buffer[] =  new byte[1024];
			int index = 0; 
			long totalCostTime = 0; //�ܺ�ʱ
			int totalFileCount = 0; //���ļ���Ŀ
			long totalFileLength = 0; //���ļ���С
			while(index<fileNames.length){ 
				int len = 0;
				long progress = 0;
				long fileLength = fileLengths[index];
				long startTime = System.currentTimeMillis();
				socketOS.write(String.valueOf(index).getBytes()); //�ȷ���һ����Ϣ��֪ͨ��������һ��Ҫ���յ��ļ����
				socketOS.flush();
				this.talkFrame.updateReceiveTableState(index, Config.STATE_ACCEPTING);
				String filePath = this.savePath+"\\"+fileNames[index];
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				while(progress<fileLengths[index]  && ((len=socketIS.read(buffer))!=-1)){
					bos.write(buffer, 0, len);
					//������Ϣ
					progress += len;
					String lengthShow = BasicUtils.convertByte(fileLength);
					String info = "("+(index+1)+"/"+fileNames.length+")  "+BasicUtils.convertByte(progress)+"/"+lengthShow+"  "+progress*100/fileLength+"%";
					int pValue = (int)(progress*100/fileLength);
					talkFrame.updateReceiveProgress(pValue,info,"���ڽ��գ�"+fileNames[index]);
				}
				bos.flush();
				bos.close();
				System.out.println(TAG+"�ڡ�"+index+"�����ļ�������ϡ�");
				long costTime = (System.currentTimeMillis()-startTime);
				totalCostTime += costTime;
				totalFileCount++;
				totalFileLength += fileLengths[index];
				this.talkFrame.updateReceiveTableState(index, Config.STATE_COMPLETED);
				talkFrame.appendSysMsg("�ļ�["+fileNames[index]+"]������ϣ��ļ���С["+BasicUtils.convertByte(fileLengths[index])+"]����ʱ["
						+BasicUtils.convertTime(costTime/1000)+"] "+BasicUtils.formatDate(new Date()));
				index++;
			}
			talkFrame.recoverReceiveTable();
			//���ȫ����������Ҫ�ٷ���һ����Ϣ
			socketOS.write(String.valueOf(index).getBytes());
			socketOS.flush();
			socketIS.close();
			socketOS.close();
			socket.close();
			talkFrame.appendSysMsg("�����ļ�������ϣ��ļ�����Ŀ["+totalFileCount+"]�����ļ��ܴ�С["+BasicUtils.convertByte(totalFileLength)+"]���ܺ�ʱ["+BasicUtils.convertTime(totalCostTime/1000)+"]");
			talkFrame.recoverReceivePanel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package bean;

public class User {
	private String hostName; //���������û����������ƣ��磺Dell-PC
	private String hostAddress; //������ַ���û�������IP��ַ�����磺192.168.1.100
	private String macAddress; //����Ӳ����ַ���磺
	private String userName; //�û��Զ���ĵ�¼��������û������磺NeverMore�����û�����ã���Ĭ��ΪloginName��ֵ
	private String remark; //����������Ϣ��Ԥ��
	
	public User(){}
	
	public User(String hostName,String hostAddress,String macAddress,String userName,String remark){
		this.hostName = hostName;
		this.hostAddress = hostAddress;
		this.macAddress = macAddress;
		this.userName = userName;
		this.remark = remark;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostAddress() {
		return hostAddress;
	}
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}

package bean;

public class User {
	private String hostName; //主机名，用户主机的名称，如：Dell-PC
	private String hostAddress; //主机地址，用户主机的IP地址，形如：192.168.1.100
	private String macAddress; //主机硬件地址，如：
	private String userName; //用户自定义的登录本软件的用户名，如：NeverMore，如果没有设置，则默认为loginName的值
	private String remark; //个人描述信息，预留
	
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

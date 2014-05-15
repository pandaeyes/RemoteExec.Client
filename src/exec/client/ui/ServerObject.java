package exec.client.ui;

public class ServerObject {
	
	String domain = "";
	int port = 0;
	String desc = "";
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String toString() {
		return desc + "[" + domain + ":" + port + "]";
	}
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ServerObject) {
			ServerObject srv = (ServerObject)obj;
			return (srv.getDesc().equals(getDesc()) && srv.getDomain().equals(getDomain()) && srv.getPort() == getPort());
		} else {
			return false;
		}
	}
}

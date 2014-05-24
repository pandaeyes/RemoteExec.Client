package exec.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exec.client.ui.ClientFrame;
import exec.client.ui.ServerObject;
import exec.common.ISmsObject;
import exec.common.License;
import exec.proto.SmsObjectS100;
import exec.proto.SmsObjectS101;
import exec.proto.SmsObjectS102;
import exec.proto.SmsObjectS103;
import exec.proto.SmsObjectS104;

public class ClientService {
	
	private final static Logger log = LoggerFactory.getLogger(ClientService.class);
	
	private static ClientService instance = null;
	private List<ServerObject> serverList = null;
	private ClientNio clientNio = null;
	private License license = null;
	private Map<Class, Method> methodList = new HashMap<Class, Method>();
	private ClientFrame frame = null;
	private String version = "test";
	
	private ClientService(){
		Method [] Methods = this.getClass().getMethods();
		for (Method m : Methods) {
			if ((m.getModifiers() & Modifier.PUBLIC) != 0 && "handle".equals(m.getName())) {
				if (!m.getParameterTypes()[0].isInterface())
					methodList.put(m.getParameterTypes()[0], m);
			}
		}
		buildlicenseFile();
		buildServersFile();
	}
	public static ClientService getInstance() {
		if (instance == null)
			instance = new ClientService();
		return instance;
	}

	public List<ServerObject> getServerList() {
		if (serverList == null) {
			serverList = readServers();
		}
		return serverList;
	}
	
	public void closeConnect() {
		if (clientNio != null) {
			clientNio.close();
			clientNio = null;
			frame.closeConnect();
		}
	}
	
	public void switchConnect(ServerObject server) {
		if (clientNio != null) {
			clientNio.close();
			clientNio = null;
		}
		try {
			clientNio = new ClientNio(server.getDomain(), server.getPort());
		} catch(Exception e) {
			log.error("连接失败");
		}
	}
	
	public boolean send(ISmsObject sms) {
		if (clientNio != null) {
			clientNio.getIoSession().write(sms);
			return true;
		} else {
			return false;
		}
	}
	
	public License getLicense() {
		if (license == null) {
			license = readLicense();
		}
		return license;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setFrame(ClientFrame frame) {
		this.frame = frame;
	}
	
	public ISmsObject handle(SmsObjectS100 sms) {
		frame.handle(sms);
		return null;
	}
	
	public ISmsObject handle(SmsObjectS101 sms) {
		frame.handle(sms);
		return null;
	}
	
	public ISmsObject handle(SmsObjectS102 sms) {
		frame.handle(sms);
		return null;
	}
	
	public ISmsObject handle(SmsObjectS103 sms) {
		frame.handle(sms);
		return null;
	}
	
	public ISmsObject handle(SmsObjectS104 sms) {
		frame.handle(sms);
		return null;
	}
	
	public ISmsObject handle(ISmsObject obj) {
		Class c = obj.getClass();
		Method method = methodList.get(c);
		if (method == null) {
			log.error("没有协议的处理方法:" + obj.getClass());
			return null;
		} else {
			Object returnSms = null;
			try {
				returnSms = method.invoke(this, new Object[]{obj});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return (ISmsObject)returnSms;
		}
	}
	
	public void linkError() {
		frame.linkError();
	}
	
	private List<ServerObject> readServers() {
		File file = new File("servers.xml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder; 
		Document document = null; 
		List<ServerObject> list = new ArrayList<ServerObject>();
		try {
			builder = builderFactory.newDocumentBuilder();
		    document = builder.parse(file);
		    Element root = document.getDocumentElement();
		    NodeList nodeList = root.getElementsByTagName("server");
		    int size = nodeList.getLength();
		    for (int i = 0; i < size; i++) {
		    	ServerObject server = new ServerObject();
		    	Element node = (Element)nodeList.item(i);
		    	server.setDomain(node.getAttribute("domain").trim());
		    	server.setPort(Integer.parseInt(node.getAttribute("port").trim()));
		    	server.setDesc(node.getAttribute("desc").trim());
		    	list.add(server);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private License readLicense() {
		License license = new License();
		File file = new File("license.xml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder; 
		Document document = null; 
		try {
			builder = builderFactory.newDocumentBuilder();
		    document = builder.parse(file);
		    Element root = document.getDocumentElement();
		    NodeList nodeList = root.getChildNodes();
		    int size = nodeList.getLength();
		    for (int i = 0; i < size; i++) {
		    	Node node = nodeList.item(i);
		    	if (node instanceof Element) {
		    		Element element = (Element)node;
		    		if (element.getTagName().equals("name") && element.getNodeType() == Node.ELEMENT_NODE) {
		    			license.setName(element.getTextContent());
		    		} else if (element.getTagName().equals("signature") && element.getNodeType() == Node.ELEMENT_NODE) {
		    			license.setSignature(element.getTextContent());
		    		} else if (element.getTagName().equals("version") && element.getNodeType() == Node.ELEMENT_NODE) {
		    			version = element.getTextContent();
		    		}
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return license;
	}
	
	private void buildlicenseFile() {
		File file = new File("license.xml");
		if (!file.isFile()) {
			try {
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
				BufferedWriter confBf =new BufferedWriter(write);
				confBf.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
				confBf.write("<License>\r\n");
				confBf.write("<version>20140523</version>\r\n");
				confBf.write("<name></name>\r\n");
				confBf.write("<signature></signature>\r\n");
				confBf.write("</License>\r\n");
				confBf.close();
				write.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void buildServersFile() {
		File file = new File("servers.xml");
		if (!file.isFile()) {
			try {
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
				BufferedWriter confBf =new BufferedWriter(write);
				confBf.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
				confBf.write("<servers>\r\n");
				confBf.write("<server domain=\"127.0.0.1\" port=\"8090\" desc=\"本地测试服\"/>\r\n");
				confBf.write("</servers>\r\n");
				confBf.close();
				write.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

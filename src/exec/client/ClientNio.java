package exec.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exec.common.SmsCodecFactory;

public class ClientNio {
	private final static Logger log = LoggerFactory.getLogger(ClientNio.class);
	
	private IoConnector connector = null;
	private IoSession session = null;

	public ClientNio(String domain, int port) {
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new SmsCodecFactory(Charset.forName(("UTF-8")))));
		connector.setHandler(new ClientHandler()); 
		connector.getSessionConfig().setReadBufferSize(2048*100);
//		((DefaultSocketSessionConfig)connector.getSessionConfig()).setTcpNoDelay(true);
		ConnectFuture future = connector.connect(new InetSocketAddress(domain, port));
		future.addListener(new IoFutureListener<ConnectFuture>() { 
			@Override 
			public void operationComplete(ConnectFuture future) {
				try {
					session = future.getSession();
				} catch(Exception e) {
					ClientService.getInstance().linkError();
					log.error("连接失败");
				}
			} 
		}); 
	}
	
	public IoSession getIoSession() {
		return session;
	}
	
	public void close() {
		if (session != null)
			session.close(true);
		connector.dispose();
	}
}

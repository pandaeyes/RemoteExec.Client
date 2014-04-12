package exec.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends IoHandlerAdapter {

	private final static Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private final String values;

	public ClientHandler(String values) {
		this.values = values;
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
		String str = message.toString();
		log.info("==========clien==messageReceived:" + str);
	}

	@Override
	public void sessionOpened(IoSession session) {
		log.info(values);
		session.write(values);
	}
}

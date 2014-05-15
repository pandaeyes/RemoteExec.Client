package exec.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exec.common.ISmsObject;
import exec.common.License;
import exec.proto.SmsObjectC100;

public class ClientHandler extends IoHandlerAdapter {

	private final static Logger log = LoggerFactory.getLogger(ClientHandler.class);

	public ClientHandler() {
	}
	
	public void sessionOpened(IoSession session) throws Exception {
		License license = ClientService.getInstance().getLicense();
		SmsObjectC100 sms100 = new SmsObjectC100();
		sms100.setName(license.getName());
		sms100.setSignature(license.getSignature());
		session.write(sms100);
    }
	
	public void sessionClosed(IoSession session) throws Exception {
		ClientService.getInstance().closeConnect();
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
		ISmsObject sms = (ISmsObject)message;
		ISmsObject returnSms = ClientService.getInstance().handle(sms);
		if (returnSms != null)
			session.write(returnSms);
	}
	
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.info("exceptionCaught:" + session.getId());
        if (log.isWarnEnabled()) {
        	log.warn("EXCEPTION, please implement " + getClass().getName()
                    + ".exceptionCaught() for proper handling:", cause);
        }
    }
}

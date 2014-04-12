package exec.client;

public class ClientServer {
	
	private static ClientServer server = null;
	
	public static ClientServer getInstance() {
		if (server == null)
			server = new ClientServer();
		return server;
	}

}

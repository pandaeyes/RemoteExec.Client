package exec.client;

public class ClientService {
	
	private static ClientService server = null;
	
	public static ClientService getInstance() {
		if (server == null)
			server = new ClientService();
		return server;
	}

}

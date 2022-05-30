package ga.windpvp.commandsync.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SyncServer {

	/**
	 * A list of connections
	 */
	public static List<SyncConnection> connectionList = new CopyOnWriteArrayList<>();

	/**
	 * The socket of the server
	 */
	private ServerSocket serverSocket;
	
	/**
	 * The password to access the server
	 */
	private String password;

	public void runServer(int port, String password) {
		
		this.password = password;
		
		// Runnable for the server
		Runnable commandServerTask = (() -> {
			try {
				run(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// Start the server on its own thread
		new Thread(commandServerTask).start();

	}

	private void run(int port) throws IOException {
		serverSocket = new ServerSocket(port);

		// Handle new connections on its own thread so the server can process multiple
		// clients
		while (true) {
			this.startConnection(serverSocket.accept());
		}
	}

	private void startConnection(Socket clientSocket) {
		SyncConnection connection = new SyncConnection();
		connection.startConnection(clientSocket);
		connectionList.add(connection);
	}
	
	protected String getPassword() {
		return password;
	}
}

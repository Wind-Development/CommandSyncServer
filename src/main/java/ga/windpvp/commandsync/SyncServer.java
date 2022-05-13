package ga.windpvp.commandsync;

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

	protected void runServer() {
		// Runnable for the statistics server
		Runnable commandServerTask = (() -> {
			try {
				run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// Start the server on its own thread
		new Thread(commandServerTask).start();

	}

	private void run() throws IOException {
		serverSocket = new ServerSocket(500);

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
}

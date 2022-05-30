package ga.windpvp.commandsync.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.proxy.ProxyServer;

import ga.windpvp.commandsync.SyncPlugin;

public class SyncConnection {

	/**
	 * The pool for connections to use
	 */
	private static Executor connectionPool = Executors.newCachedThreadPool();

	/**
	 * The time out for the keep alive in seconds
	 */
	public volatile int keepAliveTimeOutTime = 180;

	/**
	 * Whether this connection has been unregistered
	 */
	private volatile boolean hasUnregistered = false;

	/**
	 * The connection name
	 */
	private String connectionName = "backend-server";

	/**
	 * Starts the keep alive handler
	 */
	private void startKeepAliveHandler() {
		// Handle keepalives
		Runnable keepAliveRunnable = (() -> {

			while (true) {

				// Check for keepalive expiry every second
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// See if the keepalive is expired
				if (keepAliveTimeOutTime == 0 && !hasUnregistered) {
					closeConnection();
				} else if (hasUnregistered) {
					break;
				}

				// Decrement client's keepalive count down
				keepAliveTimeOutTime--;

			}
		});

		// Start keep alive thread
		connectionPool.execute(keepAliveRunnable);
	}

	/**
	 * Closes the connection
	 */
	public void closeConnection() {
		// Deregister
		SyncServer.connectionList.remove(this);

		// Prevent connection from closing twice
		hasUnregistered = true;
	}

	/**
	 * Starts the connection
	 * 
	 * @param clientSocket The client's socket
	 */
	public void startConnection(Socket clientSocket) {
		
		SyncPlugin.getInstance().getLogger().info("New connection from " + this.connectionName + " established.");
		
		// Initializes a new connection from a client
		Runnable connectionRunnable = (() -> {
			try {
				// The connected client's input
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				// Output for communication with client
				PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

				// Handle keep alives
				startKeepAliveHandler();

				// Handle client messages
				String clientInput;
				boolean hasAuthorized = false;
				
				while (true) {

					clientInput = in.readLine();

					if (!hasAuthorized) {
						if (clientInput.contains("password ")) {
							String password = clientInput.replace("password ", "");
							if (!password.equals(SyncPlugin.getInstance().getSyncServer().getPassword())) {
								closeConnection();
								break;
							} else {
								SyncPlugin.getInstance().getLogger().info("Successfully authorized a connection");
								hasAuthorized = true;
							}
						}
						continue;
					}
					
					// Exit connection if keep alive has expired
					if (hasUnregistered) {
						break;
					}

					// Exit connection if told to do so by the client
					if (clientInput.equalsIgnoreCase(".")) {
						closeConnection();
						break;

					} else if (clientInput.toLowerCase().contains("name ")) {

						String finalName = clientInput.toLowerCase().replace("name ", "");
						SyncConnection.this.connectionName = finalName;

					} else if (clientInput.equalsIgnoreCase("keep alive packet")) {

						keepAliveTimeOutTime = 100;

					} else if (clientInput.toLowerCase().contains("run command ")) {

						String processCommand = clientInput.toLowerCase().replace("run command ", "");
						ProxyServer server = SyncPlugin.getInstance().getServer();
						
						SyncPlugin.getInstance().getLogger().info("received command " + processCommand);

						server.getCommandManager().executeAsync(server.getConsoleCommandSource(), processCommand);

						SyncPlugin.getInstance().getLogger().info("ran command " + processCommand);
					} 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// Start the connection with the client on its own thread
		connectionPool.execute(connectionRunnable);
	}

}

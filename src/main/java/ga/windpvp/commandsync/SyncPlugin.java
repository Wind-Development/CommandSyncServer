package ga.windpvp.commandsync;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import ga.windpvp.commandsync.networking.SyncServer;

@Plugin(id = "commandsyncserver", name = "Command Sync Server", version = "0.0.1-SNAPSHOT", description = "Make the Velocity proxy run commands based on backend input!", authors = {
		"windcolor-dev" })
public class SyncPlugin {

	private final ProxyServer server;
	private final Logger logger;

	private static SyncPlugin INSTANCE;
	private SyncServer syncServer;

	@Inject
	public SyncPlugin(ProxyServer server, Logger logger) {

		this.server = server;
		this.logger = logger;

	}
	
	@Subscribe
	public void onInitialization(ProxyInitializeEvent event) {
		INSTANCE = this;
		
		File config = new File("plugins/commandsync/config.txt");
		int port = 1500;
		
		if (!config.exists()) {
			try {
				config.createNewFile();
				Scanner myReader = new Scanner(config);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					
					if (data.toLowerCase().contains("port=")) {
					 	port = Integer.valueOf(data.replace("port=", ""));
					 	logger.info("Loaded config!");
					 	break;
					}
					
				}
				myReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		syncServer = new SyncServer();
		syncServer.runServer(port);
	}

	public ProxyServer getServer() {
		return server;
	}

	public Logger getLogger() {
		return logger;
	}

	public static SyncPlugin getInstance() {
		return INSTANCE;
	}
}
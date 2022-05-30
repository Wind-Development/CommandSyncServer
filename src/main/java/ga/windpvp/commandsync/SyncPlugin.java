package ga.windpvp.commandsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
		String password = "defaultPassword";
		
		if (!config.exists()) {
			try {
				if (!config.getParentFile().exists()) {
					config.getParentFile().mkdirs();
				}
				config.createNewFile();
				
				FileWriter writer = new FileWriter(config);
				
				writer.write("port=1500\n");
				writer.write("password=defaultPassword");
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Scanner reader = null;
		try {
			reader = new Scanner(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (reader.hasNextLine()) {
			String data = reader.nextLine();
			
			// Port
			if (data.toLowerCase().contains("port=")) {
			 	port = Integer.valueOf(data.replace("port=", ""));
			}
			
			// Password
			if (data.toLowerCase().contains("password=")) {
				password = data.replace("password=", "");
			}
			
		}
	 	logger.info("Loaded config!");

		reader.close();
		if (password.equals("defaultPassword")) {
			logger.warn("The command sync server is running with the default password, it is recommended to use a custom one");
		}
		
		syncServer = new SyncServer();
		syncServer.runServer(port, password);
	}

	public ProxyServer getServer() {
		return server;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public SyncServer getSyncServer() {
		return syncServer;
	}

	public static SyncPlugin getInstance() {
		return INSTANCE;
	}
}
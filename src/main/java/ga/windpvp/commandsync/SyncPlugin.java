package ga.windpvp.commandsync;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "commandsync", name = "Command Sync", version = "0.0.1-SNAPSHOT", description = "Make the Velocity proxy run commands based on backend input!", authors = {
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
		
		syncServer = new SyncServer();
		syncServer.runServer();
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
package com.lenkp.asteriskmonitor.service;

import java.util.logging.Logger;

/**
 * This class is for accessing the services
 * A service is a singleton object
 * 
 * @author mbahhalim
 *
 */
public class ServiceLocator {

	public final static Logger LOGGER =
			Logger.getLogger(ServiceLocator.class.getName());
	
	private final static BridgeService BRIDGE_SERVICE =
			new BridgeServiceProvider();
	
	private final static ChannelService CHANNEL_SERVICE =
			new ChannelServiceProvider();
	
	private final static EndpointService ENDPOINT_SERVICE =
			new EndpointServiceProvider();
	
	private final static DatabaseService DATABASE_SERVICE =
			new DatabaseServiceProvider();
	
	private final static RemoteSshService REMOTE_SERVICE =
			new RemoteSshServiceProvider();
	
	/**
	 * @return an instance of bridge service
	 */
	public static BridgeService getBridgeService() {
		LOGGER.info("Get instance for bridge service");
		return BRIDGE_SERVICE;
	}
	
	/**
	 * @return an instance of channel service
	 */
	public static ChannelService getChannelService() {
		LOGGER.info("Get instance for channel service");
		return CHANNEL_SERVICE;
	}
	
	/**
	 * @return an instance of endpoint service
	 */
	public static EndpointService getEndpointService() {
		LOGGER.info("Get instance for endpoint service");
		return ENDPOINT_SERVICE;
	}
	
	public static DatabaseService getDatabaseService() {
		LOGGER.info("Get instance for sqlite service");
		return DATABASE_SERVICE;
	}
	
	public static RemoteSshService getRemoteSshService() {
		LOGGER.info("Get instance Remote SSH service");
		return REMOTE_SERVICE;
	}
	
}

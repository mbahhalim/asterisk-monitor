package com.lenkp.asteriskmonitor.service;

import java.util.logging.Logger;

public class ServiceLocator {

	public final static Logger LOGGER =
			Logger.getLogger(ServiceLocator.class.getName());
	
	private final static BridgeService BRIDGE_SERVICE =
			new BridgeServiceProvider();
	
	private final static ChannelService CHANNEL_SERVICE =
			new ChannelServiceProvider();
	
	private final static EndpointService ENDPOINT_SERVICE =
			new EndpointServiceProvider();
	
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
	
}

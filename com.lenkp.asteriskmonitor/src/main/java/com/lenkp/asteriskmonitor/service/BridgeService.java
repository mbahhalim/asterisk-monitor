package com.lenkp.asteriskmonitor.service;

import java.util.List;

import ch.loway.oss.ari4java.generated.Bridge;

/**
 * Type for bridge service
 * 
 * @author mbahhalim
 *
 */
public interface BridgeService {

	/**
	 * @return a list of bridge fetched from Asterisk
	 */
	public List<Bridge> getBridges();
	
}

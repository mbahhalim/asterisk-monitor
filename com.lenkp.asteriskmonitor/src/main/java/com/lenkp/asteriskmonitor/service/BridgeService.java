package com.lenkp.asteriskmonitor.service;

import java.util.List;

import ch.loway.oss.ari4java.generated.Bridge;

public interface BridgeService {

	/**
	 * @return a list of bridge fetched from Asterisk
	 */
	public List<Bridge> getBridges();
	
}

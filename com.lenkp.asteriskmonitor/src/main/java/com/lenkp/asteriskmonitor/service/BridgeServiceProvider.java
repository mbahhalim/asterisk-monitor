package com.lenkp.asteriskmonitor.service;

import java.util.List;
import java.util.logging.Logger;

import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.tools.RestException;

class BridgeServiceProvider implements BridgeService {

	public final static Logger LOGGER =
			Logger.getLogger(BridgeServiceProvider.class.getName());
	
	/* (non-Javadoc)
	 * @see com.lenkp.asteriskmonitor.service.BridgeService#getBridges()
	 */
	@Override
	public List<Bridge> getBridges() {
		LOGGER.info("Fetching bridges from Asterisk");
		try {
			return AsteriskManager.getAri().bridges().list();
		} catch (RestException e) {
			LOGGER.severe("Failed fetching bridges from Asterisk");
			e.printStackTrace();
			return null;
		}
	}
	
}

package com.lenkp.asteriskmonitor.service;

import java.util.List;
import java.util.logging.Logger;

import ch.loway.oss.ari4java.generated.Endpoint;
import ch.loway.oss.ari4java.tools.RestException;

class EndpointServiceProvider implements EndpointService {

	public final static Logger LOGGER =
			Logger.getLogger(EndpointServiceProvider.class.getName());
	
	/* (non-Javadoc)
	 * @see com.lenkp.asteriskmonitor.service.EndpointService#getEndpoints()
	 */
	@Override
	public List<Endpoint> getEndpoints() {
		LOGGER.info("Fetching endpoints from Asterisk");
		try {
			return AsteriskManager.getAri().endpoints().list();
		} catch (RestException e) {
			LOGGER.severe("Failed fetching endpoints from Asterisk");
			e.printStackTrace();
			return null;
		}
	}
	
}

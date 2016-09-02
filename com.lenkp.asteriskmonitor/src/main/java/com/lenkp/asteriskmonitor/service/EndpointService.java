package com.lenkp.asteriskmonitor.service;

import java.util.List;

import ch.loway.oss.ari4java.generated.Endpoint;

public interface EndpointService {

	/**
	 * @return a list of endpoint fetched from Asterisk
	 */
	public List<Endpoint> getEndpoints();
	
}

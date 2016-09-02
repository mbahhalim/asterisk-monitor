package com.lenkp.asteriskmonitor.service;

import java.util.List;

import ch.loway.oss.ari4java.generated.Channel;

public interface ChannelService {

	/**
	 * @return a list of channel fetched from Asterisk
	 */
	public List<Channel> getChannels();
	
	/**
	 * Hanging up a channel
	 * 
	 * @param channelId
	 */
	public void hangUpChannel(String channelId);
	
}

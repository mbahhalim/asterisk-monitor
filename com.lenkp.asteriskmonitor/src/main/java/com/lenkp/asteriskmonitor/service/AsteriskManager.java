package com.lenkp.asteriskmonitor.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.lenkp.asteriskmonitor.gui.AsteriskConnectionSettings;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;

/**
 * This class is for managing Asterisk connection
 * 
 * @author mbahhalim
 *
 */
public final class AsteriskManager {

	public final static Logger LOGGER =
			Logger.getLogger(AsteriskManager.class.getName());
	
	private static ARI ari = null;
	
	/**
	 * @return an ari connection
	 */
	protected static ARI getAri() {
		if (ari == null) LOGGER.warning("Asterisk connection is null");
		return ari;
	}
	
	/**
	 * Create a connection to Asterisk
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public static void createConnection(
			String host,
			String port,
			String username,
			String password) {
		LOGGER.info("Creating a connection to Asterisk");
		try {
			ari = AriFactory.nettyHttp(
					"http://" + host + ":" + port + "/",
					username,
					password,
					AriVersion.ARI_1_9_0);
		} catch (Throwable t) {
			LOGGER.severe("Failed creating connection to Asterisk");
			try {
	            File file = new File("config.properties");
	            Properties properties = new Properties();
	            
	            properties.load(new FileInputStream(file));
	            
	            properties.remove("host");
	            properties.remove("port");
	            properties.remove("username");
	            properties.remove("password");
	            
	            OutputStream out = new FileOutputStream(file);
	            
	            properties.store(out, null);
	            
	            AsteriskConnectionSettings settingFrame = new AsteriskConnectionSettings();
				
				settingFrame.setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			// TODO (Halim) Add warning window
			t.printStackTrace();
		}
	}
	
}

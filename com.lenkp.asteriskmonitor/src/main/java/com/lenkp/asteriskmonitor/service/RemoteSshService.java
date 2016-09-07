package com.lenkp.asteriskmonitor.service;

public interface RemoteSshService {

	public void connectToHost (String host, String username, String password);
	public void openSFTP ();
	public void takePTUInfo (String pathToFile);
	public String getPTULinphoneUsername ();
	public String getPTULinphonePassword ();
	public String getPTULinphoneHost ();
	public void replacePTULinphoneInfo (String username, String password, String host);
	public void disconnect ();
	
}

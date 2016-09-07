package com.lenkp.asteriskmonitor.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

class RemoteSshServiceProvider implements RemoteSshService {

	private JSch jsch;
	private Session session;
	private ChannelSftp sftp;
	private StringBuilder stringBuilder =  new StringBuilder();
	
	public final static Logger LOGGER = 
			Logger.getLogger(RemoteSshServiceProvider.class.getName());
	
	@Override
	public void connectToHost(String host, String username, String password) {
		LOGGER.info("Connecting to host");
		
		jsch = new JSch();
		
		try {
			session = jsch.getSession(username, host);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		session.setPassword(password);
		
    	java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	session.setConfig(config);
    	
    	try {
			session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
    		
	}

	@Override
	public void openSFTP() {
		Channel channel;
		
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getPTULinphoneUsername() {
		return stringBuilder.substring(stringBuilder.toString().indexOf("username=")+9,stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("username=")));
	}

	@Override
	public String getPTULinphonePassword() {
		return stringBuilder.substring(stringBuilder.toString().indexOf("password=")+9,stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("password=")));
	}

	@Override
	public String getPTULinphoneHost() {
		return stringBuilder.substring(stringBuilder.toString().indexOf("domain=")+7,stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("domain=")));
	}

	@Override
	public void replacePTULinphoneInfo(String username, String password, String host) {
		if (username != null) {
			stringBuilder.replace(stringBuilder.toString().indexOf("username=")+9, stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("username=")), username);
		}
		
		if (password != null) {
			stringBuilder.replace(stringBuilder.toString().indexOf("password=")+9, stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("password=")), password);
		}
		
		if (host != null) {
			stringBuilder.replace(stringBuilder.toString().indexOf("host=")+5, stringBuilder.indexOf("\n", stringBuilder.toString().indexOf("host=")), host);
		}
		
		Path currentRelativePath = Paths.get("");
		String currentPathToString = currentRelativePath.toAbsolutePath().toString();
		
		File file = new File(currentPathToString+"/src/linphone.rc");
		try {
			if (file.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Files.write(Paths.get(currentPathToString+"/src/linphone.rc"), stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			System.out.println("failed to write new config to file");
			e.printStackTrace();
		}
		
		try {
			sftp.put(new FileInputStream(file), file.getName());
		} catch (FileNotFoundException | SftpException e) {
			e.printStackTrace();
		}
		
		if (file.delete()) {
			System.out.println("file is deleted");
		} else {
			System.out.println("delete operation is failed");
		}
	}

	@Override
	public void takePTUInfo(String pathToFile) {
		try {
			InputStream obj_InputStream = sftp.get(pathToFile);
			
			char[] ch_Buffer = new char[0x10000];
			
			Reader obj_Reader = new InputStreamReader(obj_InputStream, "UTF-8");
			
			int int_Line = 0;
	   
	        do 
	        {
	          int_Line = obj_Reader.read(ch_Buffer, 0, ch_Buffer.length);
	          
	          if (int_Line > 0) { 
	        	  stringBuilder.append(ch_Buffer, 0, int_Line);
	          }
	          
	        } while (int_Line >= 0);
	        
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		sftp.disconnect();
		session.disconnect();
	}

}

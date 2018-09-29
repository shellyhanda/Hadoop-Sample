package com.shelly.poc.oozie.sshtest;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHTest {

	public static void main(String[] args) {
		System.out.println("Starting to connect....");
try {

	String key=args[0];
	Session session = null;
	JSch jsch = new JSch();
	// new changes
	session = jsch.getSession("zh722e", "charon.cs.boeing.com", Integer.parseInt("22"));
	session.setConfig("StrictHostKeyChecking", "no");
	session.setConfig("PreferredAuthentications", "password");
	jsch.addIdentity("", key.getBytes(), null, null);
	
	System.out.println("before session.isConnected()=" + session.isConnected());
	if (!session.isConnected()) {
		System.out.println("inside if statement session.isConnected()=" + session.isConnected());
		session.connect();
	}
	System.out.println("session.isConnected().."+session.isConnected());
	
	//moveFile(session,"/boeing/sw/ecfd/bin/TransferFileHpcHdp");
	
} catch (Exception e) {
	System.out.println("Error connecting...");
	e.printStackTrace();
}
 }
	public static void moveFile(Session session, InputStream fileToCat, OutputStream outputToFile, String fileToMove,
			String script) throws JSchException, InterruptedException {

		String putOrGet = null;
		// Hadoop to HPC copy
		System.out.println("fileToCat...." + fileToCat);
		if (fileToCat != null) {
			putOrGet = "put";
		}

		// HPC to Hadoop copy
		if (outputToFile != null) {
			putOrGet = "get";
		}

		if (putOrGet != null) {
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			if (putOrGet.equals("get")) {
				channelExec.setOutputStream(outputToFile);
			} else {
				channelExec.setInputStream(fileToCat);
			}

			String command = script + " " + putOrGet + " " + fileToMove;
			System.out.println("Going to run Command ==" + command);
			channelExec.setCommand(command);
			channelExec.connect();

			// This will make channel remain open
				waitToCompleteTask(channelExec);
			
			System.out.println("command executed......");
			channelExec.disconnect();
		}
	}
	private static void waitToCompleteTask(ChannelExec channelExec) throws JSchException, InterruptedException
	{
		System.out.println("I am in waitToCompleteTask..start..........");
		long lStartTime = System.currentTimeMillis();
		boolean notClosedchannelClosed = false;
		do{
			System.out.println("I am in going to sleep..........");
			Thread.sleep(20L);
			System.out.println("I am in out of sleep..........");
			notClosedchannelClosed = channelExec.isClosed();
			System.out.println("After channelClosed.........."+notClosedchannelClosed);
		}while(!notClosedchannelClosed);
		
		long lEndTime = System.currentTimeMillis();
		long output = lEndTime - lStartTime;
		System.out.println("Total time taken to transfer a file is *****"+output/1000);

		System.out.println("I am in waitToCompleteTask..end..........");
	}
	
}

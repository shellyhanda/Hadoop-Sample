package com.demo.oozie.hello;

import org.apache.hadoop.conf.Configuration;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class HelloWorldOozie {

	public static void main(String[] args) throws JSchException {
		System.out.println("Hi Shelly");
		System.out.println("Welcome to Oozie World !!!!");
		
		getSFTPSession() ;

	}
	 public static class DaProcessorLogger implements com.jcraft.jsch.Logger {
		    static java.util.Hashtable name=new java.util.Hashtable();
		    static{
		      name.put(new Integer(DEBUG), "DEBUG: ");
		      name.put(new Integer(INFO), "INFO: ");
		      name.put(new Integer(WARN), "WARN: ");
		      name.put(new Integer(ERROR), "ERROR: ");
		      name.put(new Integer(FATAL), "FATAL: ");
		    }
		    public boolean isEnabled(int level){
		      return true;
		    }
		    public void log(int level, String message){
		      System.err.print(name.get(new Integer(level)));
		      System.err.println(message);
		    }
		}

	 public static Session getSFTPSession() throws JSchException {
		    Session session = null;
		    JSch.setLogger(new DaProcessorLogger());
		    JSch jsch = new JSch();

		    session = jsch.getSession("zh722e", "charon.cs.boeing.com", 22);
		    session.setConfig("StrictHostKeyChecking", "no");
        	session.setConfig("PreferredAuthentications", "password");

		    session.setPassword("Indi9111");
		    System.out.println("before session.isConnected()=" + session.isConnected());
		   
		    if(!session.isConnected()){
		        System.out.println("inside if loop session.isConnected()=" + session.isConnected());
		      session.connect();
		    }
		    System.out.println("after if loop session.isConnected()=" + session.isConnected());
		    return session;
		  }
}

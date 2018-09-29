package hdfsconnection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class SFTPTransferLocal {
	public static void main(String[] args) {
		JSch jsch=new JSch();
		 String user="zh722e";
	      String host="charon.cs.boeing.com";
	      int port=22;
	      try {
	    	  Session session=jsch.getSession(user, host, port);
	  		session.setConfig("StrictHostKeyChecking", "no");
	  		session.setPassword("India123");
	  		if (!session.isConnected()) {
	  			session.connect();
	  		}
	    	 System.out.println("session.isConnected()***********"+session.isConnected());
	          Channel channel=session.openChannel("sftp");
	          channel.connect();
	          ChannelSftp sftpChannel=(ChannelSftp)channel;
	          System.out.println(" ChannelSftp c>>>>>>>>>>"+sftpChannel);
	          String inputFolder="/ptmp/webhfs_test";
	          sftpChannel.cd(inputFolder);
				Vector filelist = sftpChannel.ls(inputFolder);
				for (int i = 0; i < filelist.size(); i++) {
					LsEntry entry = (LsEntry) filelist.get(i);
					System.out.println("entry filename==="+entry.getFilename());
				}
				
				Configuration conf = new Configuration();
			    conf.set("fs.defaultFS", "hdfs://127.0.0.1:8020");
			    conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
			    FileSystem fs = FileSystem.get(conf);
			    
				String fileToCopy="solution_300MB.tar";
				InputStream inputStream = sftpChannel.get(fileToCopy);
				System.out.println("inputStream=="+inputStream);
				OutputStream outputStream = fs.create(new Path("/user/root/test_hdfs/solution_300MB_sftp.tar"));
				System.out.println("Copy started********....");
				IOUtils.copyBytes(inputStream, outputStream, conf, true);
				inputStream.close();
				outputStream.close();
				System.out.println("Done....");
				
		} catch (Exception e) {
			System.out.println("Error in SSH SFTP.....");
			e.printStackTrace();
		}


	}
}


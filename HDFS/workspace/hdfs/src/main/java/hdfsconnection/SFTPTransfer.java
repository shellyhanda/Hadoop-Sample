package hdfsconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Shell.ExitCodeException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SFTPTransfer {

	public static void main(String[] args) {
	
		 JSch jsch=new JSch();
		 String user="zh722e";
	      String host="charon.cs.boeing.com";
	      int port=22;
	      long startTime = System.currentTimeMillis();
	      Session session=null;
	      Channel channel=null;
	      ChannelSftp sftpChannel=null;
	      InputStream inputStream =null;
	      OutputStream outputStream = null;
	      FileSystem fs =null;
	      try {
	    	session=jsch.getSession(user, host, port);
	  		session.setConfig("StrictHostKeyChecking", "no");
	  		session.setPassword("India123");
	  		if (!session.isConnected()) {
	  			session.connect();
	  		}
	    	 System.out.println("session.isConnected()***********"+session.isConnected());
	          channel=session.openChannel("sftp");
	          channel.connect();
	          sftpChannel=(ChannelSftp)channel;
	          System.out.println(" ChannelSftp c>>>>>>>>>>"+sftpChannel);
	          String inputFolder="/ptmp/webhfs_test";
	          sftpChannel.cd(inputFolder);
				/*Vector filelist = sftpChannel.ls(inputFolder);
				for (int i = 0; i < filelist.size(); i++) {
					LsEntry entry = (LsEntry) filelist.get(i);
					System.out.println("entry filename==="+entry.getFilename());
				}*/
				
				Configuration conf = new Configuration();
			    conf.set("fs.defaultFS", "hdfs://BOEINGHDO4:8020");
			    conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
			    fs = FileSystem.get(conf);
			    
				String fileToCopy="solution_300MB.tar";
				if(args.length!=0)
				{
					fileToCopy=args[1];
				}
				System.out.println("fileToCopy="+fileToCopy);
				inputStream = sftpChannel.get(fileToCopy);
				System.out.println("inputStream=="+inputStream);
				outputStream = fs.create(new Path("/user/svchdphdo4ecfd/test_script_res/"+fileToCopy));
				System.out.println("Copy started********....");
				IOUtils.copyBytes(inputStream, outputStream, conf, true);
				System.out.println("File copy Completed");
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Total Time Taken---="+totalTime/1000 +"seconds");
				
						
		} catch (Exception e) {
			System.out.println("Error in SSH SFTP.....");
			e.printStackTrace();
		}
	      finally {
	    	    try {
					inputStream.close();
					outputStream.close();
					fs.close();
					sftpChannel.disconnect();
					session.disconnect();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			
		}
	      


	}

}

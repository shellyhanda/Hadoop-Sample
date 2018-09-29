package hdfsconnection;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSConnect {
	
	public static void main(String[] args) throws IOException {


		Configuration conf = new Configuration();
	    conf.set("fs.defaultFS", "hdfs://127.0.0.1:8020");
	    
	    FileSystem fs = FileSystem.get(conf);
	    fs.createNewFile(new Path("/user/root/test2.txt"));
	    FileStatus[] status = fs.listStatus(new Path("/user/root"));
	    fs.copyFromLocalFile(new Path("C:\\MyWork\\AWS_commands_HCL.txt"), new Path("/user/root/test_hdfs"));
	    for(int i=0;i<status.length;i++){
	        System.out.println(status[i].getPath());
	    }
	 }
}
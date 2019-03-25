package com.shelly.poc.mrclass.flightmrdemo;


import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class FlightsByCarrierReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
 @Override
protected void reduce(Text token, Iterable<IntWritable> counts,Context context) throws IOException, InterruptedException {
		int sum = 0;
		System.out.println("counts="+counts.iterator().toString());
		System.out.println("token="+token);
		for (IntWritable count : counts) {
			System.out.println("inside for sum="+sum);
		sum+= count.get();
		}
		
		context.write(token, new IntWritable(sum));
		}
}

/*import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class FlightsByCarrierReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	@Override
	protected void reduce(Text token, Iterable<IntWritable> counts,	Context context) throws IOException, InterruptedException {
			int sum = 0;
			System.out.println("counts="+counts.iterator().toString());
			System.out.println("token="+token);
			for (IntWritable count : counts) {
			sum+= count.get();
			System.out.println("inside for sum="+sum);
			}
			System.out.println("**sum**+"+sum);
			context.write(token, new IntWritable(sum));
			}

}
*/
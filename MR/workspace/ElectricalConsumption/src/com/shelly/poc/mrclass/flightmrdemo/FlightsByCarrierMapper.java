package com.shelly.poc.mrclass.flightmrdemo;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;

public class FlightsByCarrierMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		System.out.println("**key**"+key);
		String[] lines = new CSVParser().parseLine(value.toString());
		System.out.println("**lines0**="+lines[0]);
		System.out.println("**lines1**="+lines[1]);
		//context.write(new Text(lines[0]), new IntWritable(1)); // provides the sum of the flight name present in the .csv
		context.write(new Text(lines[0]), new IntWritable(Integer.parseInt(lines[1]))); // provides the sum of the flights column no 2 in .csv
		
		/*if (key.get() > 0) {
			String[] lines = new CSVParser().parseLine(value.toString());
			System.out.println("**lines**="+lines);
			context.write(new Text(lines[0]), new IntWritable(1));
		}	*/
	}
	

}

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;


public class StubDriver {
	
  public static void main(String[] args) throws Exception {

    /*
     * Validate that two arguments were passed from the command line.
     */
    if (args.length != 2) {
      System.out.printf("Usage: StubDriver <input dir> <output dir>\n");
      System.exit(-1);
    }
    
    long startTime = System.currentTimeMillis();

    Configuration conf = new Configuration();
    //conf.setBoolean("mapreduce.compress.map.output", true);
    //conf.setClass("mapreduce.map.output.compress.codec", 
    	//	SnappyCodec.class, CompressionCodec.class);

    Path out = new Path(args[1]);
    
    Job job1 = Job.getInstance(conf, "word count");
    job1.setJarByClass(StubDriver.class);
    job1.setInputFormatClass(TextInputFormat.class);
    
    
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path(out, "out1"));
    //FileOutputFormat.setCompressOutput(job1, true);
    //FileOutputFormat.setOutputCompressorClass(job1, SnappyCodec.class);
    
    
    job1.setMapperClass(StubMapper.class);
    //job1.setCombinerClass(StubReducer.class);
    job1.setReducerClass(StubReducer.class);
    job1.setNumReduceTasks(50);

    job1.setMapOutputKeyClass(Text.class);
    job1.setMapOutputValueClass(IntWritable.class);
    
    if (!job1.waitForCompletion(true)) {
    	  System.exit(1);
    	  }
    
    Job job2 = Job.getInstance(conf, "sort frequency");
    job2.setJarByClass(StubDriver.class);
    
    FileInputFormat.addInputPath(job2, new Path(out, "out1"));
    FileOutputFormat.setOutputPath(job2, new Path(out, "out2"));
    
    job2.setMapperClass(KeyValueMapper.class);
    job2.setReducerClass(FrequenceReducer.class);
    
    job2.setInputFormatClass(KeyValueTextInputFormat.class);
    job2.setSortComparatorClass(IntComparator.class);
    job2.setNumReduceTasks(50);
    
    job2.setMapOutputKeyClass(IntWritable.class);
    job2.setMapOutputValueClass(Text.class);
    
    if (!job2.waitForCompletion(true)) {
    	  System.exit(1);
    	}

    long endTime   = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("Performance = " + totalTime);
  }
}


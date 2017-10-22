import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;


public class StubDriver  {
	
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

    Path out = new Path(args[1]);
    
    Job job1 = Job.getInstance(conf, "InvertesIndex");
    job1.setJarByClass(StubDriver.class);
    job1.setInputFormatClass(TextInputFormat.class);
    
    
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path(out, "out1"));
    
    job1.setMapperClass(StubMapper.class);
    //job1.setCombinerClass(StubReducer.class);
    job1.setReducerClass(StubReducer.class);
    //job1.setNumReduceTasks(1);

    job1.setMapOutputKeyClass(Text.class);
    job1.setMapOutputValueClass(Text.class);
    
    job1.setOutputKeyClass(Text.class);
    job1.setOutputValueClass(Text.class);
    
    job1.setInputFormatClass(TextInputFormat.class);
    job1.setOutputFormatClass(TextOutputFormat.class);
    
    if (!job1.waitForCompletion(true)) {
    	  System.exit(1);
    	  }
    long c = job1.getCounters().findCounter(UpdateCount.CNT).getValue();
    long endTime   = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("Performance = " + totalTime);
    System.out.println("Unique = " + c);
  }
}


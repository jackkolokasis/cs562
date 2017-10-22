import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class KeyValueMapper  extends Mapper <Text, Text, IntWritable, Text> {
	private final static IntWritable frequency = new IntWritable();
	
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		int newVal = Integer.parseInt(value.toString());
	    frequency.set(newVal);
	    context.write(frequency, key);
	    }

}

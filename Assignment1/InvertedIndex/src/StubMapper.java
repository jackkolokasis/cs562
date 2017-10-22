import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;

public class StubMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private BufferedReader file;
	private Set<String> stopWordList = new HashSet<String>();

	/*
	 * Method to read the stop word file and get the stop words
	 */
	
	protected void setup(Context context) throws java.io.IOException,
			InterruptedException {
		
		try {
			file = new BufferedReader(new FileReader("/home/cloudera/workspace/InvertedIndex/stopWords.csv"));
			String stopWord = null;
			while ((stopWord = file.readLine()) != null) {
				stopWordList.add(stopWord);
			}
		} catch (IOException ioe) {
			System.err.println("Exception while reading stop word file '"
					+ file + "' : " + ioe.toString());
		}
	}
		
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
	  
	  String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
	  String line=value.toString();
	   
	  //Split the line in words
	   
	  StringTokenizer itr = new StringTokenizer(line);
	  String str = new String();
	  
	  while (itr.hasMoreTokens()) {
		  str = itr.nextToken();
		  str = str.replaceAll("[^A-Za-z]", "");
		  str = str.toLowerCase();
		  if (!stopWordList.contains(str)){
			  context.write(new Text(str), new Text(fileName));
		  }
	  }
  }
}
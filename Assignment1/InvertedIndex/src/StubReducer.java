import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StubReducer extends Reducer<Text, Text, Text, Text> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
	  
	  ArrayList<String> ar = new ArrayList<String>();
	  
	  for(Text t:values){
		  
		  String str=t.toString();
		  
		  if (!ar.contains(str)){
			  ar.add(str);
		  }
	  }
	  
	  StringBuilder sb = new StringBuilder();
	  int len = ar.size();
	  int i = 1;
	  
	  for (String s: ar) {
		  sb.append(s);
		  if (i != len)
			  sb.append(", ");
		  i++;
	  }
	  if (len == 1) {
		  context.getCounter(UpdateCount.CNT).increment(1);
	  }
	  context.write(key, new Text(sb.toString()));
  }
}
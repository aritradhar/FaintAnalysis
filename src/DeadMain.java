import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import soot.PackManager;
import soot.Transform;


public class DeadMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		List<String> sootArgs = new ArrayList<String>(Arrays.asList(args));
	    
	    sootArgs.add(0, "-keep-line-number");
	    // Produce no output
	    sootArgs.add("-output-format");
	    sootArgs.add("J");
	 
	    //sootArgs.add("-process-dir");
	    //sootArgs.add("input");
	    sootArgs.add("Test1");
	    sootArgs.add("-src-prec");
	    sootArgs.add("class");
	    //sootArgs.add("-cp");
	    //sootArgs.add("input");
	    sootArgs.add("-cp");
	    sootArgs.add(".;C:\\Program Files\\Java\\jdk1.7.0_06\\jre\\lib\\rt.jar");
	    //sootArgs.add("-exclude");
	    //sootArgs.add("java");
	    //sootArgs.add("sun");
	    //sootArgs.add("java.lang");
	    //sootArgs.add("-pp .");


	    //PackManager.v().getPack("jtp").add(
	       // new Transform("jtp.LiveVariablesAnalysis", LiveVariablesAnalysis));
	    soot.Main.main(sootArgs.toArray(args));
	    
	    // Print all the line numbers

	}

}

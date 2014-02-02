package lolspecanalyzer;

import java.util.LinkedList;
import java.util.List;
import lolspecanalyzer.analyzers.AbstractAnalyzer;
import lolspecanalyzer.analyzers.TimestampAnalyzer;
import lolspecanalyzer.analyzers.StringAnalyzer;

// Hello world! This is my debugging tool for analyzing (decrypted and decompressed) League of Legends spectator frames.
// It loads a frame from a file and runs a number of analyzers (mostly various kinds of heuristics at the moment) against it.
// It then prints out a human readable byte map of the file, annotated ('marked') by the analyzers. This makes reverse
// engineering the format much more fun. You can add your own analyzer by subclassing AbstractAnalyzer in the analyzers package.
//
// Next big step for this framework is a more semantic approach for consuming bytes instead of plain annotation. This would
// make it easier to parse data structures like lists and maps which seem to be present in the frames.
public class LolSpecAnalyzer
{
	private static final int BYTEMAP_LINE_SIZE = 256;	// You can adjust this according to your screen resolution so the byte map fits its width
	
	public static void main(String[] args)
	{
		// Load frame from disk 
		byte[] frameBytes = new Frame("data/token.plain").bytes;

		// Setup analyzers
		List<AbstractAnalyzer> analyzers = new LinkedList<>();
		analyzers.add(new StringAnalyzer());
		analyzers.add(new TimestampAnalyzer());
		
		// Run analyzers
		for (AbstractAnalyzer analyzer : analyzers)
		{
			analyzer.setBytes(frameBytes);
			analyzer.analyze();
		}
		
		// Print output with byte map
		StringBuilder output = new StringBuilder();
		output.append("Analyzed frame with ").append(frameBytes.length).append(" bytes. Map:");
		
		for (int i = 0; i < frameBytes.length; i++)
		{
			boolean marked = false;
			
			if (i % BYTEMAP_LINE_SIZE == 0)
			{
				output.append("\n").append(i).append(": ");
			}
			
			for (AbstractAnalyzer analyzer : analyzers)
			{
				if (analyzer.getMark(i) != null)
				{
					output.append(analyzer.getMark(i));
					marked = true;
				}
			}
			
			if (!marked)
			{
				output.append("_");
			}
		}
		
		System.out.println(output);
	}
}
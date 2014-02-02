package lolspecanalyzer.analyzers;

import com.google.common.base.Charsets;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import lolspecanalyzer.util.Tools;

// This heuristic finds two different kinds of strings: Null-terminated and length-prefixed.
public class StringAnalyzer extends AbstractAnalyzer
{
	private static final boolean PRINT_ALL_STRINGS = true;		// Besides marking strings in byte output, should we collect them all and print them separately, counting how many times we saw each one?
	private static final int STRING_LENGTH_BYTES = 4;			// Numbers of bytes specifying the length of length-prefixed strings
	private static final int MIN_STRING_LENGTH_PREFIXED = 2;	// Ignore strings shorter than this number of characters (to avoid too many false positives)
	private static final int MIN_STRING_LENGTH_TERMINATED = 4;	// ...
	
	private final SortedMultiset<String> stringCounts = TreeMultiset.create();
	
	@Override
	public void analyze()
	{
		byteLoop:
		for (int i = 0; i < bytes.length - (STRING_LENGTH_BYTES - 1); i++)
		{
			// Heuristic for finding strings terminated with a null character
			int offset = 0;

			while (isChar(bytes[i + offset]))
			{
				offset++;
			}

			if (offset >= MIN_STRING_LENGTH_TERMINATED && bytes[i + offset] == 0)
			{
				stringCounts.add(new String(bytes, i, offset, Charsets.US_ASCII));
				passBytes(i, offset);
				markByte(i + offset, '"');
				i += offset + 1 - 1;
				continue;
			}
			
			// Heuristic for finding strings with length prefix
			int stringLength = (int) Tools.decodeUnsignedInteger(bytes, i, 4);
			
			if (stringLength >= MIN_STRING_LENGTH_PREFIXED && (i + STRING_LENGTH_BYTES + (stringLength  - 1) < bytes.length))
			{
				for (int j = 0; j < stringLength; j++)
				{
					if (!isChar(bytes[i + STRING_LENGTH_BYTES + j]))
					{
						continue byteLoop;
					}
				}
				
				stringCounts.add(new String(bytes, i + STRING_LENGTH_BYTES, stringLength, Charsets.US_ASCII));
				markBytes(i, STRING_LENGTH_BYTES, '"');
				passBytes(i + STRING_LENGTH_BYTES, stringLength);

				i += STRING_LENGTH_BYTES + stringLength - 1;
			}
		}
		
		if (PRINT_ALL_STRINGS)
		{
			System.out.println("Strings detected:");
			
			for (String string : stringCounts.elementSet())
			{
				System.out.println(string + " (x" + stringCounts.count(string) + ")");
			}
			
			System.out.println("");
		}
	}
	
	private boolean isChar(byte charByte)
	{
		// 32 - 126: ASCII letter/sign range
		// 96: '`'
		// 64: '@'
		return charByte >= 32 && charByte <= 126 && charByte != 96 && charByte != 64;
	}
}
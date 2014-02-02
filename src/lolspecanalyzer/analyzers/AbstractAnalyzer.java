package lolspecanalyzer.analyzers;

import java.util.Arrays;

// Base class for analyzers. Holds the raw bytes as well as a mark for each byte which the analyzer can
// set to mark something in the byte output. So if the timestamp analyzer finds a timestamp of 4 bytes, 
// it can mark them 'T', 'I', 'M', 'E'. Which it does... :).
public abstract class AbstractAnalyzer
{
	protected byte[] bytes;
	private Character[] marks;

	// This is what the analyzer should implement
	public abstract void analyze();

	public void setBytes(byte[] bytes)
	{
		this.bytes = Arrays.copyOf(bytes, bytes.length);
		this.marks = new Character[bytes.length];
	}
	
	// Mark a byte with a specific character in the byte output
	protected void markByte(int index, Character mark)
	{
		marks[index] = mark;
	}
	
	protected void markBytes(int startIndex, int num, Character mark)
	{
		for (int i = 0; i < num; i++)
		{
			marks[startIndex + i] = mark;
		}
	}
	
	// Let a number of bytes "pass through" to the byte output. So instead of displaying nothing,
	// we can display the raw char representation of the bytes. Useful for showing actual ASCII strings
	// instead of just marking them as strings.
	protected void passBytes(int startIndex, int num)
	{
		for (int i = 0; i < num; i++)
		{
			marks[startIndex + i] = (char) (int) bytes[startIndex + i];
		}
	}
	
	public Character getMark(int index)
	{
		return marks[index];
	}
}

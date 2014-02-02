package lolspecanalyzer.util;

public class Tools
{
	public static long decodeUnsignedInteger(byte[] bytes, int startIndex, int numBytes)
	{
		if (numBytes < 1)
		{
			throw new IllegalArgumentException("Integer to be decoded must be at least 1 byte large");
		}
		else if (numBytes > 7)
		{
			// Java long is signed, so we can only read up to 7 unsigned bytes without overflowing
			throw new UnsupportedOperationException("Decoding integers > 7 bytes not supported by this implementation");
		}
	
		long num = 0;

		// Java has quite poor support for reading primitive data types of varible length from binary data, so we do this instead.
		for (int i = 0; i < numBytes; i++)
		{
			num = (num << 8) + (bytes[startIndex + ((numBytes - 1) - i)] & 0xFF);	// Java is big endian, but the integers are in little endian. Reverse.
		}
		
		return num;
	}
}
package lolspecanalyzer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class Frame
{
	public final byte[] bytes;

	public Frame(String filePath)
	{
		// Allocate room for content
		File file = new File(filePath);

		// Java arrays are int indexed! Fail if size is too big
		if (file.length() != (int) file.length())
		{
			throw new UnsupportedOperationException("File size of " + filePath + " too large for this implementation");
		}

		// Read contents
		bytes = new byte[(int) file.length()];

		try (RandomAccessFile fileInput = new RandomAccessFile(file, "r"))
		{
			fileInput.readFully(bytes);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
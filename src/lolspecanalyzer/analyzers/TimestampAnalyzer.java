package lolspecanalyzer.analyzers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lolspecanalyzer.util.Tools;

// This is a heuristic for detecting timestamps in the data by finding the longest increasing subsequence of unsigned 4-byte integers
public class TimestampAnalyzer extends AbstractAnalyzer
{
	private static final int TIMESTAMP_BYTES = 4;
	
	// This heuristic creates a lot of false positives, but it is quite easy to spot patterns among the hits. 
	// For example: Running this analyzer against the provided token.plain gives suspiciously good results at 5290597 to 5350912 which is ~60 seconds!
	// Once the range has been determined by manual inspection, you can cut off the hits before and after.
	private static final boolean PRINT_HITS = false;
	private static final Integer RANGE_START = 5290597;		// 'null' to not filter
	private static final Integer RANGE_END = 5350912;		// ...
	
	@Override
	public void analyze()
	{
		// Wrap bytes in LisNodes. Note that we decode integers from each possible starting position, even though the same bytes then might be used more than once (with TIMESTAMP_BYTES > 1). It's just a heuristic...
		List<LisNode<Long>> lisNodes = new LinkedList<>();
		
		for (int i = 0; i < bytes.length - (TIMESTAMP_BYTES - 1); i++)
		{
			lisNodes.add(new LisNode<>(i, Tools.decodeUnsignedInteger(bytes, i, TIMESTAMP_BYTES)));
		}
		
		// Find LIS
		List<LisNode<Long>> lis = lis(lisNodes);
		
		// Walk through result
		for (LisNode<Long> node : lis)
		{
			if (PRINT_HITS)
			{
				System.out.println("DEBUG: Timestamp hit at index " + node.index + " with time " + node.value);
			}
			
			if ((RANGE_START == null || node.value >= RANGE_START) && (RANGE_END == null || node.value <= RANGE_END))
			{
				markByte(node.index + 0, 'T');
				markByte(node.index + 1, 'I');
				markByte(node.index + 2, 'M');
				markByte(node.index + 3, 'E');
			}
		}
	}

	// Adapted from http://rosettacode.org/wiki/Longest_increasing_subsequence#Java
	private static <E extends Comparable<? super E>> List<LisNode<E>> lis(List<LisNode<E>> n)
	{
		List<LisNode<E>> pileTops = new ArrayList<>();
		
		// Sort into piles
		for (LisNode<E> node : n)
		{
			int i = Collections.binarySearch(pileTops, node);
			
			if (i < 0)
			{
				i = ~i;
			}
			if (i != 0)
			{
				node.pointer = pileTops.get(i - 1);
			}
			if (i != pileTops.size())
			{
				pileTops.set(i, node);
			}
			else
			{
				pileTops.add(node);
			}
		}
		
		// Extract LIS from nodes
		List<LisNode<E>> result = new ArrayList<>();
		
		for (LisNode<E> node = pileTops.isEmpty() ? null : pileTops.get(pileTops.size() - 1); node != null; node = node.pointer)
		{
			result.add(node);
		}
		
		Collections.reverse(result);
		
		return result;
	}

	// This is just a container class allowing backtracking from values to byte indices
	private static class LisNode<E extends Comparable<? super E>> implements Comparable<LisNode<E>>
	{
		public int index;
		public E value;
		public LisNode<E> pointer;

		public LisNode(int index, E value)
		{
			this.index = index;
			this.value = value;
		}
		
		@Override
		public int compareTo(LisNode<E> y)
		{
			return value.compareTo(y.value);
		}

		@Override
		public boolean equals(Object obj)
		{
			return (obj instanceof LisNode) && Integer.compare(index, ((LisNode<E>) obj).index) == 0;
		}

		@Override
		public int hashCode()
		{
			return index;
		}
	}
}
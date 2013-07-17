package algo.library;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * 
 * @author Anunay
 * Java TreeSet backed suffix tree
 * Supports, prepend and delete first operations!
 * Query function yet to be added
 */
public class SuffixTree 
{
	public static void main(String[] args) 
	{
		SuffixTree tree	=	SuffixTree.buildTree("banana", 100000);
		System.out.println(Arrays.toString(tree.lcpArray()));
	}
	
	/**
	 * Creates an instance of suffix tree, 
	 * with string s and maximum size maxSize!
	 * 
	 * @param s
	 * @param maxSize
	 * @return
	 * 
	 */
	public static SuffixTree buildTree(String s,int maxSize)
	{
		SuffixTree tree	=	new SuffixTree(maxSize);
		tree.s = s;
		for(int i = s.length()-1 ; i >= 0 ; i--)
		{
			tree.prepend(s.charAt(i));
		}
		return tree;
	}
	
	/**
	 * @return
	 * Number of total substrings of string!
	 * The running time is O(length of the string)
	 */
	public int countSubstr() 
	{
		int[] SA	=	suffixArray();
		int[] lcp	=	lcpArray();
		int ans = s.length()-SA[0];
		for(int i = 1 ; i < lcp.length ; i++)
		{
			ans += s.length()-SA[i] - lcp[i];
		}
		
		return ans;
	}
	
	/**
	 * 
	 * @return
	 * Builds suffix array from suffix tree 
	 */
	public int[] suffixArray() 
	{
		sa = new int[getLength()];
		int j = 0;
		for(Node node : tree)
		{
			sa[j++] = node.index-begin;
		}
		return sa;
	}
	
	/**
	 * 
	 * @return
	 * Returns the inverse of suffix array
	 */
	public int[] suffixArrayInverse()
	{
		sainv = new int[getLength()];
		suffixArray();
		for(int i = 0; i < sainv.length ; i++)
		{
			sainv[sa[i]] = i;
		}
		return sainv;
	}
	
	/**
	 * 
	 * @return
	 * Longest common prefix array
	 */
	public int[] lcpArray()
	{
		suffixArrayInverse();
		lcp = new int[getLength()];
		int l = 0;
		int n = getLength();
		for(int j = 0 ; j < n ; j++)
		{
			l = Math.max(0, l-1);
			int i = sainv[j];
			if(i != 0)
			{
				int jp = sa[i-1];
				while(j+l < n && jp + l < n && s.charAt(j+l) == s.charAt(jp+l))
					l++;
			}
			else
				l = 0;
			lcp[i] = l;
		}
		
		return lcp;
	}
	
	public void print() 
	{
		for(Node node : tree)
		{
			System.out.println(node.index-begin);
		}
	}
	
	int size;
	int[] sa,sainv,lcp;
	Node[] string;
	int begin,end;
	TreeSet<Node> tree = new TreeSet<Node>();
	int state = -1;
	String s;
	double currentMaxRank,currentMinRank;
	static int ADDING_STATE = 1, SEARCHING_STATE = 2,DELETING_STATE = 3;
	
	public SuffixTree(int size) 
	{
		this.size = 2*size;
		string = new Node[2*size];
		begin = 2*size;
		end = 2*size-1;
		currentMaxRank = (currentMinRank = 10.0);
	}
	
	/**
	 * Deletes the first character of the string
	 */
	public void deleteFirst()
	{
		setState(DELETING_STATE);
		tree.remove(getNode(begin));
		
		if(getNode(begin).value == currentMinRank)
		{
			currentMinRank = tree.first().value;
		}
		else if(getNode(begin).value == currentMaxRank)
		{
			currentMaxRank = tree.last().value;
		}
		
		clearState();
		begin++;
	}
	
	/**
	 * Prints the string stored yet
	 */
	public void printString()
	{
		String s = "";
		for(int i = begin ; i <= end ; i++)
		{
			s = s + getNode(i).c;
		}
		System.out.println(s);
	}
	
	/**
	 * 
	 * @param c
	 * Prepends the character to the string
	 */
	public void prepend(char c) 
	{
		begin--;
		Node node	=	new Node(c,begin);
		string[((begin)%size+size)%size] = node;
		setState(ADDING_STATE);
		tree.add(node);
		
		if(getLength() == 1)
		{
			node.value = currentMaxRank;
		}
		else
		{
			Node prev = tree.lower(node);
			Node next = tree.higher(node);
			if(prev == null)
			{
				node.value = --currentMinRank;
			}
			else if(next == null)
			{
				node.value = ++currentMaxRank;
			}
			else
			{
				node.value = (prev.value + next.value)/2.0;
				if(node.value == prev.value || node.value == next.value)
				{
					int N = getLength();
					Node[] array = new Node[N];
					int j = 0;
					for(Node n : tree)
					{
						array[j++] = n;
					}
					
					array[0].value = -1000000000000000000.0;
					array[N-1].value = 1000000000000000000.0;
					setValue(array,0,N-1);
					currentMinRank = array[0].value;
					currentMaxRank = array[N-1].value;
				}
				
			}
		}
		
		clearState();
	}
	
	private void setValue(Node[] array, int i, int j) 
	{
		if(Math.abs(i-j) > 1)
		{
			array[(i+j)/2].value = (array[i].value+array[j].value)/2.0;
			setValue(array, i, (i+j)/2);
			setValue(array,(i+j)/2,j);
		}
	}
	
	private int getState() 
	{
		return state;
	}
	
	private void setState(int state) 
	{
		this.state = state;
	}
	
	private void clearState() 
	{
		this.state = -1;
	}
	
	public class Node implements Comparable<Node>
	{
		char c;
		int index;
		double value;
		
		public Node(char c) 
		{
			this.c = c;
		}
		
		public Node(char c,int start) 
		{
			this.c = c;
			this.index = start;
		}
		
		public Node(char c,int start,double value) 
		{
			this.c = c;
			this.index = start;
			this.value = value;
		}
		
		@Override
		public int compareTo(Node o) 
		{
			if(this.index == o.index)
				return 0;
			
			int state = SuffixTree.this.getState();
			if(state == ADDING_STATE)
			{
				Node currNode = this.index == begin ? this : o;
				Node otherNode = this.index == begin ? o : this;
				
				if(currNode.c != otherNode.c)
				{
					return c-o.c;
				}
				
				Node beginNode = getNode(currNode.index+1);
				Node nextNode = getNode(otherNode.index+1);
				if(nextNode == null)
				{
					return 1;
				}
				else
				{
					return beginNode.value > nextNode.value ? 1 : -1;
				}
			}
			else if(state == DELETING_STATE)
			{
				return this.value > o.value ? 1 : -1;
			}
			
			return 0;
		}
		
		@Override
		public String toString() 
		{
			String s = "";
			for(int i = index ; i <= end ;i++)
			{
				s = s + string[((i)%size+size)%size].c;
			}
			
			return s;
		}
		
	}
	
	private Node getNode(int index) 
	{
		return string[(index%size+size)%size];
	}
	
	/**
	 * 
	 * @return
	 * Length of the string built yet
	 */
	private int getLength() 
	{
		return end-begin+1;
	}
	
	@Override
	public String toString() 
	{
		return tree.toString();
	}
}

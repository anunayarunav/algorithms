package algo.library;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Anunay
 * Finds all the cut vertices of a graph,
 * takes O(E+V) time,performs dfs of the 
 * graph. Pass the graph as LinkedList<Integer>[] 
 * that is an adjacency list, ie. for each node 
 * from 0...N store the adjacent nodes in linkedlist of corresponding array entry
 *   
 * Use:
 * new CutVertices(LinkedList<Integer>[] graph).findCutNodes()
 */
public class CutVertices 
{
	int time = 0;
	int[] cutnode;
	int[] color;
	int[] earliestAncestor;
	int[] entry;
	int[] outdegree;
	Integer[] parent;
	private LinkedList<Integer>[] graph;
	
	public static void main(String[] args)
	{
		
	}
	
	public CutVertices(LinkedList<Integer>[] graph)  
	{
		int V	=	graph.length;
		this.graph = graph;
		cutnode	=	new int[V];
		color	=	new int[V];
		earliestAncestor	=	new int[V];
		entry = new int[V];
		outdegree = new int[V];
		Arrays.fill(entry, Integer.MAX_VALUE);
		
		parent	=	new Integer[V];
		
		Arrays.fill(color, 0);
	}
	
	private void dfs(int v) 
	{
		color[v] = 1;
		earliestAncestor[v] = v;
		entry[v] = ++time;
		
		for(int w : graph[v])
		{
			if(color[w] == 0)	//not visited
			{
				parent[w] = v;
				outdegree[v]++;
				dfs(w);
			}
			else
			{
				if(entry[w] < entry[earliestAncestor[v]])
				{
					earliestAncestor[v] = w;
				}
			}
		}
		
		processVertex(v);
		color[v] = 2;
	}
	
	private void processVertex(int v) 
	{
		if(v == 0) //root
		{
			if(outdegree[v] > 1)
			{
				cutnode[v] = 1;
			}
			return;
		}
		
		boolean root = (parent[v] == 0);
		if((earliestAncestor[v] == parent[v]) && !root)
		{
			cutnode[parent[v]] = 1;
		}
		
		if(earliestAncestor[v] == v)
		{
			cutnode[parent[v]] = 1;
			if(outdegree[v] > 0)
			{
				cutnode[v] = 1;
			}
		}
		
		int time_v = entry[earliestAncestor[v]];
		int time_parent = entry[earliestAncestor[parent[v]]];
		
		if(time_v < time_parent )
		{
			earliestAncestor[parent[v]] = earliestAncestor[v];
		}
	}
	
	public int[] findCutNodes() 
	{
		dfs(0);
		return cutnode;
	}
}

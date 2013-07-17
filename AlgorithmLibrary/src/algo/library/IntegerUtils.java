package algo.library;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * 
 * @author Anunay
 * Various Utility class, 
 * like finding modulo, modular inverse prime,
 * base conversion, ncr mod prime calculation etc.
 * See main for usage
 */
public class IntegerUtils 
{
	long[] factorial;
	int p;
	
	public static void main(String[] args) 
	{
		System.out.println(modulo(100007, 10, 1000000007));
		System.out.println(fastModuloPrime(100007, 10000000007l, 1000000007));
		System.out.println(inv(2,1000000007));
		IntegerUtils integerUtils	=	new IntegerUtils(100000, 1000000007);
		System.out.println(integerUtils.ncr(1000, 10));
	}
	
	/**
	 * @param factorialSize
	 * Maximum number upto which 
	 * the factorial might be needed,
	 * this is used for ncr
	 * 
	 * @param p
	 * prime for which the modulo 
	 * is to be taken
	 */
	public IntegerUtils(int factorialSize,int p) 
	{
		factorial = new long[factorialSize];
		this.p = p;
		buildFactorial();
	}
	
	private void buildFactorial() 
	{
		factorial[0] = 1;
		factorial[1] = 1;
		for(int i = 2 ; i < factorial.length ; i++)
		{
			factorial[i] = ((factorial[i-1]%p)*(((long)i)%p))%p;
		}
	}
	
	public static BigInteger max(BigInteger...x)
	{
		BigInteger ret = BigInteger.valueOf(-1);
		for(BigInteger y : x)
		{
			ret = y.max(ret);
		}
		return ret;
	}
	
	public static Integer max(int...x)
	{
		int ret = Integer.MIN_VALUE;
		for(int y : x)
		{
			ret = Math.max(ret,y);
		}
		return ret;
	}
	
	/**
	 * @param n
	 * @param r
	 * @return
	 * C(n,r) mod p
	 */
	public int ncr(int n,int r)
	{
		long fact1 = factorial[n];
		long fact2 = inv((int)factorial[r], p);
		long fact3 = inv((int)factorial[n-r], p);
		long l = ((fact1*fact2)%p*fact3)%p;
		return (int)l;
	}
	
	/**
	 * @param a
	 * @param p
	 * @return
	 * Modular inverse wrt prime i.e.
	 * a number b such that 
	 * a*b = 1 mod p
	 */
	public static int inv(int a,int p)
	{
		long l = modulo(a, p-2, p);
		return (int)l;
	}
	
	/**
	 * finds a^b mod c in log c time
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static long modulo(long a,long b,int c)
	{
	    long x=1,y=a; 
	    while(b > 0)
	    {
	        if(b%2 == 1)
	        {
	            x=(x*y)%c;
	        }
	        y = (y*y)%c; 
	        b /= 2;
	    }
	    return x%c;
	}
	
	/**
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 * Fast modulo 
	 * some prime number
	 */
	public static long fastModuloPrime(long a,long b,int p)
	{
		if(a%p == 0)
			return 0;
		return modulo(a, b%(p-1), p);
	}
	
	/**
	 * Sieve of erastosthenes faster
	 * @param limit
	 * @return
	 * boolean array, each element of array is true, 
	 * if index is prime else fast. The array size is limit+1
	 * giving primes upt limit. Taks time O(n * log(logn)) where
	 * n = limit
	 */
	public static boolean[] sieveOfEratosthenesFast(int limit) 
	{
		boolean[] A	=	new boolean[limit+1];
		Arrays.fill(A, true);
		for(int i = 2 ; i <= Math.sqrt(limit) ; i++)
		{
			if(A[i])
			{
				for(int j = i*i ; j <= limit ; j=j+i)
				{
					A[j] = false;
				}
			}
		}
		
		return A;
	}
	
	public static ArrayList<Integer> sieveOfEratosthenes(int limit) 
	{
		TreeSet<Integer> allNumbers	=	new TreeSet<Integer>();
		ArrayList<Integer> primeNumbers	=	new ArrayList<Integer>();
		
		for(int i = 2 ; i <= limit ; i++)
		{
			allNumbers.add(i);
		}
		
		while(!allNumbers.isEmpty())
		{
			int p = allNumbers.pollFirst();
			int s = 2*p;
			int i = 3;
			primeNumbers.add(p);
			while(s <= limit)
			{
				allNumbers.remove(s);
				s = i*p;
				i++;
			}
		}
		//System.out.println(primeNumbers.size());
		return primeNumbers;
	}
	
	/**
	 * Given the primes till sqrt(last), finds all the 
	 * primes between first and last
	 * 
	 * @param first
	 * @param last
	 * @param primes
	 * @return
	 */
	public static TreeSet<Integer> sieveRange(int first, int last, TreeSet<Integer> primes) 
	{
		TreeSet<Integer> allNumbers	=	new TreeSet<Integer>();
		
		for(int i = first ; i <= last ; i++)
		{
			allNumbers.add(i);
		}
		
		for(int p : primes)
		{
			if(allNumbers.isEmpty())
			{
				break;
			}
			
			int k = (allNumbers.first()/p)*p;
			if(k <= p)
			{
				k = 2*p;
			}
			
			while(k <= last)
			{
				allNumbers.remove(k);
				k+= p;
			}
		}
		
		allNumbers.remove(1);
		
		return allNumbers;
	}
}

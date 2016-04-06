package PA3;
import java.io.*;
import java.util.*;
import PA3.Location;
import PA3.Lookup;
import PA3.ClientHandler;
public class PA3
{
	static String rep;
	static int args_len;
	static int serverid;
	public static void main(String[] args) throws Exception
	{
		boolean final_result=false	;
		String val;
		String file;
		args_len=args.length;
		//String rep=null;
		Peer pn=new Peer();
		Lookup lp=new Lookup();
		Location l=new Location();
		serverid=Integer.parseInt(args[0])-1;
		if(args_len>1)
		{		
			rep=args[1];
		}
		pn.createpeer(serverid);
		while(true)
		{
			System.out.println("\nFile Operations");
			System.out.println("1. register");
			System.out.println("2. search");
			System.out.println("3. obtain");
			System.out.println("0. exit");
			System.out.println("\nSelect any operation");
			Scanner s=new Scanner(System.in);
			int in=s.nextInt();
			Scanner k=new Scanner(System.in);
			
			switch(in)
			{
			case 0: 	System.exit(0);
			case 1: 	final_result=pn.register();
					System.out.println(final_result);
					break;

			case 2: 	//System.out.println(final_result);
					if(final_result==true)
					{
						String search;
						System.out.println("Please provide the file name you wish to lookup");
						Scanner s1=new Scanner(System.in);
						search=s1.nextLine();
						lp=pn.retrieve(search);
						l=pn.selectpeer(lp.getlookfupfile(),lp.getpeerlist());
					}
					else{ System.out.println("register files first");
					}
					break;
			case 3: 	file=pn.obtain(l);
					System.out.println(file+"is received");
					break;
			default: 
					System.out.println("Enter 1,2 or 3");
			}
		}
	}
	
}

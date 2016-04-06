package PA3;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;

import PA3.Lookup;
class Evaluation {
	 public long starttime,endtime ; 
	// static long fortime;
	 ArrayList<String> fnames=new ArrayList<String>();
	 ArrayList<Lookup> peerslist=new ArrayList<Lookup>();
	ArrayList<Location> loca=new ArrayList<Location>();
	 String dirpath;
	 static Peer peers=new Peer();
	 boolean register=false;
	 Location location=new Location();
	 Lookup ll=new Lookup();	
	 static String peernum;
	static int len=0;
	public static void main(String[] args) throws Exception
	{	len=args.length;
		Evaluation e=new Evaluation();
		peernum=args[0];			
		peers.createpeer(Integer.parseInt(peernum)-1);
			e.eval(args[1],args[2]); 
			System.exit(0);			       
			
	}	
	
	private void eval(String filesize,String filenum) throws Exception
	{
		Peer a=new Peer();
		Evaluation ee=new Evaluation();	
		int exival=2;
		String[] env = {"PATH=/bin:/usr/bin/"};
		String cmd ="./testfiles.sh"+" "+filenum+" "+filesize+" "+peernum; //test$nodeid_filesize_$filenum
		Process process =Runtime.getRuntime().exec(cmd, env);
		try {
			exival=process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(exival==0)
		{	
		while(true)
		{				
			System.out.println("\nChoose options in sequence:");
			
			System.out.println("1. get register time");
			System.out.println("2. get search time");
			System.out.println("3. get download time");
			System.out.println("0. exit");
			Scanner in=new Scanner(System.in);
			int i=in.nextInt();
			switch(i)
			{
			case 1: ee.getregtime(filesize,filenum);
				break;	
			case 2: ee.getsearchtime(filesize,filenum);
				break;
			case 3: ee.getdownloadtime(filesize);
			       		
				System.out.println("delete files? Press any key..");
				Scanner input=new Scanner(System.in);
				String read=input.nextLine();
				String currentDir=System.getProperty("user.dir");
				String dirpath=a.peerdirpath;
				File file=new File(dirpath);	
				File[] files = file.listFiles();
				for (File f: files)
				{
					f.delete();
				}
				System.out.println("deleted.");
				
				break;
		
			case 0: System.exit(0);
				break;
			default: System.out.println("Enter 1,2 or 3");
					
			}
		}		
		}	
	}


	private void getregtime(String filesize,String filenum) throws UnknownHostException, IOException, ClassNotFoundException
	{
		starttime=System.currentTimeMillis();
		register=peers.register();
		endtime=System.currentTimeMillis();
		System.out.println("\nTime elapsed to register"+" "+filesize+" files:"+ (endtime-starttime) + " millisecs");
		
				
	}
			

	private void getsearchtime(String filesize,String filenum) throws Exception {
		Peer pe1=new Peer();
		String name;
		int peerid=Integer.parseInt(peernum);
		if(pe1.count!=1)
			peerid=Integer.parseInt(peernum)+1;
		if(register==true)
		{		starttime=System.currentTimeMillis();
				for (int i=1;i<=Integer.parseInt(filenum);i++)
				{
					if((peerid==Integer.parseInt(peernum)) && (pe1.count!=1))
					{
						peerid++;
					}
										
					if(peerid>pe1.count)
					{
					 	peerid=1;
						if(Integer.parseInt(peernum)==1 && pe1.count!=1)
						{ peerid++;
						}					
					}
						name="test"+peerid+"_"+filesize+"_"+i+".BIN";
						//System.out.println(name);
					peerid++;
					peerslist.add((peers.retrieve(name)));									
				}
				endtime=System.currentTimeMillis();
				System.out.println("Time elapsed to search"+" "+filesize+" files:"+ (endtime-starttime)+" millisecs");
			
		}
	
	}
private void getdownloadtime(String filesize) throws IOException {
	String fname;
	String peerlist;
	
	for(int i=0;i<peerslist.size();i++)
	{ 		fname=(String)peerslist.get(i).getlookfupfile();
			peerlist=(String)peerslist.get(i).getpeerlist();
			loca.add(i,peers.selectpeer(fname,peerlist));
	}
	starttime=System.currentTimeMillis();
	for(int i=0;i<loca.size();i++)
	{
		peers.obtain(loca.get(i));
		
	}
	endtime=System.currentTimeMillis();
	System.out.println("Time elapsed to download"+" "+filesize+"  files:"+ (endtime-starttime)+" millisecs");

	
}
}

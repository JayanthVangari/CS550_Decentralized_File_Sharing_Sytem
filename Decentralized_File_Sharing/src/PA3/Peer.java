package PA3;
import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;
import PA3.Location;
import PA3.Lookup;
import PA3.ClientHandler;
public class Peer implements Runnable
{
	public ServerSocket peerserver; 
	public Socket client;
	public int clientid;
	public  static int servport;
	public  static String peerdirpath;
	public String peer;
	static boolean registered=false;
	public static ArrayList<String> name=new ArrayList<String>();
	public String peerdir;
	static int count;
	public 	static int[] array_ports;
	static String[][] peerinfo;
	public static Socket csocket[];
	public static File f1;

	void createpeer(int id) throws IOException {
		String k;
		BufferedReader br,br1;
		String currentDir=System.getProperty("user.dir");
		
		File f=new File(currentDir+"/config");
		try{
			br1=new BufferedReader(new FileReader(f));
			br=new BufferedReader(new FileReader(f));
			
		int j=0;
		clientid=id;
		while((k=br1.readLine())!=null)
		{	
			count++;
		}		
		System.out.println("Total Servers : "+count);
		peerinfo=new String[count][4];
		csocket=new Socket[count];
		array_ports=new int[count];
		while((k=br.readLine())!=null)
		{	
			StringTokenizer st=new StringTokenizer(k," ");
			int i=0;	
			while(st.hasMoreTokens())
			{		
				peerinfo[j][i]=st.nextToken().toString();
				i++;
			}	
			j++;
		}
		servport=Integer.parseInt(peerinfo[id][1]); 
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		new Thread(new Peer()).start();
		System.out.println("start all the peers and press any key");
		Scanner input=new Scanner(System.in);
		String read=input.nextLine();
		for(int i=0;i<count;i++)
		{
			try {
				csocket[i]=new Socket(peerinfo[i][0],Integer.parseInt(Peer.peerinfo[i][1]));
				array_ports[i]=Integer.parseInt(Peer.peerinfo[i][1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String currentDir1 = System.getProperty("user.dir");
		Scanner a=new Scanner(System.in);
		peerdir="peer"+(id+1);
		peerdirpath=currentDir+"/"+ peerdir;
		f1=new File(peerdirpath);	
		if(f1.exists())
		{
			System.out.println("exists");
			String k1=f1.getAbsolutePath();
			System.out.println("directory exists at:"+k1);
			
		}
		else{
				if(f1.mkdirs());
				String k1=f1.getAbsolutePath();
				System.out.println("Directory created at:" +k1);
			}
		}
		
		public boolean register() 
		{
		ObjectOutputStream ops;
		ObjectInputStream ips;
		File f=null;
		int node;
		int i=0;
		int replica_node=0;
		boolean result=false;
		String message,send;
		String[] parts;
		f= new File(peerdirpath);
		File[] filename =f.listFiles();
			for(File file: filename)
		{
			if(file.isFile())
			{
				name.add(i,file.getName());
				i++;	
			}
		}
		
		for(i=0;i<name.size();i++)
		{
			
						
			node=(int)hash(name.get(i))%count;	
			if(node<0)
				node=-(node);
			try{
								
				ops=new ObjectOutputStream(csocket[node].getOutputStream());
				send=clientid+" "+1+" "+name.get(i)+" "+servport+";"+peerdirpath+";";
				ops.writeObject(send);
				ips=new ObjectInputStream(csocket[node].getInputStream());
				message=(String) ips.readObject();
				if(message.contains("success"))
				{
					parts=message.split(" ");
					replica_node=Integer.parseInt(parts[1]);
					result=true;
				}
				if(result==true && PA3.args_len>1 && PA3.rep.contentEquals("r"))
				{
						replicate(replica_node,i);
				
				}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		return result;
	}

	public void replicate(int id,int i)
	{
		File f;
		int len=0;
		int n;
		String message;
		boolean result=false;
		byte[] recvfile=new byte[64*1024];
		BufferedOutputStream bos=null;
		byte[] filetosend=new byte[64*1024];
		ObjectOutputStream ops=null;
		ObjectInputStream ips=null;
		DataInputStream dis=null;
		int l=0;
		try{
			
			String file=peerdirpath+"/"+name.get(i);
			f=new File(file);
			message="r"+" "+name.get(i)+" "+f.length();
			ops=new ObjectOutputStream(csocket[id].getOutputStream());
			bos=new BufferedOutputStream(csocket[id].getOutputStream());	
			ops.writeObject(message);
			FileInputStream infile=new FileInputStream(f);
				while(infile.available()>0)
				{							
				len=infile.read(filetosend);
				bos.write(filetosend,0,len);
				}
					
				bos.flush();
				infile.close();
				ips=new ObjectInputStream(csocket[id].getInputStream());
			
				String reply=(String)ips.readObject();
				
			}
		catch(Exception e)
		{}
			
	}


	String tempinfo;
	public Lookup retrieve(String key) {
		Lookup l=null;	
		int node;
		String message,send;
		node=(int)hash(key)%count;
		if(node<0)
		node=-(node);
		send="c "+2+" "+key;	
		try
		{
		ObjectOutputStream ops=new ObjectOutputStream(csocket[node].getOutputStream());
		ops.writeObject(send);
		ObjectInputStream ips=new ObjectInputStream(csocket[node].getInputStream());
		tempinfo=(String) ips.readObject();
		}
			
		catch(Exception e)
		{
				
		}		
		if(tempinfo.equals("FNF"))	
		{
		System.out.println("File not Found");
		System.out.println("Please provide the file name you wish to lookup");
		Scanner be=new Scanner(System.in);
		key=be.nextLine();
		retrieve(key);
		}
		l=new Lookup(key,tempinfo);
		return l;
	
	}
	
	public Location selectpeer(String file,String list )
	{
		String[] parts; 
		String path,ipaddress;
		int serverport;
		int in;
		ArrayList<String> substr=new ArrayList<String>();
		int j=0;
			int k=0;
						
			list=list.replaceAll("[^a-zA-Z\\d.\\,\\/\\;\\-\\_]","");
			
		
			for(int i1=0;i1<list.length();i1++)
			{
				if(list.charAt(i1)==',')
				{
				 	  substr.add(j,list.substring(k,i1));
					  j++;
					  k=i1+1;
				 
				} 
				if(i1==(list.length()-1))
				  {  substr.add(j,list.substring(k,i1+1));
				  }
				   
				}
		
		Evaluation e=new Evaluation();		
		if(e.len==0)
		{
			System.out.println("\n PEERS LIST");
			for(int i=0;i<substr.size();i++)
			{
				System.out.println(i+":"+substr.get(i));
			}
			System.out.println("Select one of the peers");
			Scanner select=new Scanner(System.in);
			in=select.nextInt();
			
		}
		else
		{	in=0; 
			
		}
		if(in<0 && in>=(substr.size()-1))
		{
			selectpeer(file,list);
		}
		
		peer=substr.get(in);
		parts=peer.split(";");
		ipaddress=parts[3];
		serverport=Integer.parseInt(parts[1]);
		path=parts[2];
		Location loc=new Location(file,ipaddress,serverport,path);
		return loc;
	}


	private long hash(String key) {
	
	 long hash=65599;
	for (int j=0;j<key.length();j++)
	{	
	hash=(key.charAt(j)+hash)*33+j;
	while(hash>Long.MAX_VALUE)
	{	
		hash=hash/10;
	}
	}
	return hash;
	}
	public String obtain(Location locate)
	{
		Lookup ll=new Lookup();	
		Location loc=new Location();	
		String[] parts;
		String send,length=null;
		int id=0,c1=0;
		long len=0;
		String saddress=locate.getsaddress();
		int sport=locate.getportadd();
		String filesearched=locate.getfname();
		String path=locate.getpath();
		String name,ext;
		File file=null;
		FileOutputStream fops=null;;		
		ObjectOutputStream reqfile=null;		
		BufferedInputStream bis=null;
		ObjectInputStream ois=null;
		OutputStream os=null;
		Evaluation e=new Evaluation();		
		
		send="c "+3+" "+filesearched+" "+path;
		
		for(int i=0;i<count;i++)
			{
				if(sport==array_ports[i])
				{
					id=i;
				}
			}
		
		try
		{
			reqfile= new ObjectOutputStream(csocket[id].getOutputStream());
			reqfile.writeObject(send);
			ois=new ObjectInputStream(csocket[id].getInputStream());
			length=(String)ois.readObject();
		}
		catch(Exception ee)
		{
		if(e.len==0)
		{
			while(!(csocket[id].isConnected()) && count<=3)
			{
				try
				{
				csocket[id]=new Socket(peerinfo[id][0],Integer.parseInt(Peer.peerinfo[id][1]));
				count=count+1;
				}
				catch(Exception ie)
				{}
								
			}
				System.out.println("select the neighour node in the peer list");
				ll=retrieve(filesearched);
				
				loc=selectpeer(ll.getlookfupfile(),ll.getpeerlist());
				obtain(loc);
				System.out.println(filesearched+" received");
				System.exit(0);
				
		}			
		}
		byte[] recvfile=new byte[64*1024];
		if(filesearched.contains("."))
		{	
			parts=filesearched.split("\\.");
			name=parts[0];
			ext=parts[1];
			if(name.contains("_cpy"))
			{
				name=name.concat("y");
				filesearched=(name+"."+ext).toString();
				file=new File(peerdirpath+"/"+filesearched);
			}
			else
			{
				name=name+"_cpy";
				filesearched=(name+"."+ext).toString();
				file=new File(peerdirpath+"/"+filesearched);
			}
			
		}
		else
		{
			if(filesearched.contains("_cpy"))
			{
			filesearched=filesearched.concat("y");
			file=new File(peerdirpath+"/"+filesearched);
			}
			else
			{
				filesearched=filesearched.concat("_cpy");
				file=new File(peerdirpath+"/"+filesearched);
			}
		}
		try{
			os=new FileOutputStream(file);
			InputStream instream=csocket[id].getInputStream();
			bis=new BufferedInputStream(instream);
			while((bis.available())>=0)
			{	
				c1=bis.read(recvfile);
				len=len+c1;
				os.write(recvfile,0,c1);
				if(Long.parseLong(length)==len)
				{	
					os.flush();
					break;	
				}
			}
		}
		catch(Exception eee)
		{
			
		}

		finally
		{
			try
			{
				os.flush();
				fops.close();
				
			}
			catch(Exception ef)
			{}			
		}
		
		
	return filesearched;
	}

	public void run() 
	{
		Peer pe=new Peer();
		try
		{
			String k;
			peerserver=new ServerSocket(servport);
			System.out.println("server started at port:"+servport);
			while(true)
			{	
				Socket peerclient1=peerserver.accept();
				if(peerclient1!=null)
				{
					Peer s= new Peer();
					s.client=peerclient1;
				}	
				ClientHandler ch=new ClientHandler();
				ch.find(peerclient1);
			}
		}
		catch(Exception ie)
		{	
			ie.printStackTrace();
		}
	}	
}


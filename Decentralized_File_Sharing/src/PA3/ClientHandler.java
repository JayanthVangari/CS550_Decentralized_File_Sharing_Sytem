package PA3;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
class ClientHandler implements Runnable
 {
	 File f;
	byte[] filetosend=new byte[64*1024];
	int len=0;	
	public static Hashtable<String,ArrayList<String>> ht=new Hashtable<String,ArrayList<String>>(4000003);
	ArrayList<String> ll;
	Peer pe=new Peer();
	String file,reqfile,path,name;
	 private Socket client;
	 public ClientHandler()
	 {
		 
	 }
	 public ClientHandler(Socket client)
	 {
	 this.client=client;
	 }
	public void find(Socket client)
 	{
 		 new Thread(new ClientHandler(client)).start();
	}
	
	@Override
	 public void run()
	 {
		byte[] bytearray=new byte[64*1024];
		ObjectInputStream ois;
		ObjectOutputStream ops;
		BufferedOutputStream bos;
		FileOutputStream fops=null;;		
		OutputStream os=null;		
		BufferedInputStream bis=null;
		DataOutputStream dos=null;
		Peer pe=new Peer();
		String file;
		String length=null;
		int c1=0,len;
			
		InetAddress addr=client.getInetAddress();
		String ipaddress=addr.getHostAddress();
		while(true)	
		{
			try
			{
				ois = new ObjectInputStream(client.getInputStream());
				ops=new ObjectOutputStream(client.getOutputStream());
				String message=(String)ois.readObject();
				byte[] recvfile=new byte[64*1024];
				long l=0;
		
				String[] parts=message.split(" ");
				if(parts[0].contentEquals("r"))
				{
					f=new File(pe.peerdirpath+"/"+parts[1]);
												
						
					try{
						os=new FileOutputStream(f);
						InputStream instream=client.getInputStream();
						bis=new BufferedInputStream(instream);
					
						while((bis.available())>=0)
						{	c1=bis.read(recvfile);
							l=l+c1;
							os.write(recvfile,0,c1);
							if(Long.parseLong(parts[2])==l)
							{	
								os.flush();
								break;								
								
							}
							
						}
						ops.writeObject(""+l);
			
					}
					
					
					catch(Exception e)
					{
			
					}
				}
				if( Integer.parseInt(parts[1])==1)
				{	
					int replica=Integer.parseInt(pe.peerinfo[pe.clientid][2]);
					if((Integer.parseInt(parts[0]))==replica)
					{
						if((Integer.parseInt(parts[0]))==0)
							replica=(Integer.parseInt(parts[0]))+1;
						else
							replica=replica-1;
					}
					
					parts[3]=replica+";"+parts[3].concat(ipaddress);
					if(ht.containsKey(parts[2]))
					{
						if(!ht.get(parts[2]).contains(parts[3]))	

							ht.get(parts[2]).add(parts[3]);
					}	
					else
					{
						ll=new ArrayList<String>();
						ll.add(parts[3]);
						ht.put(parts[2],ll);
					}
					ops.writeObject("success"+" "+replica);
				}
				else if(Integer.parseInt(parts[1])==2)
				{
					String value;
					if(ht.containsKey(parts[2]))
					{
						value=(ht.get(parts[2])).toString();
						ops.writeObject(value);
					}
					else
					{
						value="FNF";
						ops.writeObject(value);
					}
				}
				else if(Integer.parseInt(parts[1])==3)
				{
					name=parts[2];
					path=parts[3];
					file= path+"/"+name;
					f=new File(file);	
					ops.writeObject(""+f.length());
					bos=new BufferedOutputStream(client.getOutputStream());			
				
					FileInputStream infile=new FileInputStream(f);
					while(infile.available()>0)
					{	len=infile.read(filetosend);
						bos.write(filetosend,0,len);
					}
					bos.flush();

					
					infile.close();
					
					//bos.close();
					
				}
			}
			catch(EOFException eof)
			{}
			catch (Exception e) {}
		}
		
	}
}

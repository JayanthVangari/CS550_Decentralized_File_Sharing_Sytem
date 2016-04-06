package PA3;
import java.io.*;

	public class Location
	{
		String file;
		String saddress;
		int serverport;
		String path;
		public Location()
		{
			
		}
		public Location(String file,String saddress,int serverport,String path)
		{
			this.file=file;
			this.saddress=saddress;
			this.serverport=serverport;
			this.path=path;
		}
		
		public String getfname()
		{
			return this.file;
		}
		public String getsaddress()
		{
			return this.saddress;
			
		}
		public int getportadd()
		{
			return this.serverport;
		}
		String getpath()
		{
			return this.path;
		}
	}
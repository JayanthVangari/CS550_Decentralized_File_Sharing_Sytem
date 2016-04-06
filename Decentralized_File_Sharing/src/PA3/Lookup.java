package PA3;
import java.io.*;
class Lookup{
		String lookupfile;
		 String peerlist;
		public Lookup(String lookupfile,String peerlist)
		{
			this.lookupfile=lookupfile;
			this.peerlist=peerlist;
			
		}
		public Lookup() {
			
		}
		public String getlookfupfile()
		{
			return this.lookupfile;
		}
		public String getpeerlist()
		{
			return this.peerlist;
		}
	}

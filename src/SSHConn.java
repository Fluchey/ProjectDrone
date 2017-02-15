/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	SSHConnection.java
*
*/

import java.io.*;
import java.util.*;

import ch.ethz.ssh2.*;

public class SSHConn
{
	//Login credentials for ALL connections
	private String userName="pi";
	private String password="takemehigh";
	//This is dependent on the host ip, set via constructor
	private String hostName;

	//Connection variables
	private Connection conn;
	private boolean isAuthenticated;
	private Session sess;
	private InputStream stdOut;
	private BufferedReader br;
	private boolean isConnected = false;


	public SSHConn(String ip)
	{
		 this.hostName=ip;
	}

	public void initConnection(){
		try
		{
			System.out.println("Trying to connect to " + hostName);

			long startTime = System.currentTimeMillis();

			conn = new Connection(hostName);
			/* TODO Kolla in ifall vi kan ta ner timern om vi utesluter Johans RPi */
			/* Timeout för anslutning om man försöker ansluta i mer än 5 sekunder */
			conn.connect(null, 5000, 5000);

			isAuthenticated = conn.authenticateWithPassword(userName, password);
			if(isAuthenticated == false) throw new IOException("Authentication failed");

			sess = conn.openSession();
			isConnected = true;

			System.out.println("Connected to " + hostName);
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("It took: " + estimatedTime + "ms\n");

			stdOut = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdOut));

		}catch(Exception e)
		{
			System.out.println("Could not initiate connection to: "+hostName);
		}
	}
	
	public ArrayList getText()
	{
		initConnection();
		String command="cat /home/pi/ProjectDroneData/data.txt";
		ArrayList<String> textData = new ArrayList<String>();
		
		try
		{
			sess.execCommand(command);
			String line="";
			
			while ((line = br.readLine()) != null)
			{	
				textData.add(line);
			}
			
		}catch(Exception e)
		{
			System.out.println("Error reading text data from: " + hostName + " \n");
		}

		return textData;
	}

	public boolean getIsConnected() {
		return isConnected;
	}

	public void closeConnections()
	{
		sess.close();
		conn.close();
		isConnected = false;
	}
}
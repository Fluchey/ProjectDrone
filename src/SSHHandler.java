/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	SSHHandler.java
*
*/

import java.util.*;

public class SSHHandler {
	
	private ProjectDrone projectDrone;
//	Set<SSHConn> connections;
	
	public SSHHandler (ProjectDrone pd){
		projectDrone = pd;
		
	}
	
	public void connect(){
		/* initialize all connections */
		projectDrone.getOtherDevices().forEach((ipStr) -> {
			SSHConn conn = new SSHConn(ipStr);
			ArrayList otherDevicesData = conn.getText();
			if(conn.getIsConnected()) conn.closeConnections();
			projectDrone.getDevice().getDataContainer().updateOther(otherDevicesData);
		});
		
		//update data from the ones that responded
		
	}
}

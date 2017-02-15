/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	Device.java
*
*/

import java.util.*;

public class Device
{
	private String deviceName;
	private String ipAddress;
	private String macAddress;
	private DataContainer dataContainer;
	private LocalSensorHandler localSensorHandler;

	private HashMap<String, String> mapOfSensors; /* Lägger in alla mappade enheter med dess kopplade sensorer */

	
	public Device(String dn, String ip, String mac)
	{
		this.deviceName=dn;
		this.ipAddress=ip;
		this.macAddress=mac;
		this.dataContainer=new DataContainer(this);
		mapOfSensors = new HashMap<>();
		populateMapOfSensors();
		this.localSensorHandler = new LocalSensorHandler(this);
	}

	public void populateMapOfSensors(){
        /* Denna map länkar samman en temperaturmätare med en RPi */
		mapOfSensors.put("135", "192.168.1.3");
		mapOfSensors.put("151", "192.168.1.5"); /*toBeCompleted måste bytas ut */
	}

	public HashMap<String, String> getMapOfSensors() {
		return mapOfSensors;
	}
	
	public String getDeviceName()
	{
		return this.deviceName;
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public String getMacAddress()
	{
		return this.macAddress;
	}

	public DataContainer getDataContainer() {
		return dataContainer;
	}

	public LocalSensorHandler getLocalSensorHandler() {
		return localSensorHandler;
	}

	@Override
	public String toString()
	{
		return "Device Name: "+deviceName+" IP-Address: "+ipAddress+" MAC-address: "+macAddress;
	}
}

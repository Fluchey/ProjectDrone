/**
 * IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
 * Johan Bäckström, 831121-0119
 * Anton Fluch, 910630-3358
 * 
 * ProjectDrone.java
 */

import java.util.*;
import java.net.*;

public class ProjectDrone {
    private final static boolean DEBUG = false;
    private Device device;
    private SSHHandler sshHandler;
    private Set<String> otherDevices;

    
    public ProjectDrone() {
        try {
            this.device = createThisDevice();
        } catch (SocketException e) {
            System.out.println("Could not get local IP-, or MAC-addresses");
        }

        sshHandler = new SSHHandler(this);
        otherDevices = new TreeSet<>();
    }

    public static void main(String[] args) {
        ProjectDrone projectDrone = new ProjectDrone();
        projectDrone.run(); 
    }

    public void run() {
        populateDeviceMap();//Lägger in alla enheter utom den egna i en hashmap
        
        /*Test för att köra koden flera gånger*/
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        	@Override
        	public void run()
        	{
        		device.getLocalSensorHandler().updateLocalSensors(); //uppdaterar den lokala sensordatan
        		sshHandler.connect();
        		device.getDataContainer().printDevices();
        	}
        },0,2000);
    }

    public Device getDevice() {
        return this.device;
    }

    public SSHHandler getSshHandler() {
        return this.sshHandler;
    }

    public Set<String> getOtherDevices() {
        return this.otherDevices;
    }


    public Device createThisDevice() throws SocketException
    /* Tar reda på IP och mac-adress för nätverksinterface wlan0 */
    {
        String ipAddress = "";
        String mac = "";
        String iface = "";
        byte[] macAddress = null;

        if (DEBUG) System.out.println("Listing local interfaces...\n");

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        /* Loopa igenom samtliga nätverkskort */
        for (NetworkInterface netInt : Collections.list(interfaces))
        {
            /* tar reda på IP för endast wlan0 */
            if (netInt.getName().equals("wlan0"))//
            {
                iface = netInt.getName();

                Enumeration<InetAddress> inetAddresses = netInt.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) //loopa igenom samtliga ip-adresser f�r wlan0
                {
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        if (inetAddress.getHostAddress().startsWith("192.168") && Integer.parseInt(inetAddress.getHostAddress().substring(10)) < 10) {
                            if (DEBUG) System.out.println("Saving local IPv4-address and MAC-address... \n");

                            ipAddress = inetAddress.getHostAddress();
                            if (DEBUG) System.out.println(ipAddress);

                            macAddress = netInt.getHardwareAddress();
                            if (DEBUG) System.out.println(macAddress);

                            if (mac.length() < 17) {
                                for (int i = 0; i < macAddress.length; i++)//Returnerar rätt MAC-format
                                {
                                    String val = String.format("%02X", macAddress[i]);

                                    if (i == macAddress.length - 1) mac += val;
                                    else mac += val + ":";
                                }
                            }
                        }
                    }
                }
            }
        }
        if (DEBUG) System.out.println("IP of " + iface + ": " + ipAddress + ", MAC of " + iface + ": " + mac + "\n");

        return new Device("local device", ipAddress, mac);

    }

    public void populateDeviceMap() //Alla enheter hårdkodat, ta bort den lokala ur samlingen
    {
        if (DEBUG) System.out.println("Populating list of known devices, except the local device.\n");

//        Device d1 = new Device("Raspberry Pi (2) nummer 1", "192.168.1.1", "48:EE:0C:F2:C9:11");
//        Device d2 = new Device("Raspberry Pi (2) nummer 2", "192.168.1.2", "48:ee:0c:23:28:63");
//        Device d3 = new Device("Raspberry Pi (3) nummer 3", "192.168.1.3", "b8:27:eb:ce:f2:0e");
//        Device d4 = new Device("Raspberry Pi (3) nummer 4", "192.168.1.4", "b8:27:eb:f7:01:d3");
//        Device d5 = new Device("Raspberry Pi (3) nummer 5", "192.168.1.5", "b8:27:eb:ae:d5:1b");

        otherDevices.add("192.168.1.1");
        otherDevices.add("192.168.1.2");
        otherDevices.add("192.168.1.3"); 
        otherDevices.add("192.168.1.4");
        otherDevices.add("192.168.1.5");

        //JAVA 1.8
        Iterator<String> setIter = otherDevices.iterator();
        while (setIter.hasNext()) {
            String ip = setIter.next();
            if (this.device.getIpAddress().equals(ip))
                setIter.remove(); //Ta bort den lokala enheten
        }
    }

}
